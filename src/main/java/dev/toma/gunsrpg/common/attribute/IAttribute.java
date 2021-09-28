package dev.toma.gunsrpg.common.attribute;

import java.util.Collection;
import java.util.UUID;

public interface IAttribute {

    double value();

    float floatValue();

    int intValue();

    void setValue(double value);

    double getBaseValue();

    void setBaseValue(double value);

    void tickAttributes();

    void addModifier(IAttributeModifier modifier);

    void removeModifier(IAttributeModifier modifier);

    void removeModifierById(UUID modifierId);

    void removeAllModifiers();

    IAttributeModifier getModifier(IAttributeModifier modifier);

    Collection<IAttributeModifier> listModifiers();

    void markChanged();

    void addAttributeListener(IAttributeListener listener);

    void removeListener(IAttributeListener listener);

    IAttributeId getId();
}
