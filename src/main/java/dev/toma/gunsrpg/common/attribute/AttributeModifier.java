package dev.toma.gunsrpg.common.attribute;

import java.util.UUID;

public class AttributeModifier implements IAttributeModifier {

    private final UUID uid;
    private final IModifierOp op;
    private final double value;

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
}
