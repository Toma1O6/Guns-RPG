package dev.toma.gunsrpg.resource.perks;

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
}
