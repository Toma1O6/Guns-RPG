package com.wf.firearms.network;

import com.wf.firearms.data.PerkDef;
import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.data.SkillDatabase;
import com.wf.firearms.network.FirearmsProgressSyncPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record AdjustPerkPacket(String perkId, int delta) {
    public static void encode(AdjustPerkPacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.perkId);
        buf.writeVarInt(msg.delta);
    }

    public static AdjustPerkPacket decode(FriendlyByteBuf buf) {
        return new AdjustPerkPacket(buf.readUtf(128), buf.readVarInt());
    }

    public static void handle(AdjustPerkPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null || msg.delta == 0) {
                return;
            }
            SkillDatabase.getPerk(msg.perkId).ifPresent(perk -> apply(player, perk, msg.delta));
        });
        ctx.get().setPacketHandled(true);
    }

    private static void apply(ServerPlayer player, PerkDef perk, int delta) {
        int cur = Math.max(0, PlayerFirearmsData.getPerkInvestment(player, perk.getId()));
        if (delta < 0) {
            if (cur <= 0) {
                return;
            }
            int next = cur + delta;
            if (next < 0) {
                next = 0;
            }
            if (next == cur) {
                return;
            }
            PlayerFirearmsData.addPerkPoints(player, cur - next);
            PlayerFirearmsData.setPerkInvestment(player, perk.getId(), next);
            FirearmsProgressSyncPacket.sendTo(player);
            return;
        }
        if (delta <= 0) {
            return;
        }
        int next = cur + delta;
        int maxUp = PlayerFirearmsData.maxPerkSteps(perk, true);
        next = Math.min(maxUp, next);
        if (next == cur) {
            return;
        }
        int cost = next - cur;
        if (PlayerFirearmsData.getPerkPoints(player) < cost) {
            player.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("§c属性点不足"), true);
            return;
        }
        PlayerFirearmsData.addPerkPoints(player, -cost);
        PlayerFirearmsData.setPerkInvestment(player, perk.getId(), next);
        FirearmsProgressSyncPacket.sendTo(player);
    }
}
