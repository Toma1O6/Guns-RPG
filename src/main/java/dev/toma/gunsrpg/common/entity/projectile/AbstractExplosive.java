package dev.toma.gunsrpg.common.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public abstract class AbstractExplosive extends Bullet {

    public AbstractExplosive(EntityType<? extends AbstractExplosive> type, World world) {
        super(type, world);
        noPhysics = false;
    }

    public AbstractExplosive(EntityType<? extends AbstractExplosive> type, World world, LivingEntity entity) {
        super(type, world, entity);
        noPhysics = false;
    }

    public abstract void onCollided(Vector3d impact);

    @Override
    protected void onHitBlock(BlockRayTraceResult result) {
        onCollided(result.getLocation());
    }

    @Override
    protected void handleEntityCollision(EntityRayTraceResult result) {
        onCollided(result.getLocation());
    }

    @Override
    protected void onDamageChanged() {
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }
}
