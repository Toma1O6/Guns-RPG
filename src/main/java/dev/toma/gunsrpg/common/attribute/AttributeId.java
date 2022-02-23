package dev.toma.gunsrpg.common.attribute;

import net.minecraft.util.ResourceLocation;

import java.util.Objects;

public class AttributeId implements IAttributeId {

    private final ResourceLocation id;
    private final IAttribFactory factory;
    private final double baseValue;

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
