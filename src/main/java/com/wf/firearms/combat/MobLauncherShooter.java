package com.wf.firearms.combat;

import com.wf.firearms.entity.LaunchedExplosiveEntity;
import com.wf.firearms.launcher.LauncherShellStats;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** Mob 榴弹/火箭发射（无限弹药，不走玩家换弹管线）。 */
public final class MobLauncherShooter {
    private MobLauncherShooter() {}

    public static void fire(
            LivingEntity shooter,
            String weaponKey,
            Item shellItem,
            float damageMultiplier,
            float inaccuracy) {
        if (shooter.level().isClientSide() || shellItem == null) {
            return;
        }
        FirearmSpec spec = FirearmRegistry.getOrDefault(weaponKey);
        if (spec == null || spec.weaponClass() != WeaponClass.LAUNCHER) {
            return;
        }
        LauncherShellStats base = LauncherShellStats.forItem(shellItem);
        float radius = base.blastRadius();
        float damage = Math.max(1f, base.explosionDamage() * damageMultiplier);
        boolean rocket = base.rocket() || "rocket_launcher".equals(weaponKey);
        Level level = shooter.level();
        ItemStack visual = new ItemStack(shellItem);
        LaunchedExplosiveEntity projectile =
                new LaunchedExplosiveEntity(
                        level,
                        shooter,
                        visual,
                        base.fuseTicks(),
                        radius,
                        damage,
                        base.explodeOnImpact() || rocket,
                        rocket,
                        base.incendiary(),
                        base.toxic());
        projectile.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
        float spread = Math.max(0.02f, spec.spread() + inaccuracy);
        float speed = spec.projectileSpeed() * (rocket ? 1.8f : 1.1f);
        projectile.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot(), 0f, speed, spread);
        level.addFreshEntity(projectile);
        level.playSound(
                null,
                shooter.getX(),
                shooter.getY(),
                shooter.getZ(),
                SoundEvents.GENERIC_EXPLODE,
                SoundSource.HOSTILE,
                0.5f,
                0.85f + level.random.nextFloat() * 0.2f);
    }
}
