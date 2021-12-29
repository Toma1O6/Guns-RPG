package dev.toma.gunsrpg.common.attribute;

import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class AttributeId implements IAttributeId {

    private final ResourceLocation id;
    private final IAttribFactory factory;
    private final double baseValue;
    private String displayTag;
    private IValueFormatter formatter = IValueFormatter.IDENTITY;

    private AttributeId(ResourceLocation id, IAttribFactory factory, double baseValue) {
        this.id = id;
        this.factory = factory;
        this.baseValue = baseValue;
    }

    public static AttributeId create(ResourceLocation id, double base, IAttribFactory factory) {
        return new AttributeId(
                Objects.requireNonNull(id),
                Objects.requireNonNull(factory),
                base
        );
    }

    public AttributeId display(String tag) {
        return this.display(tag, formatter);
    }

    public AttributeId display(String tag, IValueFormatter formatter) {
        this.displayTag = tag;
        this.formatter = formatter;
        return this;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public double getBaseValue() {
        return baseValue;
    }

    @Override
    public IAttribute createNewInstance() {
        return factory.constructAttribute(this);
    }

    @Override
    public String getDisplayTag() {
        return displayTag;
    }

    @Override
    public boolean hasDisplayTag() {
        return displayTag != null && !displayTag.isEmpty();
    }

    @Override
    public IValueFormatter getFormatter() {
        return formatter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeId that = (AttributeId) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public interface IAttribFactory {
        IAttribute constructAttribute(IAttributeId id);
    }
}
