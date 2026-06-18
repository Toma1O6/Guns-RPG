package com.wf.firearms.network;

import com.wf.firearms.client.gui.SkillTreeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** 服务端解锁尝试结果（客户端提示 + 刷新技能树界面）。 */
public record UnlockSkillResultPacket(boolean success, String message) {
    public static void encode(UnlockSkillResultPacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.success);
        buf.writeUtf(msg.message, 256);
    }

    public static UnlockSkillResultPacket decode(FriendlyByteBuf buf) {
        return new UnlockSkillResultPacket(buf.readBoolean(), buf.readUtf(256));
    }

    public static void handle(UnlockSkillResultPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                applyClient(msg);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void applyClient(UnlockSkillResultPacket msg) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        String text = msg.message == null || msg.message.isEmpty()
                ? (msg.success ? "已解锁" : "无法解锁")
                : msg.message;
        mc.player.displayClientMessage(
                Component.literal((msg.success ? "§a[火器]§r " : "§c[火器]§r ") + text), false);
        if (mc.screen instanceof SkillTreeScreen screen) {
            screen.refreshAfterNetwork();
        }
    }
}
