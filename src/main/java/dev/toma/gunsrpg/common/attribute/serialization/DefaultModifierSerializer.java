package dev.toma.gunsrpg.common.attribute.serialization;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.attribute.AttributeModifier;
import dev.toma.gunsrpg.common.attribute.AttributeOps;
import dev.toma.gunsrpg.common.attribute.IModifierOp;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

public final class DefaultModifierSerializer implements IModifierSerializer<AttributeModifier> {

    private static final ResourceLocation UID = GunsRPG.makeResource("attribute");

    @Override
    public ResourceLocation getSerializerUid() {
        return UID;
    }

    @Override
    public void toNbtStructure(AttributeModifier modifier, CompoundNBT nbt) {
        nbt.putUUID("uid", modifier.getUid());
        nbt.putString("op", modifier.getOperation().getId().toString());
        nbt.putDouble("value", modifier.getModifierValue());
    }

    @Override
    public AttributeModifier fromNbtStructure(CompoundNBT data) {
        UUID uuid = data.getUUID("uid");
        ResourceLocation opKey = new ResourceLocation(data.getString("op"));
        double value = data.getDouble("value");
        IModifierOp op = AttributeOps.find(opKey);
        return new AttributeModifier(uuid, op, value);
    }
}
