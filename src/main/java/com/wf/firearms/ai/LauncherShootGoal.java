package com.wf.firearms.ai;

import com.wf.firearms.bloodmoon.BloodmoonService;
import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.combat.FirearmSpec;
import com.wf.firearms.combat.MobFirearmShooter;
import com.wf.firearms.combat.MobLauncherShooter;
import com.wf.firearms.config.MobSpawnConfig;
import com.wf.firearms.entity.ExplosiveSkeletonEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class LauncherShootGoal extends Goal {
    private final ExplosiveSkeletonEntity grenadier;
    private int fireCooldown;

    public LauncherShootGoal(ExplosiveSkeletonEntity grenadier) {
        this.grenadier = grenadier;
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = grenadier.getTarget();
        return grenadier.isLoadoutReady() && target != null && target.isAlive();
    }

    @Override
    public void start() {
        fireCooldown = 0;
    }

    @Override
    public void tick() {
        LivingEntity target = grenadier.getTarget();
        if (target == null || !grenadier.isLoadoutReady()) {
            return;
        }
        FirearmSpec spec = FirearmRegistry.getOrDefault(grenadier.getWeaponKey());
        double range = MobFirearmShooter.attackRangeFor(spec);
        double dist = grenadier.distanceToSqr(target);
        boolean canSee = canShootAt(target);
        if (dist <= range * range && canSee) {
            grenadier.getNavigation().stop();
        } else if (dist <= range * range * 4) {
            grenadier.getNavigation().moveTo(target, 1.05);
        } else {
            grenadier.getNavigation().moveTo(target, 1.12);
        }
        MobFirearmShooter.clampLook(grenadier, target);

        if (--fireCooldown > 0 || !canSee || dist > range * range) {
            return;
        }
        if (!grenadier.level().isClientSide()) {
            MobLauncherShooter.fire(
                    grenadier,
                    grenadier.getWeaponKey(),
                    grenadier.pickShellForShot(),
                    grenadier.getDamageMultiplier(),
                    grenadier.getInaccuracy());
        }
        fireCooldown = Math.max(1, grenadier.getFireIntervalTicks());
    }

    private boolean canShootAt(LivingEntity target) {
        var combat = MobSpawnConfig.mob("gunsrpg:explosive_skeleton");
        boolean ignoreLos =
                combat != null
                        && combat.combat().bloodmoonIgnoreLineOfSight()
                        && BloodmoonService.isBloodMoon(grenadier.level());
        if (ignoreLos) {
            return true;
        }
        return MobFirearmShooter.canSeeTarget(grenadier, target);
    }
}
