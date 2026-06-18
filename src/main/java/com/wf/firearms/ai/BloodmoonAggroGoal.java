package com.wf.firearms.ai;

import com.wf.firearms.bloodmoon.BloodmoonService;
import com.wf.firearms.config.BloodmoonConfig;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.EnumSet;

/** 血月：周期性在仇恨距离内锁定最近玩家（原版 BeAngryDuringBloodmoonGoal 移植）。 */
public class BloodmoonAggroGoal extends TargetGoal {
    private final TargetingConditions targetConditions;
    private Player target;

    public BloodmoonAggroGoal(Mob mob) {
        super(mob, false);
        setFlags(EnumSet.of(Goal.Flag.TARGET));
        double range = BloodmoonConfig.bloodMoonMobAgroRange();
        targetConditions =
                TargetingConditions.forCombat()
                        .range(range)
                        .ignoreLineOfSight()
                        .ignoreInvisibilityTesting()
                        .selector(
                                e ->
                                        e instanceof Player player
                                                && player.isAlive()
                                                && !player.isCreative()
                                                && !player.isSpectator());
    }

    @Override
    public boolean canUse() {
        Level level = mob.level();
        if (mob.getRandom().nextInt(10) != 0) {
            return false;
        }
        if (!BloodmoonService.isBloodMoon(level)) {
            return false;
        }
        findClosestPlayer();
        return target != null;
    }

    @Override
    public void start() {
        mob.setTarget(target);
        super.start();
    }

    private void findClosestPlayer() {
        target =
                mob.level()
                        .getNearestPlayer(
                                targetConditions,
                                mob,
                                mob.getX(),
                                mob.getEyeY(),
                                mob.getZ());
    }
}
