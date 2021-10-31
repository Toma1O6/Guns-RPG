package dev.toma.gunsrpg.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

public class Bullet extends AbstractProjectile {

    private PenetrationData penetrationData;

    public Bullet(EntityType<? extends AbstractProjectile> type, World world, ProjectileSettings settings, LivingEntity owner) {
        super(type, world, settings, owner);
    }

    @Override
    public void preTick() {

    }

    @Override
    public void postTick() {

    }

    @Override
    protected void onHitBlock(BlockRayTraceResult result) {

    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result) {

    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return false;
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return (penetrationData != null && entity != penetrationData.entity) && super.canHitEntity(entity);
    }

    private static class PenetrationData {

        private Entity entity;
    }
}
