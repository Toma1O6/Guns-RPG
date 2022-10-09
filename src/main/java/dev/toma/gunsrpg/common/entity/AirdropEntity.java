package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.ModBlocks;
import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.common.tileentity.AirdropTileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class AirdropEntity extends Entity {

    public AirdropEntity(World world) {
        this(ModEntities.AIRDROP.get(), world);
    }

    public AirdropEntity(EntityType<?> type, World world) {
        super(type, world);
        noCulling = true;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void tick() {
        BlockPos pos = blockPosition();
        if (!onGround) {
            if (level.getBlockState(pos).getMaterial().isLiquid()) {
                onLanding(pos);
            }
            this.setDeltaMovement(0, -0.10, 0);
        } else {
            if (!level.isClientSide) {
                if (!level.isEmptyBlock(pos)) {
                    pos = pos.above();
                }
                onLanding(pos);
            }
        }
        this.move(MoverType.SELF, getDeltaMovement());
    }

    private void onLanding(BlockPos landingPos) {
        level.setBlock(landingPos, ModBlocks.AIRDROP.defaultBlockState(), 3);
        TileEntity entity = level.getBlockEntity(landingPos);
        remove();
        if (!(entity instanceof AirdropTileEntity)) {
            GunsRPG.log.error("Unexpected block entity type at {} from {}", landingPos, this);
            return;
        }
        ((AirdropTileEntity) entity).generateLoot();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT p_213281_1_) {

    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT p_70037_1_) {

    }
}
