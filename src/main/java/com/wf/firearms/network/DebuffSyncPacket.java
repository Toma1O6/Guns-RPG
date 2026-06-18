package com.wf.firearms.network;

import com.wf.firearms.client.debuff.ClientDebuffState;
import com.wf.firearms.debuff.DebuffConfig;
import com.wf.firearms.debuff.DebuffSyncService;
import com.wf.firearms.debuff.DebuffType;
import com.wf.firearms.debuff.PlayerDebuffData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

/** 同步 debuff HUD 快照到客户端。 */
public record DebuffSyncPacket(
        Anim anim,
        DebuffType animType,
        boolean fractureActive,
        int fractureDuration,
        int fractureMaxDuration,
        int fractureProgress,
        int bleedStage,
        int bleedProgress,
        boolean bleedResist,
        float bleedResistProgress,
        int poisonStage,
        int poisonProgress,
        int infectionStage,
        int infectionProgress) {

    public enum Anim {
        NONE,
        APPLY,
        WORSE,
        HEAL,
        CURE
    }

    public static void encode(DebuffSyncPacket msg, FriendlyByteBuf buf) {
        buf.writeByte(msg.anim.ordinal());
        buf.writeByte(msg.animType == null ? -1 : msg.animType.ordinal());
        buf.writeBoolean(msg.fractureActive);
        buf.writeVarInt(msg.fractureDuration);
        buf.writeVarInt(msg.fractureMaxDuration);
        buf.writeVarInt(msg.fractureProgress);
        buf.writeVarInt(msg.bleedStage);
        buf.writeVarInt(msg.bleedProgress);
        buf.writeBoolean(msg.bleedResist);
        buf.writeFloat(msg.bleedResistProgress);
        buf.writeVarInt(msg.poisonStage);
        buf.writeVarInt(msg.poisonProgress);
        buf.writeVarInt(msg.infectionStage);
        buf.writeVarInt(msg.infectionProgress);
    }

    public static DebuffSyncPacket decode(FriendlyByteBuf buf) {
        Anim anim = Anim.values()[Math.min(buf.readByte(), Anim.values().length - 1)];
        int typeOrd = buf.readByte();
        DebuffType animType = typeOrd >= 0 && typeOrd < DebuffType.values().length
                ? DebuffType.values()[typeOrd]
                : null;
        return new DebuffSyncPacket(
                anim,
                animType,
                buf.readBoolean(),
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readBoolean(),
                buf.readFloat(),
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readVarInt());
    }

    public static void handle(DebuffSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                applyClient(msg);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void applyClient(DebuffSyncPacket msg) {
        ClientDebuffState.applySnapshot(msg);
    }

    public static DebuffSyncPacket snapshot(ServerPlayer player, Anim anim, DebuffType animType) {
        boolean fracture = PlayerDebuffData.hasFracture(player);
        int fracMax = fracture ? PlayerDebuffData.getFractureMaxDuration(player) : 0;
        int fracDur = fracture ? PlayerDebuffData.getFractureDuration(player) : 0;
        int fracProgress = fracture ? DebuffSyncService.computeFractureProgress(player) : 0;

        int bleedStage = PlayerDebuffData.getStage(player, DebuffType.BLEED);
        int poisonStage = PlayerDebuffData.getStage(player, DebuffType.POISON);
        int infectionStage = PlayerDebuffData.getStage(player, DebuffType.INFECTION);

        int bleedPause = PlayerDebuffData.getBleedPauseTicks(player);
        int bleedPauseMax = PlayerDebuffData.getBleedPauseMax(player);
        boolean bleedResist = bleedPause > 0;
        float bleedResistProgress = 0f;
        if (bleedResist && bleedPauseMax > 0) {
            bleedResistProgress = 1f - bleedPause / (float) bleedPauseMax;
        }

        return new DebuffSyncPacket(
                anim,
                animType,
                fracture,
                fracDur,
                Math.max(1, fracMax),
                fracProgress,
                bleedStage,
                DebuffSyncService.computeProgress(player, DebuffType.BLEED),
                bleedResist,
                bleedResistProgress,
                poisonStage,
                DebuffSyncService.computeProgress(player, DebuffType.POISON),
                infectionStage,
                DebuffSyncService.computeProgress(player, DebuffType.INFECTION));
    }

    public static void sendTo(ServerPlayer player, Anim anim, DebuffType animType) {
        ModNetwork.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player), snapshot(player, anim, animType));
    }
}
