package com.wf.firearms.entity;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.airdrop.AirdropBlockEntity;
import com.wf.firearms.registry.ModBlocks;
import com.wf.firearms.registry.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public class AirdropEntity extends Entity {
    private static final Vec3 FALL_VEC = new Vec3(0.0D, -0.35D, 0.0D);
    /** 超过约 30 秒仍未落地则强制按地表落点放置。 */
    private static final int MAX_FALL_TICKS = 600;

    public AirdropEntity(EntityType<? extends AirdropEntity> type, Level level) {
        super(type, level);
        this.noCulling = true;
        setNoGravity(true);
    }

    public AirdropEntity(Level level, double x, double y, double z) {
        this(ModEntities.AIRDROP.get(), level);
        setPos(x, y, z);
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos pos = blockPosition();
        BlockPos landing = resolveLandingPos(pos);

        if (!level().isClientSide && tickCount > MAX_FALL_TICKS) {
            GunsRpg.LOGGER.warn("[gunsrpg] 空投实体 {} 下落超时，强制落至 {}", pos, landing);
            placeOnGround(landing);
            return;
        }

        if (!level().isClientSide && pos.getY() <= landing.getY() + 0.5D) {
            placeOnGround(landing);
            return;
        }

        setDeltaMovement(FALL_VEC);
        if (verticalCollision || onGround()) {
            placeOnGround(resolveLandingPos(blockPosition()));
            return;
        }
        move(MoverType.SELF, getDeltaMovement());
        if (verticalCollision || onGround()) {
            placeOnGround(resolveLandingPos(blockPosition()));
        }
    }

    /** 从当前高度向下找可放置空投箱的顶面（避免超时落在半空）。 */
    private BlockPos resolveLandingPos(BlockPos from) {
        int x = from.getX();
        int z = from.getZ();
        int yStart = Math.min(from.getY(), level().getMaxBuildHeight() - 2);
        for (int y = yStart; y >= level().getMinBuildHeight() + 1; y--) {
            BlockPos ground = new BlockPos(x, y, z);
            BlockPos above = ground.above();
            BlockState groundState = level().getBlockState(ground);
            if (groundState.isSolidRender(level(), ground) && level().getBlockState(above).canBeReplaced()) {
                return above;
            }
        }
        int surface = level().getHeight(Heightmap.Types.MOTION_BLOCKING, x, z);
        return new BlockPos(x, surface, z);
    }

    private void placeOnGround(BlockPos landingPosition) {
        if (level().isClientSide) {
            discard();
            return;
        }
        BlockState existing = level().getBlockState(landingPosition);
        if (!existing.canBeReplaced()) {
            level().destroyBlock(landingPosition, true);
        }
        discard();
        level().setBlock(landingPosition, ModBlocks.AIRDROP.get().defaultBlockState(), 3);
        BlockEntity blockEntity = level().getBlockEntity(landingPosition);
        if (!(blockEntity instanceof AirdropBlockEntity airdrop)) {
            GunsRpg.LOGGER.error("[gunsrpg] 空投落地异常：{} 不是 AirdropBlockEntity", landingPosition);
            return;
        }
        airdrop.beginSmokePhase();
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
