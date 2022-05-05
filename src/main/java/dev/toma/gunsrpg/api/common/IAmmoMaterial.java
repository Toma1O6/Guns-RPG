package dev.toma.gunsrpg.api.common;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public interface IAmmoMaterial {

    ResourceLocation getMaterialID();

    ITextComponent getDisplayName();

    int getTextColor();

    int defaultLevelRequirement();

    Integer getTracerColor();
}
