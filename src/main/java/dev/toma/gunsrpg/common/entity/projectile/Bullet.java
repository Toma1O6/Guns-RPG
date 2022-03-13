package dev.toma.gunsrpg.common.entity.projectile;

import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.play.server.SSpawnParticlePacket;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class Bullet extends AbstractPenetratingProjectile {

    public Bullet(EntityType<? extends Bullet> type, World world) {
        super(type, world);
    }

    public Bullet(EntityType<? extends Bullet> type, World world, LivingEntity owner) {
        super(type, world, owner);
    }

    @Override
    public void preTick() {}

    @Override
    public void postTick() {}

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return false;
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult result) {
        if (!level.isClientSide) {
            BlockPos pos = result.getBlockPos();
            BlockState state = level.getBlockState(pos);
            if (state.getMaterial() == Material.GLASS) {
                level.destroyBlock(pos, false);
                Vector3d projectilePos = position();
                checkForCollisions(projectilePos, projectilePos.add(this.getDeltaMovement()));
                return;
            }
            Vector3d vec = result.getLocation();
            Vector3i normal = result.getDirection().getNormal();
            float dirX = normal.getX() / 5.0f;
            float dirY = normal.getX() / 5.0f;
            float dirZ = normal.getX() / 5.0f;
            SoundType type = state.getSoundType();
            level.playSound(null, vec.x, vec.y, vec.z, type.getBreakSound(), SoundCategory.MASTER, type.getVolume(), type.getPitch());
            SSpawnParticlePacket packet = new SSpawnParticlePacket(new BlockParticleData(ParticleTypes.BLOCK, state), true, vec.x, vec.y, vec.z, dirX, dirY, dirZ, 0.01F, 3);
            ((ServerWorld) level).players().forEach(player -> player.connection.send(packet));
            state.onProjectileHit(level, state, result, this);
            remove();
        }
    }

    @Override
    protected void handleEntityCollision(EntityRayTraceResult result) {
        if (!level.isClientSide) {
            Entity entity = result.getEntity();
            this.hurtTarget(entity, this.getOwner(), false); // TODO headshots
            entity.invulnerableTime = 0;
            remove(); // TODO pass if penetration data allow it
        }
    }
}
