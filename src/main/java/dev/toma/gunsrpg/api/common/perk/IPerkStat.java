package dev.toma.gunsrpg.api.common.perk;

import dev.toma.gunsrpg.api.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.common.item.perk.CrystalAttribute;

public interface IPerkStat {

    CrystalAttribute getAttribute();

    IAttributeModifier getModifier();
}
