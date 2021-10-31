package dev.toma.gunsrpg.common.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public interface IProjectileEjector {
    AbstractProjectile createProjectile(EntityType<? extends AbstractProjectile> type, World level, LivingEntity source);
}
