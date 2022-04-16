package dev.toma.gunsrpg.common.perk;

import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import net.minecraft.util.ResourceLocation;

public final class Perk {

    private ResourceLocation id;
    private final IAttributeId attributeId;
    private final float scaling;
    private final PerkValueSpec boundSpec;
    private final boolean invertCalculation;

    public Perk(IAttributeId attributeId, float scaling, PerkValueSpec boundSpec, boolean invertCalculation) {
        this.attributeId = attributeId;
        this.scaling = scaling;
        this.boundSpec = boundSpec;
        this.invertCalculation = invertCalculation;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
    }

    public ResourceLocation getPerkId() {
        return id;
    }

    public IAttributeId getAttributeId() {
        return attributeId;
    }

    public double getModifier(int level, PerkType type) {
        float scale = this.getScaling();
        float bound = this.getBounds(type);
        double value = level * scale;
        return value < 0 ? Math.max(value, -bound) : Math.min(value, bound);
    }

    public float getScaling() {
        return scaling;
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

    @Override
    public String toString() {
        return "Perk{" + "id=" + id + '}';
    }
}
