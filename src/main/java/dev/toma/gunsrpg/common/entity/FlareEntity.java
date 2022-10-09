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
    private boolean playerThrown;

    public FlareEntity(EntityType<? extends FlareEntity> type, World world) {
        super(type, world);
        setInvisible(true);
    }

    public FlareEntity(World world, PlayerEntity owner) {
        this(ModEntities.FLARE.get(), world);
        setPos(owner.getX(), owner.getY(), owner.getZ());
        startHeight = (int) getY();
    }

    public void setThrownByPlayer(PlayerEntity player) {
        this.playerThrown = true;
        float power = 1.5f;
        setPos(getX(), getY() + player.getEyeHeight(), getZ());
        Vector3d look = Vector3d.directionFromRotation(player.xRot, player.yRot).scale(power);
        this.setDeltaMovement(look.x, look.y, look.z);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeInt(startHeight);
        buffer.writeInt(timeWaiting);
        buffer.writeBoolean(playerThrown);
    }

    @Override
    public void readSpawnData(PacketBuffer buffer) {
        startHeight = buffer.readInt();
        timeWaiting = buffer.readInt();
        playerThrown = buffer.readBoolean();
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        nbt.putInt("startHeight", startHeight);
        nbt.putInt("timeWaiting", timeWaiting);
        nbt.putBoolean("byPlayer", playerThrown);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT nbt) {
        startHeight = nbt.getInt("startHeight");
        timeWaiting = nbt.getInt("timeWaiting");
        playerThrown = nbt.getBoolean("byPlayer");
    }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide) {
            level.addParticle(ParticleTypes.CLOUD, true, getX(), getY() + 0.2, getZ(), 0, 0.015, 0);
            level.addParticle(ParticleTypes.CLOUD, true, getX(), getY(), getZ() + 0.2, 0, 0.012, 0);
            level.addParticle(ParticleTypes.CLOUD, true, getX(), getY() + 0.2, getZ(), 0, 0.01, 0);
        }
        if (tickCount > 1000) {
            playerThrown = true;
        }
        if (playerThrown) {
            if (++timeWaiting >= 400) {
                summonAirdrop(100, 0);
            }
            Vector3d delta = this.getDeltaMovement();
            double drag = onGround ? 0.8 : 0.99;
            Vector3d nextTickPhysics = delta.multiply(drag, 1.0, drag).add(0, -0.05, 0);
            this.setDeltaMovement(nextTickPhysics);
        } else {
            Vector3d motion = getDeltaMovement();
            boolean reachedHeight = getY() >= startHeight + 125;
            if (!reachedHeight) {
                setDeltaMovement(motion.x, 0.55, motion.z);
            } else {
                setDeltaMovement(0, 0, 0);
                if (++timeWaiting >= 100) {
                    summonAirdrop(-10, 130);
                }
            }
        }
        move(MoverType.SELF, getDeltaMovement());
    }

    public void summonAirdrop(int heightDiff, int soundDiff) {
        if (!level.isClientSide) {
            AirdropEntity entity = new AirdropEntity(level);
            entity.setPos(this.getX(), this.getY() + heightDiff, this.getZ());
            level.addFreshEntity(entity);
            level.playSound(null, getX(), getY() - soundDiff, getZ(), ModSounds.PLANE_FLY_BY, SoundCategory.MASTER, 7.0F, 1.0F);
            remove();
        }
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double p_70112_1_) {
        return true;
    }
}
