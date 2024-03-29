package dev.toma.gunsrpg.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;

public class AlwaysAggroOnGoal<E extends LivingEntity> extends NearestAttackableTargetGoal<E> {

    private final E targetEntity;

    public AlwaysAggroOnGoal(MobEntity entity, boolean requireSight, E targetEntity) {
        super(entity, null, requireSight);
        this.targetEntity = targetEntity;
    }

    @Override
    public void start() {
        this.mob.setTarget(target);
    }

    @Override
    public boolean canUse() {
        if (mob.getTarget() != targetEntity) {
            this.findTarget();
            return true;
        }
        return false;
    }

    @Override
    protected void findTarget() {
        this.target = targetEntity;
    }
}
