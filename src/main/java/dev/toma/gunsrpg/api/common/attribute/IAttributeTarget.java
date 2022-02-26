package dev.toma.gunsrpg.api.common.attribute;

public interface IAttributeTarget {

    IAttributeModifier getModifier();

    IAttributeId getTargetAttribute();
}
