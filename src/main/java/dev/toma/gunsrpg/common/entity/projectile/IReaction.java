package dev.toma.gunsrpg.common.entity.projectile;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IReaction {

    void react(AbstractProjectile projectile, Vector3d impact, World world);

    default void writeInitialData(AbstractProjectile projectile, IAmmoMaterial material, @Nullable LivingEntity owner) {}
}
