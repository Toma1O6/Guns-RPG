package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.common.init.GRPGBlocks;
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
        ignoreFrustumCheck = true;
    }

    @Override
    public void onUpdate() {
        if(!onGround) {
            if(world.getBlockState(this.getPosition()).getMaterial().isLiquid()) {
                BlockPos pos = this.getPosition();
                world.setBlockState(pos, GRPGBlocks.AIRDROP.getDefaultState());
                ((TileEntityAirdrop) world.getTileEntity(pos)).generateLoot();
                this.setDead();
            }
            this.motionY = -0.075F;
        } else {
            if(!world.isRemote) {
                BlockPos pos = this.getPosition();
                world.setBlockState(pos, GRPGBlocks.AIRDROP.getDefaultState());
                ((TileEntityAirdrop) world.getTileEntity(pos)).generateLoot();
                this.setDead();
            }
        }
        this.move(MoverType.SELF, motionX, motionY, motionZ);
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return true;
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
