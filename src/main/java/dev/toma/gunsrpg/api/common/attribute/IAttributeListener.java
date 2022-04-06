package dev.toma.gunsrpg.api.common.attribute;

import java.util.UUID;

public interface IAttributeListener {

    void onModifierAdded(IAttributeModifier modifier);

    void onModifierRemoved(IAttributeModifier modifier);

    void onValueChanged(double value);
}
