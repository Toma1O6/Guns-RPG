package dev.toma.gunsrpg.common.attribute.serialization;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.attribute.AttributeOps;
import dev.toma.gunsrpg.common.attribute.ExpiringModifier;
import dev.toma.gunsrpg.common.attribute.IModifierOp;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.UUID;

public class ExpiringModifierSerializer implements IModifierSerializer<ExpiringModifier> {

    private static final ResourceLocation UID = GunsRPG.makeResource("expiring");

    @Override
    public ResourceLocation getSerializerUid() {
        return UID;
    }

    @Override
    public void toNbtStructure(ExpiringModifier modifier, CompoundNBT data) {
        data.putUUID("uid", modifier.getUid());
        data.putString("op", modifier.getOperation().getId().toString());
        data.putDouble("value", modifier.getModifierValue());
        data.putInt("initialTime", modifier.getInitialTime());
        data.putInt("timeLeft", modifier.getTimeLeft());
    }

    @Override
    public ExpiringModifier fromNbtStructure(CompoundNBT data) {
        UUID uuid = data.getUUID("uid");
        ResourceLocation opKey = new ResourceLocation(data.getString("op"));
        double value = data.getDouble("value");
        IModifierOp op = AttributeOps.find(opKey);
        int time = data.getInt("initialTime");
        ExpiringModifier modifier = new ExpiringModifier(uuid, op, value, time);
        int timeLeft = data.getInt("timeLeft");
        modifier.setTimeLeft(timeLeft);
        return modifier;
    }
}
