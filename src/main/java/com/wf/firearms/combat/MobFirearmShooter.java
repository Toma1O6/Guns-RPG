package com.wf.firearms.combat;

import com.wf.firearms.entity.BulletProjectile;
import com.wf.firearms.registry.ModEntities;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

/** Mob 无限弹药射击（不走玩家 FirearmCombat 管线）。 */
public final class MobFirearmShooter {
    private MobFirearmShooter() {}

    public static void fire(
            LivingEntity shooter,
            String weaponKey,
            AmmoMaterial ammoMaterial,
            float damageMultiplier,
            float inaccuracy) {
        if (shooter.level().isClientSide()) {
            return;
        }
        FirearmSpec spec = FirearmRegistry.getOrDefault(weaponKey);
        if (spec == null) {
            return;
        }
        Level level = shooter.level();
        float damage = spec.baseDamage() * damageMultiplier * ammoMaterial.damageMultiplier();
        AmmoCaliber caliber = AmmoCaliber.forWeapon(spec);
        float spread = Math.max(0.01f, spec.spread() + inaccuracy);
        int pellets = Math.max(1, spec.pelletCount());
        boolean pelletDecay = pellets > 1;
        for (int i = 0; i < pellets; i++) {
            BulletProjectile bullet =
                    new BulletProjectile(
                            ModEntities.BULLET.get(),
                            level,
                            shooter,
                            damage,
                            ammoMaterial,
                            caliber,
                            spec.weaponClass(),
                            spec.weaponKey(),
                            pelletDecay);
            bullet.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
            float pelletSpread = pellets > 1 ? spread * (0.85f + level.random.nextFloat() * 0.3f) : spread;
            bullet.shootFromRotation(
                    shooter, shooter.getXRot(), shooter.getYRot(), 0f, spec.projectileSpeed(), pelletSpread);
            level.addFreshEntity(bullet);
        }
        level.playSound(
                null,
                shooter.getX(),
                shooter.getY(),
                shooter.getZ(),
                SoundEvents.GENERIC_EXPLODE,
                SoundSource.HOSTILE,
                0.35f,
                1.4f + level.random.nextFloat() * 0.25f);
    }

    public static boolean canSeeTarget(LivingEntity shooter, LivingEntity target) {
        if (shooter instanceof Mob mob) {
            return mob.getSensing().hasLineOfSight(target);
        }
        return shooter.hasLineOfSight(target);
    }

    public static double attackRangeFor(FirearmSpec spec) {
        return switch (spec.weaponClass()) {
            case PISTOL -> 24;
            case SMG -> 32;
            case RIFLE, DMR, SNIPER -> 36;
            case SHOTGUN -> 14;
            case HEAVY -> 48;
            case LAUNCHER -> 40;
            default -> 28;
        };
    }

    public static float clampLook(LivingEntity mob, LivingEntity target) {
        double dx = target.getX() - mob.getX();
        double dz = target.getZ() - mob.getZ();
        double dy = target.getEyeY() - mob.getEyeY();
        double horiz = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) (Mth.atan2(dz, dx) * (180f / Math.PI)) - 90f;
        float pitch = (float) (-(Mth.atan2(dy, horiz) * (180f / Math.PI)));
        mob.setYRot(yaw);
        mob.setXRot(pitch);
        return pitch;
    }
}
