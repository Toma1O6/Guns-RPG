package dev.toma.gunsrpg.ai;

import dev.toma.gunsrpg.world.cap.WorldDataFactory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;

public class EntityAIBowRangedAttackIgnoreSight<T extends EntityMob & IRangedAttackMob> extends EntityAIBase {

    protected final T entity;
    protected final double moveSpeedAmp;
    protected int attackCooldown;
    protected final float maxAttackDistance;
    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public EntityAIBowRangedAttackIgnoreSight(T entity, double speedAmp, int attackCooldown, float maxAttackDistance) {
        this.entity = entity;
        this.moveSpeedAmp = speedAmp;
        this.attackCooldown = attackCooldown;
        this.maxAttackDistance = maxAttackDistance * maxAttackDistance;
        this.setMutexBits(3);
    }

    public void setAttackCooldown(int attackCooldown) {
        this.attackCooldown = attackCooldown;
    }

    @Override
    public void startExecuting() {
        super.startExecuting();
        entity.setSwingingArms(true);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        entity.setSwingingArms(false);
        seeTime = 0;
        attackTime = -1;
        entity.resetActiveHand();
    }

    @Override
    public boolean shouldExecute() {
        return this.entity.getAttackTarget() != null && this.isBowMainHand();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return (this.shouldExecute() || !this.entity.getNavigator().noPath()) && this.isBowMainHand();
    }

    protected boolean isBowMainHand() {
        return !this.entity.getHeldItemMainhand().isEmpty() && this.entity.getHeldItemMainhand().getItem() == Items.BOW;
    }

    @Override
    public void updateTask() {
        EntityLivingBase entitylivingbase = this.entity.getAttackTarget();
        if (entitylivingbase != null) {
            double distanceToTarget = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
            boolean canSee = this.entity.getEntitySenses().canSee(entitylivingbase) || WorldDataFactory.isBloodMoon(entity.world);
            boolean hasSeen = this.seeTime > 0;

            if (canSee != hasSeen) {
                this.seeTime = 0;
            }

            if (canSee) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (distanceToTarget <= (double)this.maxAttackDistance && this.seeTime >= 20) {
                this.entity.getNavigator().clearPath();
                ++this.strafingTime;
            } else {
                this.entity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.moveSpeedAmp);
                this.strafingTime = -1;
            }

            if (this.strafingTime >= 20) {
                if ((double)this.entity.getRNG().nextFloat() < 0.3D) {
                    this.strafingClockwise = !this.strafingClockwise;
                }
                if ((double)this.entity.getRNG().nextFloat() < 0.3D) {
                    this.strafingBackwards = !this.strafingBackwards;
                }
                this.strafingTime = 0;
            }

            if (this.strafingTime > -1) {
                if (distanceToTarget > (double)(this.maxAttackDistance * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (distanceToTarget < (double)(this.maxAttackDistance * 0.25F)) {
                    this.strafingBackwards = true;
                }

                this.entity.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.entity.faceEntity(entitylivingbase, 30.0F, 30.0F);
            } else {
                this.entity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
            }

            if (this.entity.isHandActive()) {
                if (!canSee && this.seeTime < -60) {
                    this.entity.resetActiveHand();
                } else if (canSee) {
                    int i = this.entity.getItemInUseMaxCount();

                    if (i >= 20) {
                        this.entity.resetActiveHand();
                        this.entity.attackEntityWithRangedAttack(entitylivingbase, ItemBow.getArrowVelocity(i));
                        this.attackTime = this.attackCooldown;
                    }
                }
            } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                this.entity.setActiveHand(EnumHand.MAIN_HAND);
            }
        }
    }
}
