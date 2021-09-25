package dev.toma.gunsrpg.common.entity.projectile;

import dev.toma.gunsrpg.common.attribute.IAttributeProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;

public interface IProjectileConfig {

    LivingEntity getOwner();

    EnumProjectileType getRenderType();

    ProjectileSettings getSettings();

    void assignControlObj(Projectile projectile);

    void hitBlock(BlockRayTraceResult result);

    void hitEntity(EntityRayTraceResult result);

    boolean allowBaseTick();

    void tickPre();

    void tickPost();

    void tickSettings(ProjectileSettings settings);

    void updateDirection();

    IPenetrationConfig getPenetrationConfig();

    void hurtTarget(Entity owner, Entity target, float damage);

    /* Attribute properties */

    double getNoiseMultiplier(IAttributeProvider provider);

    double getHeadshotMultiplier(IAttributeProvider provider);
}
