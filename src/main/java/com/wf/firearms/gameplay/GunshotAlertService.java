package com.wf.firearms.gameplay;

import com.wf.firearms.combat.FirearmSpec;
import com.wf.firearms.combat.WeaponClass;
import com.wf.firearms.config.GunshotAlertConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

/**
 * 开枪吸引附近僵尸（对齐原版 {@code AbstractProjectile#aggroNearby}）。
 * 吸引半径 = 基础范围 × 枪族响度 × 消音/天赋减噪。
 */
public final class GunshotAlertService {
    private GunshotAlertService() {}

    public static void onGunFire(Player player, String weaponKey, FirearmSpec spec) {
        if (player == null || player.level().isClientSide || player.isCreative() || player.isSpectator()) {
            return;
        }
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }
        WeaponClass weaponClass = spec != null ? spec.weaponClass() : WeaponClass.OTHER;
        double noiseMultiplier = noiseMultiplier(player, weaponKey, weaponClass);
        double range = alertRange(noiseMultiplier);
        if (range <= 0.0) {
            return;
        }
        alertNearby(level, player, range);
    }

    /** 有效枪声倍率（越小越安静）。 */
    public static double noiseMultiplier(Player player, String weaponKey, WeaponClass weaponClass) {
        boolean silenced = WeaponExtensionService.isSilenced(player, weaponKey);
        double mult =
                GunshotAlertConfig.classNoise(weaponClass)
                        * GunshotAlertConfig.weaponNoiseOverride(weaponKey)
                        * PerkEffectService.weaponNoiseMultiplier(player, weaponKey, silenced);
        return Math.max(0.0, mult);
    }

    public static double alertRange(double noiseMultiplier) {
        double range = GunshotAlertConfig.baseAggroRange() * noiseMultiplier;
        range = Math.max(GunshotAlertConfig.minAggroRange(), range);
        range = Math.min(GunshotAlertConfig.maxAggroRange(), range);
        return range;
    }

    private static void alertNearby(ServerLevel level, Player player, double range) {
        AABB box = player.getBoundingBox().inflate(range);
        double rangeSq = range * range;
        double directSq = GunshotAlertConfig.directAggroDistance() * GunshotAlertConfig.directAggroDistance();

        if (GunshotAlertConfig.zombiesOnly()) {
            for (Zombie zombie : level.getEntitiesOfClass(Zombie.class, box, LivingEntity::isAlive)) {
                applyAlert(player, zombie, rangeSq, directSq);
            }
            return;
        }

        for (Monster monster : level.getEntitiesOfClass(Monster.class, box, LivingEntity::isAlive)) {
            applyAlert(player, monster, rangeSq, directSq);
        }
    }

    private static void applyAlert(Player player, Mob mob, double rangeSq, double directSq) {
        if (mob.distanceToSqr(player) > rangeSq) {
            return;
        }
        if (mob.getTarget() == null) {
            mob.getNavigation().moveTo(player.getX(), player.getY(), player.getZ(), 1.0D);
        }
        if (mob.distanceToSqr(player) <= directSq) {
            mob.setTarget(player);
        }
    }
}
