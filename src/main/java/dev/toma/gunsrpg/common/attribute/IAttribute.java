package dev.toma.gunsrpg.common.attribute;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Collection;
import java.util.UUID;

public interface IAttribute extends INBTSerializable<CompoundNBT> {

    double getModifier();

    double value();

    float floatValue();

    int intValue();

    void setValue(double value);

    double getBaseValue();

    void setBaseValue(double value);

    void tickAttributes();

    void addModifier(IModifierProvider provider);

    void removeModifier(IModifierProvider provider);

    void removeModifierById(UUID modifierId);

    void removeAllModifiers();

    IAttributeModifier getModifier(IModifierProvider provider);

    Collection<IAttributeModifier> listModifiers();

    void markChanged();

    void addAttributeListener(IAttributeListener listener);

    void removeListener(IAttributeListener listener);

    IAttributeId getId();
}
