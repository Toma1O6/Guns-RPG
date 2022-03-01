package dev.toma.gunsrpg.api.common.perk;

import dev.toma.gunsrpg.api.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.common.perk.Perk;

public interface IPerk {

    Perk getType();

    IAttributeModifier getModifier();
}
