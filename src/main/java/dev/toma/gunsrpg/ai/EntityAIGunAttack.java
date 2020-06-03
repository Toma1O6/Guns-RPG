package dev.toma.gunsrpg.ai;

import dev.toma.gunsrpg.common.entity.EntityZombieGunner;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIGunAttack extends EntityAIBase {

    protected static final int[] ATTACK_RANGE_TABLE = {10, 15, 20, 25, 10};
    protected final EntityZombieGunner entity;
    protected int timeRemaining = 10;

    public EntityAIGunAttack(EntityZombieGunner gunner) {
        this.entity = gunner;
        setMutexBits(3);
    }

    @Override
    public boolean shouldExecute() {
        if(!this.hasGun()) {
            return false;
        }
        return entity.getAttackTarget() != null;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute() || !this.entity.getNavigator().noPath() || !this.hasGun();
    }

    @Override
    public void resetTask() {
        timeRemaining = 10;
    }

    @Override
    public void updateTask() {
        EntityLivingBase target = entity.getAttackTarget();
        if(target == null || !hasGun()) return;
        double dist = Math.sqrt(entity.getDistanceSq(target));
        GunItem gun = (GunItem) entity.getHeldItemMainhand().getItem();
        double attackRange = ATTACK_RANGE_TABLE[gun.getGunType().ordinal()];
        boolean canSee = entity.getEntitySenses().canSee(target);
        if(dist <= attackRange && canSee) {
            entity.getNavigator().clearPath();
        } else entity.getNavigator().tryMoveToEntityLiving(target, 1.0D);
        this.entity.faceEntity(target, 30, 30);
        this.entity.getLookHelper().setLookPositionWithEntity(target, 30, 30);
        if(canSee && dist < attackRange * 3 && --timeRemaining <= 0 && !entity.world.isRemote) {
            gun.shoot(entity.world, entity, entity.getHeldItemMainhand());
            timeRemaining = entity.rateOfFire;
        }
    }

    private boolean hasGun() {
        return entity.getHeldItemMainhand().getItem() instanceof GunItem;
    }
}
