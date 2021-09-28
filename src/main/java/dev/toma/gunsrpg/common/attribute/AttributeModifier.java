package dev.toma.gunsrpg.common.attribute;

import java.util.UUID;

public class AttributeModifier implements IAttributeModifier {

    private final UUID uid;
    private final IModifierOp op;
    private final double value;

    AttributeModifier(String uid, IModifierOp op, double value) {
        this(UUID.fromString(uid), op, value);
    }

    AttributeModifier(UUID uid, IModifierOp op, double value) {
        this.uid = uid;
        this.op = op;
        this.value = value;
    }

    @Override
    public UUID getUid() {
        return uid;
    }

    @Override
    public IModifierOp getOperation() {
        return op;
    }

    @Override
    public double getModifierValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AttributeModifier that = (AttributeModifier) o;
        return uid.equals(that.uid);
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }
}
