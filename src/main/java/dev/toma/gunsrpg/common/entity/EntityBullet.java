package dev.toma.gunsrpg.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityBullet extends Entity {

    public EntityBullet(World world) {
        super(world);
    }

    public EntityBullet(World world, EntityPlayer player, ItemStack stack) {
        super(world);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {

    }

    @Override
    protected void entityInit() {

    }
}
