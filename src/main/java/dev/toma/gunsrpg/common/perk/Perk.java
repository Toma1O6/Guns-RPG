package dev.toma.gunsrpg.common.perk;

import dev.toma.gunsrpg.api.common.attribute.IAttributeId;

public final class Perk {

    private final IAttributeId attributeId;
    private final PerkValueSpec scaleSpec;
    private final PerkValueSpec boundSpec;

    public Perk(IAttributeId attributeId, PerkValueSpec scaleSpec, PerkValueSpec boundSpec) {
        this.attributeId = attributeId;
        this.scaleSpec = scaleSpec;
        this.boundSpec = boundSpec;
    }

    public IAttributeId getAttributeId() {
        return attributeId;
    }

    public float getScaling(PerkType type) {
        return type.getValue(scaleSpec);
    }

    public float getBounds(PerkType type) {
        return type.getValue(boundSpec);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Perk perk = (Perk) o;
        return attributeId.equals(perk.attributeId);
    }

    @Override
    public int hashCode() {
        return attributeId.hashCode();
    }
}
