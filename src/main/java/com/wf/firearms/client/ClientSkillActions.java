package com.wf.firearms.client;

import com.wf.firearms.client.gui.SkillTreeScreen;
import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.gameplay.SkillUnlockService;
import com.wf.firearms.network.ModNetwork;
import com.wf.firearms.network.UnlockSkillPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * 技能解锁请求：单人游戏直接在服务端执行（避免网络包未回传导致 UI 无反应），多人仍走网络包。
 */
public final class ClientSkillActions {
    private ClientSkillActions() {}

    public static void requestSkillUnlock(String skillId) {
        if (skillId == null || skillId.isEmpty()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer client = mc.player;
        if (client == null) {
            return;
        }
        MinecraftServer server = mc.getSingleplayerServer();
        if (server != null) {
            ServerPlayer sp = server.getPlayerList().getPlayer(client.getUUID());
            if (sp != null) {
                SkillUnlockService.UnlockResult result = SkillUnlockService.tryUnlock(sp, skillId);
                if (result.success()) {
                    PlayerFirearmsData.copyProgressFrom(sp, client);
                }
                showUnlockFeedback(client, result);
                refreshSkillScreen(mc);
                return;
            }
        }
        ModNetwork.sendToServer(new UnlockSkillPacket(skillId));
    }

    /**
     * 主动技能（如 god_help_us 紧急空投）必须走服务端网络包处理。
     * 不可在客户端直接引用 {@code EmergencyAirdropService}，否则会 NoClassDefFoundError 崩溃。
     */
    public static void requestActiveSkill(String skillId) {
        if (skillId == null || skillId.isEmpty()) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return;
        }
        ModNetwork.sendActiveSkill(skillId);
        refreshSkillScreen(mc);
    }

    private static void showUnlockFeedback(Player player, SkillUnlockService.UnlockResult result) {
        String text = result.message() == null || result.message().isEmpty()
                ? (result.success() ? "已解锁" : "无法解锁")
                : result.message();
        player.displayClientMessage(
                Component.literal((result.success() ? "§a[火器]§r " : "§c[火器]§r ") + text), false);
    }

    private static void refreshSkillScreen(Minecraft mc) {
        if (mc.screen instanceof SkillTreeScreen screen) {
            screen.refreshAfterNetwork();
        }
    }
}
