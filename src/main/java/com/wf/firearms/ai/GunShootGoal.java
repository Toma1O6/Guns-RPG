package com.wf.firearms.ai;

import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.combat.FirearmSpec;
import com.wf.firearms.combat.MobFirearmShooter;
import com.wf.firearms.config.MobSpawnConfig;
import com.wf.firearms.entity.ZombieGunnerEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class GunShootGoal extends Goal {
    private final ZombieGunnerEntity gunner;
    private int fireCooldown;
    private int shotsInMag;
    private int reloadCooldown;

    public GunShootGoal(ZombieGunnerEntity gunner) {
        this.gunner = gunner;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = gunner.getTarget();
        return gunner.isLoadoutReady() && target != null && target.isAlive();
    }

    @Override
    public void start() {
        fireCooldown = 0;
        reloadCooldown = 0;
        shotsInMag = magazineSize();
    }

    @Override
    public void tick() {
        LivingEntity target = gunner.getTarget();
        if (target == null || !gunner.isLoadoutReady()) {
            return;
        }
        FirearmSpec spec = FirearmRegistry.getOrDefault(gunner.getWeaponKey());
        double range = MobFirearmShooter.attackRangeFor(spec);
        double dist = gunner.distanceToSqr(target);
        boolean canSee = MobFirearmShooter.canSeeTarget(gunner, target);
        if (dist <= range * range && canSee) {
            gunner.getNavigation().stop();
        } else if (dist <= range * range * 4) {
            gunner.getNavigation().moveTo(target, 1.05);
        } else {
            gunner.getNavigation().moveTo(target, 1.15);
        }
        MobFirearmShooter.clampLook(gunner, target);

        if (reloadCooldown > 0) {
            --reloadCooldown;
            gunner.setReloading(true);
            return;
        }
        gunner.setReloading(false);

        if (--fireCooldown > 0 || !canSee || dist > range * range) {
            return;
        }
        if (!gunner.level().isClientSide) {
            MobFirearmShooter.fire(
                    gunner,
                    gunner.getWeaponKey(),
                    gunner.getAmmoMaterial(),
                    gunner.getDamageMultiplier(),
                    gunner.getInaccuracy());
            if (!MobSpawnConfig.global().skipReload()) {
                --shotsInMag;
                if (shotsInMag <= 0) {
                    shotsInMag = magazineSize();
                    reloadCooldown = Math.max(10, spec.reloadTicks());
                }
            }
        }
        fireCooldown = Math.max(1, gunner.getFireIntervalTicks());
    }

    private int magazineSize() {
        FirearmSpec spec = FirearmRegistry.getOrDefault(gunner.getWeaponKey());
        return Math.max(1, spec.magazineSize());
    }
}
