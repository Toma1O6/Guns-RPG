package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.init.ModBlocks;
import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.common.tileentity.AirdropTileEntity;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class AirdropEntity extends Entity {

    private static final Vector3d FALL_VEC = new Vector3d(0.0D, -0.1D, 0.0D);

    private UUID owner;
    private SpawnSource spawnSource;

    public AirdropEntity(World world, @Nullable UUID owner, SpawnSource spawnSource) {
        this(ModEntities.AIRDROP.get(), world);
        this.owner = owner;
        this.spawnSource = spawnSource;
    }

    public AirdropEntity(EntityType<?> type, World world) {
        super(type, world);
        this.spawnSource = SpawnSource.EVENT;
        noCulling = true;
    }

    @Nullable
    public PlayerEntity getOwner() {
        return this.owner != null ? this.level.getPlayerByUUID(this.owner) : null;
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
        TileEntity tileEntity = this.level.getBlockEntity(landingPosition);
        if (!(tileEntity instanceof AirdropTileEntity)) {
            GunsRPG.log.error("Unexpected block entity type at {} from {}", landingPosition, this);
            return;
        }
        AirdropTileEntity airdrop = (AirdropTileEntity) tileEntity;
        airdrop.generateLoot();
        if (!this.level.isClientSide() && airdrop.isLockable()) {
            airdrop.generateDefaultLockCombination();
            PlayerEntity owner = this.getOwner();
            if (this.spawnSource.isPersonal() && owner != null) {
                IntList combination = airdrop.getLockConfiguration();
                owner.sendMessage(new TranslationTextComponent("gunsrpg.lock.password", combination.toString()), Util.NIL_UUID);
            }
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        nbt.putInt("spawnSource", this.spawnSource != null ? this.spawnSource.ordinal() : SpawnSource.EVENT.ordinal());
        if (this.owner != null) {
            nbt.putUUID("owner", this.owner);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        this.spawnSource = SpawnSource.values()[nbt.getInt("spawnSource")];
        this.owner = nbt.contains("owner") ? nbt.getUUID("owner") : null;
    }

    public enum SpawnSource {

        EVENT,
        PERSONAL;

        public boolean isPersonal() {
            return this == PERSONAL;
        }
    }
}
