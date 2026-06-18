package com.wf.firearms.network;

import com.wf.firearms.client.bloodmoon.BloodmoonClientState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

/** 同步主世界血月 active/forced 到客户端（红月、雾效、测试指令白天可见）。 */
public record BloodmoonSyncPacket(boolean active, boolean forced) {
    public static void encode(BloodmoonSyncPacket msg, net.minecraft.network.FriendlyByteBuf buf) {
        buf.writeBoolean(msg.active);
        buf.writeBoolean(msg.forced);
    }

    public static BloodmoonSyncPacket decode(net.minecraft.network.FriendlyByteBuf buf) {
        return new BloodmoonSyncPacket(buf.readBoolean(), buf.readBoolean());
    }

    public static void handle(BloodmoonSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> BloodmoonClientState.set(msg.active, msg.forced));
        ctx.get().setPacketHandled(true);
    }

    public static void sendTo(ServerPlayer player, boolean active, boolean forced) {
        ModNetwork.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player), new BloodmoonSyncPacket(active, forced));
    }

    public static void broadcast(ServerLevel level, boolean active, boolean forced) {
        if (level.dimension() != Level.OVERWORLD) {
            return;
        }
        for (ServerPlayer player : level.players()) {
            sendTo(player, active, forced);
        }
    }
}
