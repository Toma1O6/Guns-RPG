package dev.toma.gunsrpg.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityExplosiveArrow extends EntityArrow {

    public EntityExplosiveArrow(World world) {
        super(world);
    }

    public EntityExplosiveArrow(World world, EntityLivingBase entityLivingBase) {
        super(world, entityLivingBase);
        this.pickupStatus = PickupStatus.DISALLOWED;
    }

    @Override
    protected ItemStack getArrowStack() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void arrowHit(EntityLivingBase living) {
        if(!world.isRemote) {
            world.createExplosion(this, living.posX, living.posY, living.posZ, 2.0F, true);
            setDead();
        }
    }
}
