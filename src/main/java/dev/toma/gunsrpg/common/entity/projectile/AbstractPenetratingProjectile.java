package dev.toma.gunsrpg.common.entity.projectile;

import dev.toma.gunsrpg.util.properties.Properties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public abstract class AbstractPenetratingProjectile extends AbstractProjectile {

    public AbstractPenetratingProjectile(EntityType<? extends AbstractPenetratingProjectile> type, World level) {
        super(type, level);
    }

    public AbstractPenetratingProjectile(EntityType<? extends AbstractPenetratingProjectile> type, World world, LivingEntity owner) {
        super(type, world, owner);
    }

    protected abstract void handleEntityCollision(EntityRayTraceResult result);

    public void setPenetrationData(@Nullable PenetrationData data) {
        propertyContext.setProperty(Properties.PENETRATION, data);
    }

    @Override
    protected final void onHitEntity(EntityRayTraceResult result) {
        handleEntityCollision(result);
        PenetrationData penetrationData = this.getProperty(Properties.PENETRATION);
        if (penetrationData != null) {
            boolean noEnergy = penetrationData.collide(result.getEntity());
            mulDamage(penetrationData.getMultiplier());
            if (noEnergy) {
                remove();
            }
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        PenetrationData penetrationData = this.getProperty(Properties.PENETRATION);
        return super.canHitEntity(entity) && (penetrationData == null || penetrationData.getLastHit() != entity);
    }
}
