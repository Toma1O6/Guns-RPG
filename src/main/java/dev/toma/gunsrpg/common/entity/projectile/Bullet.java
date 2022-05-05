package dev.toma.gunsrpg.common.entity.projectile;

import dev.toma.gunsrpg.util.properties.Properties;
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
import net.minecraft.util.math.AxisAlignedBB;
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
    public void postTick() {
        applyGravity(0.05F);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return propertyContext.hasProperty(Properties.TRACER);
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult result) {
        if (!level.isClientSide) {
            BlockPos pos = result.getBlockPos();
            BlockState state = level.getBlockState(pos);
            if (this.canPenetrateGlass() && state.getMaterial() == Material.GLASS) {
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
            this.propertyContext.setProperty(Properties.IS_HEADSHOT, this.isHeadshot(entity));
            this.hurtTarget(entity, this.getOwner());
            entity.invulnerableTime = 0;
            PenetrationData penetrationData = this.getProperty(Properties.PENETRATION);
            if (penetrationData == null || penetrationData.getLastHit() != null) {
                remove();
            }
        }
    }

    protected boolean canPenetrateGlass() {
        return true;
    }

    protected boolean isHeadshot(Entity entity) {
        if (!canHeadshotEntity(entity)) return false;
        Vector3d vec1 = this.position();
        Vector3d vec2 = vec1.add(this.getDeltaMovement());
        AxisAlignedBB hitbox = entity.getBoundingBox();
        Vector3d impactVec = hitbox.clip(vec1, vec2).orElse(null);
        if (impactVec == null) return false;
        double headMinY = entity.getEyeY() - 0.25;
        return impactVec.y >= headMinY;
    }
}
