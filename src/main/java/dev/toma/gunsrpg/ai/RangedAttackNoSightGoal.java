package dev.toma.gunsrpg.ai;

import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.world.cap.WorldData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class RangedAttackNoSightGoal<T extends MobEntity & IRangedAttackMob> extends Goal {

    protected final T entity;
    protected final double moveSpeedAmp;
    protected final float maxAttackDistance;
    protected int attackCooldown;
    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public RangedAttackNoSightGoal(T entity, double speedAmp, int attackCooldown, float maxAttackDistance) {
        this.entity = entity;
        this.moveSpeedAmp = speedAmp;
        this.attackCooldown = attackCooldown;
        this.maxAttackDistance = maxAttackDistance * maxAttackDistance;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public void setAttackCooldown(int attackCooldown) {
        this.attackCooldown = attackCooldown;
    }

    @Override
    public void start() {
        entity.setAggressive(true);
        attackTime = attackCooldown;
    }

    @Override
    public void stop() {
        entity.setAggressive(false);
        seeTime = 0;
        attackTime = -1;
        entity.stopUsingItem();
    }

    @Override
    public boolean canUse() {
        return this.entity.getTarget() != null && this.isBowMainHand();
    }

    @Override
    public boolean canContinueToUse() {
        return (this.canUse() || !this.entity.getNavigation().isDone()) && this.isBowMainHand();
    }

    protected boolean isBowMainHand() {
        return this.entity.isHolding(item -> item == ModItems.GRENADE_LAUNCHER);
    }

    @Override
    public void tick() {
        LivingEntity livingEntity = this.entity.getTarget();
        if (livingEntity != null) {
            double distanceToTarget = this.entity.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
            boolean canSee = this.entity.getSensing().canSee(livingEntity) || WorldData.isBloodMoon(entity.level);
            boolean hasSeen = this.seeTime > 0;

            if (canSee != hasSeen) {
                this.seeTime = 0;
            }

            if (canSee) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (distanceToTarget <= (double) this.maxAttackDistance && this.seeTime >= 20) {
                this.entity.getNavigation().stop();
                ++this.strafingTime;
            } else {
                this.entity.getNavigation().moveTo(livingEntity, this.moveSpeedAmp);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if ((double) this.entity.getRandom().nextFloat() < 0.3D) {
                    this.strafingClockwise = !this.strafingClockwise;
                }
                if ((double) this.entity.getRandom().nextFloat() < 0.3D) {
                    this.strafingBackwards = !this.strafingBackwards;
                }
                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (distanceToTarget > (double) (this.maxAttackDistance * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (distanceToTarget < (double) (this.maxAttackDistance * 0.25F)) {
                    this.strafingBackwards = true;
                }

                this.entity.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.entity.lookAt(livingEntity, 30.0F, 30.0F);
            } else {
                this.entity.getLookControl().setLookAt(livingEntity, 30.0F, 30.0F);
            }

            if (canSee) {
                --attackTime;
                if (attackTime <= 0) {
                    this.entity.performRangedAttack(livingEntity, 0);
                    this.attackTime = this.attackCooldown;
                }
            }
        }
    }
}
