package dev.toma.gunsrpg.common.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

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
    protected void onHitBlock(BlockRayTraceResult result) {}

    @Override
    protected void handleEntityCollision(EntityRayTraceResult result) {}

    @Override
    protected float getPenetrationDamageMultiplier() {
        return 1;
    }
}
