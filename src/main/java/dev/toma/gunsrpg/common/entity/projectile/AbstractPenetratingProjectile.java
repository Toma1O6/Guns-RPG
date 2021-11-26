package dev.toma.gunsrpg.common.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

public abstract class AbstractPenetratingProjectile extends AbstractProjectile {

    private PenetrationData penetrationData;

    public AbstractPenetratingProjectile(EntityType<? extends AbstractPenetratingProjectile> type, World level) {
        super(type, level);
    }

    public AbstractPenetratingProjectile(EntityType<? extends AbstractPenetratingProjectile> type, World world, LivingEntity owner) {
        super(type, world, owner);
    }

    protected abstract void handleEntityCollision(EntityRayTraceResult result);

    protected abstract float getPenetrationDamageMultiplier();

    @Override
    public void setup(float damage, float velocity, int delay, boolean isPenetratingRound) {
        super.setup(damage, velocity, delay, isPenetratingRound);
        this.penetrationData = isPenetratingRound ? new PenetrationData() : null;
    }

    @Override
    protected final void onHitEntity(EntityRayTraceResult result) {
        handleEntityCollision(result);
        if (penetrationData != null) {
            boolean noEnergy = penetrationData.collide(result.getEntity());
            mulDamage(this.getPenetrationDamageMultiplier());
            if (noEnergy) {
                remove();
            }
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return (penetrationData != null && entity != penetrationData.getLastHit()) && super.canHitEntity(entity);
    }
}
