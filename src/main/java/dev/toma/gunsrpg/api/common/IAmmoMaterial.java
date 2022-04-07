package dev.toma.gunsrpg.api.common;

import dev.toma.gunsrpg.common.entity.projectile.TracerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IAmmoMaterial {

    ResourceLocation getMaterialID();

    ITextComponent getDisplayName();

    int getTextColor();

    int defaultLevelRequirement();

    TracerInfo getTracer();

    void onImpact(ProjectileEntity projectile, World world, Vector3d impactPos, @Nullable Entity impactedOn);
}
