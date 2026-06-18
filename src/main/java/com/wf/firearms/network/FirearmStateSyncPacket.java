package com.wf.firearms.network;

import com.wf.firearms.client.FirearmHudState;
import com.wf.firearms.combat.FirearmMode;
import com.wf.firearms.combat.FirearmStackState;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

/** 服务端 → 客户端：弹匣与动作状态（HUD）。 */
public record FirearmStateSyncPacket(
        int ammo,
        int magSize,
        boolean jammed,
        FirearmMode fireMode,
        FirearmStackState.Action action,
        long actionStart,
        long actionEnd,
        long gameTime) {

    public static void send(
            ServerPlayer player,
            int ammo,
            int magSize,
            boolean jammed,
            FirearmMode fireMode,
            FirearmStackState.Action action,
            long actionStart,
            long actionEnd,
            long gameTime) {
        ModNetwork.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new FirearmStateSyncPacket(
                        ammo, magSize, jammed, fireMode, action, actionStart, actionEnd, gameTime));
    }

    public static void encode(FirearmStateSyncPacket msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.ammo);
        buf.writeVarInt(msg.magSize);
        buf.writeBoolean(msg.jammed);
        buf.writeEnum(msg.fireMode);
        buf.writeEnum(msg.action);
        buf.writeVarLong(msg.actionStart);
        buf.writeVarLong(msg.actionEnd);
        buf.writeVarLong(msg.gameTime);
    }

    public static FirearmStateSyncPacket decode(FriendlyByteBuf buf) {
        return new FirearmStateSyncPacket(
                buf.readVarInt(),
                buf.readVarInt(),
                buf.readBoolean(),
                buf.readEnum(FirearmMode.class),
                buf.readEnum(FirearmStackState.Action.class),
                buf.readVarLong(),
                buf.readVarLong(),
                buf.readVarLong());
    }

    public static void handle(FirearmStateSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> FirearmHudState.update(msg));
        ctx.get().setPacketHandled(true);
    }
}
