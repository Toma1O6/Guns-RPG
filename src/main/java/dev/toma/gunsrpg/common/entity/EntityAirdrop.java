package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.tileentity.TileEntityAirdrop;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAirdrop extends Entity {

    public EntityAirdrop(World world) {
        super(world);
        this.setSize(1.0F, 1.0F);
    }

    @Override
    public void onUpdate() {
        if(!onGround) {
            this.motionY = -0.05F;
        } else {
            if(!world.isRemote) {
                BlockPos pos = this.getPosition();
                world.setBlockState(pos, ModRegistry.GRPGBlocks.AIRDROP.getDefaultState());
                ((TileEntityAirdrop) world.getTileEntity(pos)).generateLoot();
                this.setDead();
            }
        }
        this.move(MoverType.SELF, motionX, motionY, motionZ);
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
    }
}