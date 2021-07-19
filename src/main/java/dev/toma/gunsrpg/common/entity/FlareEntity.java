package dev.toma.gunsrpg.common.entity;

import dev.toma.gunsrpg.common.init.ModEntities;
import dev.toma.gunsrpg.common.init.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class FlareEntity extends Entity implements IEntityAdditionalSpawnData {

    private int startHeight;
    private int timeWaiting;

    public FlareEntity(EntityType<? extends FlareEntity> type, World world) {
        super(type, world);
        setInvisible(true);
    }

    public FlareEntity(World world, PlayerEntity owner) {
        this(ModEntities.FLARE.get(), world);
        setPos(owner.getX(), owner.getY(), owner.getZ());
        startHeight = (int) getY();
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeInt(startHeight);
        buffer.writeInt(timeWaiting);
    }

    @Override
    public void readSpawnData(PacketBuffer buffer) {
        startHeight = buffer.readInt();
        timeWaiting = buffer.readInt();
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        nbt.putInt("startHeight", startHeight);
        nbt.putInt("timeWaiting", timeWaiting);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        startHeight = nbt.getInt("startHeight");
        timeWaiting = nbt.getInt("timeWaiting");
    }

    @Override
    public void tick() {
        super.tick();
        Vector3d start = position();
        Vector3d end = start.add(getDeltaMovement());
        BlockRayTraceResult traceResult = level.clip(new RayTraceContext(start, end, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, null));
        if (traceResult != null && traceResult.getType() != RayTraceResult.Type.MISS) {
            remove();
        }
        if (level.isClientSide) {
            level.addParticle(ParticleTypes.CLOUD, true, getX(), getY(), getZ(), 0, -0.25, 0);
            level.addParticle(ParticleTypes.CLOUD, true, getX(), getY(), getZ(), 0, -0.15, 0);
            level.addParticle(ParticleTypes.CLOUD, true, getX(), getY(), getZ(), 0, -0.05, 0);
        }
        Vector3d motion = getDeltaMovement();
        boolean reachedHeight = getY() >= startHeight + 125;
        //if (motion.y > 0) setDeltaMovement(motion.x, motion.y + 0.025, motion.z);
        if (!reachedHeight) {
            setDeltaMovement(motion.x, 0.55, motion.z);
        } else {
            setDeltaMovement(0, 0, 0);
            if (++timeWaiting >= 100) {
                if (!level.isClientSide) {
                    remove();
                    AirdropEntity airdrop = new AirdropEntity(level);
                    airdrop.setPos(getX(), getY() - 10, getZ());
                    level.addFreshEntity(airdrop);
                    level.playSound(null, getX(), getY() - 130, getZ(), ModSounds.PLANE_FLY_BY, SoundCategory.MASTER, 7.0F, 1.0F);
                }
            }
        }
        move(MoverType.SELF, getDeltaMovement());
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double p_70112_1_) {
        return true;
    }
}
