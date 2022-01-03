package dev.toma.gunsrpg.common.attribute;

import dev.toma.gunsrpg.api.common.attribute.IAttributeTarget;

public final class AttributeTarget implements IAttributeTarget {

    private final IAttributeModifier modifier;
    private final IAttributeId[] attributes;

    private AttributeTarget(IAttributeModifier modifier, IAttributeId[] attributes) {
        this.modifier = modifier;
        this.attributes = attributes;
    }

    public static IAttributeTarget create(IAttributeModifier modifier, IAttributeId... ids) {
        return new AttributeTarget(modifier, ids);
    }

    @Override
    public IAttributeModifier getModifier() {
        return modifier;
    }

    @Override
    public IAttributeId[] getTargetAttributes() {
        return attributes;
    }
}
