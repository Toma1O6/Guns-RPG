package com.wf.firearms.network;

import com.wf.firearms.gameplay.SkillUnlockService;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record UnlockSkillPacket(String skillId) {
    public static void encode(UnlockSkillPacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.skillId);
    }

    public static UnlockSkillPacket decode(FriendlyByteBuf buf) {
        return new UnlockSkillPacket(buf.readUtf(256));
    }

    public static void handle(UnlockSkillPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null || msg.skillId.isEmpty()) {
                return;
            }
            var result = SkillUnlockService.tryUnlock(player, msg.skillId);
            FirearmsProgressSyncPacket.sendTo(player);
            ModNetwork.CHANNEL.send(
                    net.minecraftforge.network.PacketDistributor.PLAYER.with(() -> player),
                    new UnlockSkillResultPacket(result.success(), result.message()));
        });
        ctx.get().setPacketHandled(true);
    }
}
