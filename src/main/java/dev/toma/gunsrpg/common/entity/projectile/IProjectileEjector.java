package dev.toma.gunsrpg.common.entity.projectile;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public interface IProjectileEjector {
    Projectile createProjectile(EntityType<? extends Projectile> type, World level, LivingEntity source);
}
