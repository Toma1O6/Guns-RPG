package dev.toma.gunsrpg.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityExplosiveArrow extends AbstractArrowEntity {

    public int blastSize;

    public EntityExplosiveArrow(World world) {
        super(world);
    }

    public EntityExplosiveArrow(World world, LivingEntity entity, int blastSize) {
        super(world, entity);
        this.pickupStatus = PickupStatus.DISALLOWED;
        this.blastSize = blastSize;
    }

    @Override
    protected ItemStack getArrowStack() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void arrowHit(EntityLivingBase living) {
        if(!world.isRemote) {
            world.createExplosion(this, living.posX, living.posY, living.posZ, shootingEntity instanceof ExplosiveSkeletonEntity ? 2 : 1, false);
            setDead();
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(world.isRemote) {
            for(int i = 0; i < 4; i++) {
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, posX, posY, posZ, r(5), r(5), r(5));
                world.spawnParticle(EnumParticleTypes.FLAME, posX, posY, posZ, r(3), r(3), r(3));
            }
        }
    }

    private double r(int mod) {
        return rand.nextDouble() / mod - rand.nextDouble() / mod;
    }
}
