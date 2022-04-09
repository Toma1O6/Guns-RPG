package dev.toma.gunsrpg.common.item.guns.ammo;

import dev.toma.gunsrpg.api.common.IAmmoMaterial;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class SimpleMaterial implements IAmmoMaterial {

    private final ResourceLocation materialID;
    private final ITextComponent displayName;
    private final Integer tracer;
    private final int textColor;
    private final int defaultLevelRequirement;

    SimpleMaterial(ResourceLocation materialID, int textColor, int defaultLevelRequirement, Integer tracer) {
        this.materialID = materialID;
        this.textColor = textColor;
        this.tracer = tracer;
        this.defaultLevelRequirement = defaultLevelRequirement;
        this.displayName = new TranslationTextComponent(convertID(materialID));
    }

    @Override
    public ResourceLocation getMaterialID() {
        return materialID;
    }

    @Override
    public ITextComponent getDisplayName() {
        return displayName;
    }

    @Override
    public Integer getTracerColor() {
        return tracer;
    }

    @Override
    public int getTextColor() {
        return textColor;
    }

    @Override
    public int defaultLevelRequirement() {
        return defaultLevelRequirement;
    }

    @Override
    public void onImpact(ProjectileEntity projectile, World world, Vector3d impactPos, @Nullable Entity impactedOn) {
        // does nothing
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleMaterial that = (SimpleMaterial) o;
        return materialID.equals(that.materialID);
    }

    @Override
    public int hashCode() {
        return materialID.hashCode();
    }

    private String convertID(ResourceLocation location) {
        String loc = location.toString().replaceAll(":", ".");
        return "ammo.material." + loc;
    }
}
