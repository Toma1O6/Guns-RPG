package dev.toma.gunsrpg.common.item.guns.ammo;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ReactiveMaterial extends SimpleMaterial {

    private final IImpactCallback callback;

    ReactiveMaterial(ResourceLocation materialID, int textColor, int defaultLevelRequirement, IImpactCallback callback) {
        super(materialID, textColor, defaultLevelRequirement);
        this.callback = callback;
    }

    @Override
    public void onImpact(ProjectileEntity projectile, World world, Vector3d impactPos, @Nullable Entity impactedOn) {
        callback.call(projectile, world, impactPos, impactedOn);
    }

    @FunctionalInterface
    public interface IImpactCallback {
        void call(ProjectileEntity projectile, World world, Vector3d impactPos, @Nullable Entity impactedOn);
    }
}
