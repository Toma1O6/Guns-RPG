package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.ModBlocks;
import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.common.tileentity.AirdropTileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.network.NetworkHooks;

public class AirdropEntity extends Entity {

    private static final Vector3d FALL_VEC = new Vector3d(0.0D, -0.1D, 0.0D);

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
        this.setDeltaMovement(FALL_VEC);
        if (this.verticalCollision) {
            placeOnGround(pos, false);
        }
        FluidState fluidState = level.getFluidState(pos);
        if (!fluidState.isEmpty()) {
            placeOnGround(pos, true);
        }
        this.move(MoverType.SELF, getDeltaMovement());
    }

    private void placeOnGround(BlockPos pos, boolean forcePlacement) {
        BlockPos landingPosition;
        if (forcePlacement) {
            landingPosition = pos;
        } else {
            int y = level.getHeight(Heightmap.Type.MOTION_BLOCKING, pos.getX(), pos.getZ());
            landingPosition = new BlockPos(pos.getX(), y, pos.getZ());
        }
        if (!level.isEmptyBlock(landingPosition)) {
            level.destroyBlock(landingPosition, true);
        }
        remove();
        level.setBlock(landingPosition, ModBlocks.AIRDROP.defaultBlockState(), 3);
        TileEntity tileEntity = level.getBlockEntity(landingPosition);
        if (!(tileEntity instanceof AirdropTileEntity)) {
            GunsRPG.log.error("Unexpected block entity type at {} from {}", landingPosition, this);
            return;
        }
        ((AirdropTileEntity) tileEntity).generateLoot();
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
