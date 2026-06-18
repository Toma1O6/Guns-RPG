package com.wf.firearms.network;

import com.wf.firearms.GunsRpg;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/** 仅含网络字段；服务端逻辑延迟加载，避免客户端拉取 {@code EmergencyAirdropService} 崩溃。 */
public record ActiveSkillPacket(String skillId) {
    public static void encode(ActiveSkillPacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.skillId);
    }

    public static ActiveSkillPacket decode(FriendlyByteBuf buf) {
        return new ActiveSkillPacket(buf.readUtf(64));
    }

    public static void handle(ActiveSkillPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(
                () -> {
                    ServerPlayer player = ctx.get().getSender();
                    if (player == null) {
                        return;
                    }
                    dispatchServer(player, msg.skillId);
                });
        ctx.get().setPacketHandled(true);
    }

    private static void dispatchServer(ServerPlayer player, String skillId) {
        if (!"god_help_us".equals(skillId)) {
            return;
        }
        try {
            Class<?> svc = Class.forName("com.wf.firearms.gameplay.EmergencyAirdropService");
            svc.getMethod("tryActivate", ServerPlayer.class).invoke(null, player);
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.error("[gunsrpg] 主动技能处理失败: {}", skillId, ex);
        }
    }
}
