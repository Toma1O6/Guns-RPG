package dev.toma.gunsrpg.common.attribute;

import java.util.Collection;
import java.util.UUID;

public interface IAttribute {

    double getValue();

    int getValueAsInt();

    void setValue(double value);

    double getBaseValue();

    void setBaseValue(double value);

    void tickAttributes();

    void addModifier(IAttributeModifier modifier);

    void removeModifier(IAttributeModifier modifier);

    void removeModifierById(UUID modifierId);

    void removeAllModifiers();

    Collection<IAttributeModifier> listModifiers();

    void markChanged();

    void addAttributeListener(IAttributeListener listener);

    void removeListener(IAttributeListener listener);

    IAttributeId getId();
}
