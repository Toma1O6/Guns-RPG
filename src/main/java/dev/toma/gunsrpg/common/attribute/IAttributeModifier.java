package dev.toma.gunsrpg.common.attribute;

import java.util.UUID;

public interface IAttributeModifier extends IModifierProvider {

    UUID getUid();

    IModifierOp getOperation();

    double getModifierValue();

    @Override
    default IAttributeModifier getModifier() {
        return this;
    }
}
