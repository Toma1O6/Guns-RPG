package dev.toma.gunsrpg.common.attribute;

import net.minecraft.util.ResourceLocation;

public interface IAttributeId {

    ResourceLocation getId();

    IAttribute createNewInstance();

    double getBaseValue();

    String getDisplayTag();

    boolean hasDisplayTag();

    IValueFormatter getFormatter();
}
