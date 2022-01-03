package dev.toma.gunsrpg.api.common.attribute;

import dev.toma.gunsrpg.common.attribute.IAttributeId;
import dev.toma.gunsrpg.common.attribute.IAttributeModifier;

public interface IAttributeTarget {

    IAttributeModifier getModifier();

    IAttributeId[] getTargetAttributes();
}
