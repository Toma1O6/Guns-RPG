package dev.toma.gunsrpg.common.attribute;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Collection;
import java.util.UUID;

public interface IAttribute extends INBTSerializable<CompoundNBT> {

    double getModifiedValue(double value);

    double value();

    float floatValue();

    int intValue();

    void setValue(double value);

    double getBaseValue();

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
