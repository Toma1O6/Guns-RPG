package com.wf.firearms.network;

import com.wf.firearms.client.gui.SkillTreeScreen;
import com.wf.firearms.data.PlayerFirearmsData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/** 将火器进度同步到客户端，供技能树 UI 显示。 */
public record FirearmsProgressSyncPacket(
        int revision,
        int playerLevel,
        int totalKills,
        int skillPoints,
        int perkPoints,
        int sharedWeaponPoints,
        List<String> unlocked) {
    public static void encode(FirearmsProgressSyncPacket msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.revision);
        buf.writeVarInt(msg.playerLevel);
        buf.writeVarInt(msg.totalKills);
        buf.writeVarInt(msg.skillPoints);
        buf.writeVarInt(msg.perkPoints);
        buf.writeVarInt(msg.sharedWeaponPoints);
        buf.writeVarInt(msg.unlocked.size());
        for (String id : msg.unlocked) {
            buf.writeUtf(id, 128);
        }
    }

    public static FirearmsProgressSyncPacket decode(FriendlyByteBuf buf) {
        int revision = buf.readVarInt();
        int playerLevel = buf.readVarInt();
        int totalKills = buf.readVarInt();
        int skillPoints = buf.readVarInt();
        int perkPoints = buf.readVarInt();
        int sharedWeaponPoints = buf.readVarInt();
        int n = buf.readVarInt();
        List<String> unlocked = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            unlocked.add(buf.readUtf(128));
        }
        return new FirearmsProgressSyncPacket(
                revision, playerLevel, totalKills, skillPoints, perkPoints, sharedWeaponPoints, unlocked);
    }

    public static void handle(FirearmsProgressSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                applyClient(msg);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void applyClient(FirearmsProgressSyncPacket msg) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) {
            return;
        }
        if (msg.revision < PlayerFirearmsData.getSyncRevision(player)) {
            return;
        }
        if (mc.getSingleplayerServer() != null) {
            ServerPlayer serverPlayer = mc.getSingleplayerServer().getPlayerList().getPlayer(player.getUUID());
            if (serverPlayer != null) {
                PlayerFirearmsData.copyProgressFrom(serverPlayer, player);
                refreshSkillTree(mc);
                return;
            }
        }
        PlayerFirearmsData.setSyncRevision(player, msg.revision);
        PlayerFirearmsData.setPlayerLevel(player, msg.playerLevel);
        PlayerFirearmsData.setTotalKills(player, msg.totalKills);
        syncInt(player, msg.skillPoints, PlayerFirearmsData::getSkillPoints, PlayerFirearmsData::addSkillPoints);
        syncInt(player, msg.perkPoints, PlayerFirearmsData::getPerkPoints, PlayerFirearmsData::addPerkPoints);
        syncInt(
                player,
                msg.sharedWeaponPoints,
                PlayerFirearmsData::getSharedWeaponPoints,
                PlayerFirearmsData::addSharedWeaponPoints);
        PlayerFirearmsData.replaceUnlocked(player, msg.unlocked);
        refreshSkillTree(mc);
    }

    private static void syncInt(
            Player player,
            int target,
            java.util.function.ToIntFunction<Player> getter,
            PointDelta delta) {
        int current = getter.applyAsInt(player);
        if (current != target) {
            delta.apply(player, target - current);
        }
    }

    @FunctionalInterface
    private interface PointDelta {
        void apply(Player player, int delta);
    }

    @OnlyIn(Dist.CLIENT)
    private static void refreshSkillTree(Minecraft mc) {
        if (mc.screen instanceof SkillTreeScreen screen) {
            screen.refreshAfterNetwork();
        }
    }

    public static FirearmsProgressSyncPacket fromPlayer(Player player, int revision) {
        return new FirearmsProgressSyncPacket(
                revision,
                PlayerFirearmsData.getPlayerLevel(player),
                PlayerFirearmsData.getTotalKills(player),
                PlayerFirearmsData.getSkillPoints(player),
                PlayerFirearmsData.getPerkPoints(player),
                PlayerFirearmsData.getSharedWeaponPoints(player),
                new ArrayList<>(PlayerFirearmsData.unlockedSet(player)));
    }

    public static void sendTo(ServerPlayer player) {
        int revision = PlayerFirearmsData.bumpSyncRevision(player);
        ModNetwork.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player), fromPlayer(player, revision));
    }
}
