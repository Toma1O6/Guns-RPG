package dev.toma.gunsrpg.api.common.attribute;

public interface IAttributeListener {

    void onModifierAdded(IAttributeModifier modifier);

    void onModifierRemoved(IAttributeModifier modifier);

    void onValueChanged(double value);
}
