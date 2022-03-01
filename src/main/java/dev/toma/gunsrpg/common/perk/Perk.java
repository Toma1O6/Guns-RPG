package dev.toma.gunsrpg.common.perk;

import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import dev.toma.gunsrpg.api.common.perk.IPerk;
import dev.toma.gunsrpg.api.common.perk.IPerkOptions;
import net.minecraft.util.ResourceLocation;

public final class Perk {

    private ResourceLocation id;
    private final IAttributeId attributeId;
    private final PerkValueSpec scaleSpec;
    private final PerkValueSpec boundSpec;
    private final boolean invertCalculation;

    public Perk(IAttributeId attributeId, PerkValueSpec scaleSpec, PerkValueSpec boundSpec, boolean invertCalculation) {
        this.attributeId = attributeId;
        this.scaleSpec = scaleSpec;
        this.boundSpec = boundSpec;
        this.invertCalculation = invertCalculation;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation getPerkId() {
        return id;
    }

    public IPerk createInstance(IPerkOptions options) {
        return new PerkImpl(this, options.getType(), options.getPerkLevel());
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

    public boolean shouldInvertCalculation() {
        return invertCalculation;
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
