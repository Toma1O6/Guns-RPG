package dev.toma.gunsrpg.common.attribute;

import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import dev.toma.gunsrpg.api.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.api.common.attribute.IAttributeTarget;

public final class AttributeTarget implements IAttributeTarget {

    private final IAttributeModifier modifier;
    private final IAttributeId attribute;

    private AttributeTarget(IAttributeModifier modifier, IAttributeId attribute) {
        this.modifier = modifier;
        this.attribute = attribute;
    }

    public static IAttributeTarget create(IAttributeModifier modifier, IAttributeId id) {
        return new AttributeTarget(modifier, id);
    }

    public static IAttributeTarget[] createMany(IAttributeModifier modifier, IAttributeId... ids) {
        IAttributeTarget[] targets = new IAttributeTarget[ids.length];
        int index = 0;
        for (IAttributeId attributeId : ids) {
            targets[index++] = create(modifier, attributeId);
        }
        return targets;
    }

    @Override
    public IAttributeModifier getModifier() {
        return modifier;
    }

    @Override
    public IAttributeId getTargetAttribute() {
        return attribute;
    }
}
