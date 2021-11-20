package dev.toma.gunsrpg.common.attribute.serialization;

import dev.toma.gunsrpg.common.attribute.IAttributeModifier;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

public interface IModifierSeralizer<M extends IAttributeModifier> {

    ResourceLocation getSerizalizerUid();

    void toNbtStructure(M modifier, CompoundNBT data);

    M fromNbtStructure(CompoundNBT data);
}
