package dev.toma.gunsrpg.common.entity.projectile;

import dev.toma.gunsrpg.util.properties.Properties;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public abstract class AbstractPenetratingProjectile extends AbstractProjectile {

    public AbstractPenetratingProjectile(EntityType<? extends AbstractPenetratingProjectile> type, World level) {
        super(type, level);
    }

    public AbstractPenetratingProjectile(EntityType<? extends AbstractPenetratingProjectile> type, World world, LivingEntity owner) {
        super(type, world, owner);
    }

    protected abstract void handleEntityCollision(EntityRayTraceResult result);

    @Override
    protected final void onHitEntity(EntityRayTraceResult result) {
        handleEntityCollision(result);
        PenetrationData penetrationData = this.getProperty(Properties.PENETRATION);
        if (penetrationData != null) {
            boolean noEnergy = penetrationData.collide(result.getEntity());
            mulDamage(penetrationData.getMultiplier());
            if (noEnergy) {
                remove();
            } else {
                Vector3d pos = this.position();
                Vector3d mov = pos.add(this.getDeltaMovement());
                checkForCollisions(pos, mov);
            }
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        PenetrationData penetrationData = this.getProperty(Properties.PENETRATION);
        return super.canHitEntity(entity) && (penetrationData == null || penetrationData.getLastHit() != entity);
    }
}
