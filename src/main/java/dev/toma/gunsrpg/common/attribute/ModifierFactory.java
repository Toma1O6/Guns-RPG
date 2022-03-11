package dev.toma.gunsrpg.common.attribute;

import dev.toma.gunsrpg.api.common.attribute.*;
import dev.toma.gunsrpg.util.Constants;

import java.util.UUID;

public final class ModifierFactory {

    public static IAttributeModifier createExpiringModifier(IAttributeProvider provider, UUID uuid, IModifierOp op, double value, IAttributeId timeAttribute) {
        IAttribute attribute = provider.getAttribute(timeAttribute);
        int time = attribute.intValue();
        return new ExpiringModifier(uuid, op, value, time);
    }

    public static IAttributeTarget[] createHemostatModifiers(IAttributeProvider provider) {
        return new IAttributeTarget[] {
                AttributeTarget.create(createExpiringModifier(provider, Constants.ModifierIds.BLEED_BLOCKING, AttributeOps.SUM, 1.0, Attribs.HEMOSTAT_EFFECT), Attribs.BLEED_BLOCK),
                AttributeTarget.create(createExpiringModifier(provider, Constants.ModifierIds.BLEED_DELAYING, AttributeOps.MULB, 0.5, Attribs.HEMOSTAT_EFFECT), Attribs.BLEED_DELAY)
        };
    }

    public static IAttributeTarget[] createVitaminModifiers(IAttributeProvider provider) {
        return new IAttributeTarget[] {
                AttributeTarget.create(createExpiringModifier(provider, Constants.ModifierIds.INFECTION_BLOCKING, AttributeOps.SUM, 1.0, Attribs.VITAMINS_EFFECT), Attribs.INFECTION_BLOCK),
                AttributeTarget.create(createExpiringModifier(provider, Constants.ModifierIds.INFECTION_DELAYING, AttributeOps.MULB, 0.5, Attribs.VITAMINS_EFFECT), Attribs.INFECTION_DELAY)
        };
    }

    public static IAttributeTarget[] createPropitalModifiers(IAttributeProvider provider) {
        return new IAttributeTarget[] {
                AttributeTarget.create(createExpiringModifier(provider, Constants.ModifierIds.FRACTURE_BLOCKING, AttributeOps.SUM, 1.0, Attribs.PROPITAL_EFFECT), Attribs.FRACTURE_BLOCK),
                AttributeTarget.create(createExpiringModifier(provider, Constants.ModifierIds.FRACTURE_DELAYING, AttributeOps.MULB, 0.5, Attribs.PROPITAL_EFFECT), Attribs.FRACTURE_DELAY)
        };
    }

    public static IAttributeTarget[] createCalciumShotModifiers(IAttributeProvider provider) {
        return new IAttributeTarget[] {
                AttributeTarget.create(createExpiringModifier(provider, Constants.ModifierIds.POISON_BLOCKING, AttributeOps.SUM, 1.0, Attribs.CALCIUM_SHOT_EFFECT), Attribs.POISON_BLOCK),
                AttributeTarget.create(createExpiringModifier(provider, Constants.ModifierIds.POISON_DELAYING, AttributeOps.MULB, 0.5, Attribs.CALCIUM_SHOT_EFFECT), Attribs.POISON_DELAY)
        };
    }

    private ModifierFactory() {}
}
