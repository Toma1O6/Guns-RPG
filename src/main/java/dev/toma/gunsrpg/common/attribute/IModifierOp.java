package dev.toma.gunsrpg.common.attribute;

import net.minecraft.util.ResourceLocation;

public interface IModifierOp {

    ResourceLocation getId();

    double combine(double baseValue, double modifierValue);

    int getPriority();
}
