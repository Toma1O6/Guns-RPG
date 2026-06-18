package com.wf.firearms.gameplay;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.airdrop.AirdropService;
import com.wf.firearms.data.PlayerFirearmsData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

/** god_help_us 紧急个人空投。 */
public final class EmergencyAirdropService {
    private static final String COOLDOWN_KEY = "gunsrpg_god_help_cooldown_until";

    private EmergencyAirdropService() {}

    public static boolean tryActivate(ServerPlayer player) {
        if (!PlayerFirearmsData.isUnlocked(player, "god_help_us")) {
            player.displayClientMessage(Component.translatable("gunsrpg.skill.not_unlocked"), true);
            return false;
        }
        if (!(player.level() instanceof ServerLevel level)) {
            return false;
        }
        long now = level.getGameTime();
        long until = player.getPersistentData().getLong(COOLDOWN_KEY);
        if (now < until) {
            long remainTicks = until - now;
            long remainDays = (remainTicks + 23999) / 24000;
            player.displayClientMessage(
                    Component.translatable("gunsrpg.skill.god_help_us.cooldown", remainDays), true);
            return false;
        }
        boolean ok = AirdropService.spawnNearPlayer(level, player).isPresent();
        if (ok) {
            long cooldown = PerkEffectService.emergencyAirdropCooldownTicks(player);
            player.getPersistentData().putLong(COOLDOWN_KEY, now + cooldown);
            player.displayClientMessage(Component.translatable("gunsrpg.skill.god_help_us.success"), true);
            GunsRpg.LOGGER.info("[gunsrpg] {} 呼叫紧急空投", player.getGameProfile().getName());
        } else {
            player.displayClientMessage(Component.translatable("gunsrpg.skill.god_help_us.failed"), true);
        }
        return ok;
    }
}
