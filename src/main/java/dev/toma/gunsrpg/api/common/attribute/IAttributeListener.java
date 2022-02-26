package dev.toma.gunsrpg.api.common.attribute;

import java.util.UUID;

public interface IAttributeListener {

    UUID getListenerUid();

    void onModifierAdded(IAttributeModifier modifier);

    void onModifierRemoved(IAttributeModifier modifier);

    void onValueChanged(double value);
}
