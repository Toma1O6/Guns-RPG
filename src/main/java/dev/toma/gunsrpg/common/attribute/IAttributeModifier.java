package dev.toma.gunsrpg.common.attribute;

import dev.toma.gunsrpg.common.attribute.serialization.IModifierSeralizer;

import java.util.UUID;

public interface IAttributeModifier extends IModifierProvider {

    UUID getUid();

    IModifierOp getOperation();

    double getModifierValue();

    IModifierSeralizer<?> getSerizalizer();

    @Override
    default IAttributeModifier getModifier() {
        return this;
    }
}
