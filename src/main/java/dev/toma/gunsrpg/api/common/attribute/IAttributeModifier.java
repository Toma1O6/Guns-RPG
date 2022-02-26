package dev.toma.gunsrpg.api.common.attribute;

import java.util.UUID;

public interface IAttributeModifier {

    UUID getUid();

    IModifierOp getOperation();

    double getModifierValue();

    IModifierSerializer<?> getSerializer();

    IAttributeModifier instance();
}
