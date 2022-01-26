package dev.toma.gunsrpg.common.attribute;

import dev.toma.gunsrpg.common.attribute.serialization.IModifierSerializer;

import java.util.UUID;

public interface IAttributeModifier {

    UUID getUid();

    IModifierOp getOperation();

    double getModifierValue();

    IModifierSerializer<?> getSerializer();

    IAttributeModifier copy();
}
