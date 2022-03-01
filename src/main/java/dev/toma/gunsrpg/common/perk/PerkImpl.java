package dev.toma.gunsrpg.common.perk;

import dev.toma.gunsrpg.api.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.api.common.perk.IPerk;
import dev.toma.gunsrpg.common.attribute.AttributeModifier;
import dev.toma.gunsrpg.common.attribute.AttributeOps;

import java.util.UUID;

public final class PerkImpl implements IPerk {

    private final Perk perk;
    private final IAttributeModifier modifier;

    public PerkImpl(Perk perk, IAttributeModifier modifier) {
        this.perk = perk;
        this.modifier = modifier;
    }

    public PerkImpl(Perk perk, PerkType type, int level) {
        this.perk = perk;

        float scale = perk.getScaling(type);
        float bounds = perk.getBounds(type);
        float rawValue = this.calculateModifierValue(level, scale, bounds, perk.shouldInvertCalculation());
        float modifierValue = type == PerkType.BUFF ? 1.0F + rawValue : 1.0F - rawValue;
        long code = perk.hashCode();
        UUID uuid = new UUID(code * code * 31, code);
        this.modifier = new AttributeModifier(uuid, AttributeOps.MUL, modifierValue);
    }

    @Override
    public Perk getType() {
        return perk;
    }

    @Override
    public IAttributeModifier getModifier() {
        return modifier;
    }

    private float calculateModifierValue(int level, float scale, float bounds, boolean isInverted) {
        float value = Math.min(level * scale, bounds);
        if (isInverted)
            value = -value;
        return value;
    }
}
