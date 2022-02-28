package dev.toma.gunsrpg.common.perk;

import dev.toma.gunsrpg.api.common.attribute.IAttributeModifier;

public interface IPerk {

    Perk getType();

    IAttributeModifier getModifier();
}
