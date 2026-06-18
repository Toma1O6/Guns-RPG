package com.wf.firearms.network;

import com.wf.firearms.client.sound.ClientReloadSoundPlayer;
import com.wf.firearms.client.sound.ReloadSoundClip;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

/** 服务端 → 客户端：触发换弹/排障音效。 */
public record ReloadSoundPacket(String weaponKey, ReloadSoundClip clip, int durationTicks) {
    public static void send(ServerPlayer player, String weaponKey, ReloadSoundClip clip, int durationTicks) {
        if (player == null || weaponKey == null || durationTicks <= 0) {
            return;
        }
        ModNetwork.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> player),
                new ReloadSoundPacket(weaponKey, clip, durationTicks));
    }

    public static void encode(ReloadSoundPacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.weaponKey);
        buf.writeEnum(msg.clip);
        buf.writeVarInt(msg.durationTicks);
    }

    public static ReloadSoundPacket decode(FriendlyByteBuf buf) {
        return new ReloadSoundPacket(buf.readUtf(), buf.readEnum(ReloadSoundClip.class), buf.readVarInt());
    }

    public static void handle(ReloadSoundPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get()
                .enqueueWork(
                        () -> {
                            var mc = net.minecraft.client.Minecraft.getInstance();
                            if (mc.player != null) {
                                ClientReloadSoundPlayer.schedule(
                                        mc.player, msg.weaponKey, msg.clip, msg.durationTicks);
                            }
                        });
        ctx.get().setPacketHandled(true);
    }
}
