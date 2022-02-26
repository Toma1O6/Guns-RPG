package dev.toma.gunsrpg.api.common.attribute;

import net.minecraft.util.ResourceLocation;

public interface IAttributeId {

    ResourceLocation getId();

    IAttribute createNewInstance();

    double getBaseValue();
}
