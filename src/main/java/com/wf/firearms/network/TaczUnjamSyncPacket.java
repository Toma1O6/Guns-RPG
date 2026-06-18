package com.wf.firearms.network;

import com.wf.firearms.client.compat.tacz.TaczClientAnim;
import com.wf.firearms.client.compat.tacz.TaczUnjamClientState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

/** 服务端 → 客户端：TaCZ 排障进度（HUD 进度条；动画只播一次检视）。 */
public record TaczUnjamSyncPacket(boolean active, long startTick, long endTick) {
    public static void sendStart(ServerPlayer player, long startTick, long endTick) {
        if (player == null || endTick <= startTick) {
            return;
        }
        ModNetwork.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new TaczUnjamSyncPacket(true, startTick, endTick));
    }

    public static void sendStop(ServerPlayer player) {
        if (player == null) {
            return;
        }
        ModNetwork.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new TaczUnjamSyncPacket(false, 0, 0));
    }

    public static void encode(TaczUnjamSyncPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.active);
        buf.writeVarLong(msg.startTick);
        buf.writeVarLong(msg.endTick);
    }

    public static TaczUnjamSyncPacket decode(FriendlyByteBuf buf) {
        return new TaczUnjamSyncPacket(buf.readBoolean(), buf.readVarLong(), buf.readVarLong());
    }

    public static void handle(TaczUnjamSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get()
                .enqueueWork(
                        () -> {
                            var mc = net.minecraft.client.Minecraft.getInstance();
                            if (mc.player == null) {
                                return;
                            }
                            if (msg.active) {
                                TaczUnjamClientState.begin(msg.startTick, msg.endTick);
                                TaczClientAnim.playInspect(mc.player);
                            } else {
                                TaczUnjamClientState.clear();
                            }
                        });
        ctx.get().setPacketHandled(true);
    }
}
