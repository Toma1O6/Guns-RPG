package dev.toma.gunsrpg.common.attribute;

import java.util.UUID;

public interface IAttributeModifier {

    UUID getUid();

    IModifierOp getOperation();

    double getModifierValue();
}
