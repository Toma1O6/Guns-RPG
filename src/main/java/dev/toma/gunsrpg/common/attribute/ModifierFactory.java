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
        float effectivity = provider.getAttribute(Attribs.HEMOSTAT_EFFECTIVENESS).floatValue();
        return new IAttributeTarget[] {
                AttributeTarget.create(createExpiringModifier(provider, Constants.ModifierIds.BLEED_BLOCKING, AttributeOps.SUM, 1.0, Attribs.HEMOSTAT_EFFECT), Attribs.BLEED_BLOCK),
                AttributeTarget.create(createExpiringModifier(provider, Constants.ModifierIds.BLEED_DELAYING, AttributeOps.MULB, effectivity, Attribs.HEMOSTAT_EFFECT), Attribs.BLEED_DELAY)
        };
    }

    public static IAttributeTarget[] createVitaminModifiers(IAttributeProvider provider) {
        float effectivity = provider.getAttribute(Attribs.VITAMINS_EFFECTIVENESS).floatValue();
        return new IAttributeTarget[] {
                AttributeTarget.create(createExpiringModifier(provider, Constants.ModifierIds.INFECTION_BLOCKING, AttributeOps.SUM, 1.0, Attribs.VITAMINS_EFFECT), Attribs.INFECTION_BLOCK),
                AttributeTarget.create(createExpiringModifier(provider, Constants.ModifierIds.INFECTION_DELAYING, AttributeOps.MULB, effectivity, Attribs.VITAMINS_EFFECT), Attribs.INFECTION_DELAY)
        };
    }

    public static IAttributeTarget[] createPropitalModifiers(IAttributeProvider provider) {
        float effectivity = provider.getAttribute(Attribs.PROPITAL_EFFECTIVENESS).floatValue();
        return new IAttributeTarget[] {
                AttributeTarget.create(createExpiringModifier(provider, Constants.ModifierIds.FRACTURE_BLOCKING, AttributeOps.SUM, 1.0, Attribs.PROPITAL_EFFECT), Attribs.FRACTURE_BLOCK),
                AttributeTarget.create(createExpiringModifier(provider, Constants.ModifierIds.FRACTURE_DELAYING, AttributeOps.MULB, effectivity, Attribs.PROPITAL_EFFECT), Attribs.FRACTURE_DELAY)
        };
    }

    public static IAttributeTarget[] createCalciumShotModifiers(IAttributeProvider provider) {
        float effectivity = provider.getAttribute(Attribs.CALCIUM_SHOT_EFFECTIVENES).floatValue();
        return new IAttributeTarget[] {
                AttributeTarget.create(createExpiringModifier(provider, Constants.ModifierIds.POISON_BLOCKING, AttributeOps.SUM, 1.0, Attribs.CALCIUM_SHOT_EFFECT), Attribs.POISON_BLOCK),
                AttributeTarget.create(createExpiringModifier(provider, Constants.ModifierIds.POISON_DELAYING, AttributeOps.MULB, effectivity, Attribs.CALCIUM_SHOT_EFFECT), Attribs.POISON_DELAY)
        };
    }

    private ModifierFactory() {}
}
