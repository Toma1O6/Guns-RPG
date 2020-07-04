package dev.toma.gunsrpg.ai;

import dev.toma.gunsrpg.common.entity.EntityZombieGunner;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.Vec3d;

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
        boolean canSee = canSeeEntity(target);
        if(dist <= attackRange && canSee) {
            entity.getNavigator().clearPath();
        } else entity.getNavigator().tryMoveToEntityLiving(target, 1.0D);
        this.entity.faceEntity(target, 30, 30);
        this.entity.getLookHelper().setLookPositionWithEntity(target, 30, 30);
        if(canSee && dist < attackRange * 3 && --timeRemaining <= 0 && !entity.world.isRemote) {
            gun.shoot(entity.world, entity, entity.getHeldItemMainhand(), EntityZombieGunner.GUN_EQUIPMENT.get().get((GunItem) entity.getHeldItemMainhand().getItem()).event);
            timeRemaining = entity.rateOfFire;
        }
    }

    private boolean hasGun() {
        return entity.getHeldItemMainhand().getItem() instanceof GunItem;
    }

    private boolean canSeeEntity(EntityLivingBase target) {
        return ModUtils.raytraceBlocksIgnoreGlass(
                entity.world,
                new Vec3d(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ),
                new Vec3d(target.posX, target.posY + target.getEyeHeight(), target.posZ),
                state -> state.getBlock().isOpaqueCube(state)
        ) == null;
    }
}
