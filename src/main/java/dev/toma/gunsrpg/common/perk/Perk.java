package dev.toma.gunsrpg.common.perk;

import dev.toma.gunsrpg.api.common.attribute.IAttributeId;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public final class Perk {

    private ResourceLocation id;
    private final IAttributeId attributeId;
    private final float scaling;
    private final PerkValueSpec boundSpec;
    private final boolean invertCalculation;
    private ITextComponent displayName;

    public Perk(IAttributeId attributeId, float scaling, PerkValueSpec boundSpec, boolean invertCalculation) {
        this.attributeId = attributeId;
        this.scaling = scaling;
        this.boundSpec = boundSpec;
        this.invertCalculation = invertCalculation;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
        this.displayName = new TranslationTextComponent("perk." + SkillUtil.Localizations.convertToLocalizationKey(id));
    }

    public ResourceLocation getPerkId() {
        return id;
    }

    public IAttributeId getAttributeId() {
        return attributeId;
    }

    public ITextComponent getDisplayName() {
        return displayName;
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

    public void encode(PacketBuffer buffer) {
        buffer.writeResourceLocation(id);
        buffer.writeResourceLocation(attributeId.getId());
        buffer.writeFloat(scaling);
        boundSpec.encode(buffer);
        buffer.writeBoolean(invertCalculation);
    }

    public static Perk decode(PacketBuffer buffer) {
        ResourceLocation id = buffer.readResourceLocation();
        ResourceLocation attrId = buffer.readResourceLocation();
        float scaling = buffer.readFloat();
        PerkValueSpec spec = PerkValueSpec.decode(buffer);
        boolean invertCalculation = buffer.readBoolean();
        IAttributeId attribute = Attribs.find(attrId);
        Perk perk = new Perk(attribute, scaling, spec, invertCalculation);
        perk.setId(id);
        return perk;
    }
}
