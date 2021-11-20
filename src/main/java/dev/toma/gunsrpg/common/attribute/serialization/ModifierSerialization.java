package dev.toma.gunsrpg.common.attribute.serialization;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.attribute.AttributeModifier;
import dev.toma.gunsrpg.common.attribute.AttributeOps;
import dev.toma.gunsrpg.common.attribute.IModifierOp;
import dev.toma.gunsrpg.common.attribute.TemporaryModifier;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ModifierSerialization {

    private static final Map<ResourceLocation, IModifierSeralizer<?>> SERALIZER_MAP = new HashMap<>();

    public static final IModifierSeralizer<AttributeModifier> DEFAULT = registerSerializer(new DefaultModifierSerializer());
    public static final IModifierSeralizer<TemporaryModifier> TEMPORARY = registerSerializer(new TempModifierSerializer());

    public static <S extends IModifierSeralizer<?>> S registerSerializer(S serializer) {
        ModUtils.noDupInsert(SERALIZER_MAP, serializer, IModifierSeralizer::getSerizalizerUid);
        return serializer;
    }

    private static final class DefaultModifierSerializer implements IModifierSeralizer<AttributeModifier> {

        private static final ResourceLocation UID = GunsRPG.makeResource("attribute");

        @Override
        public ResourceLocation getSerizalizerUid() {
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

    private static final class TempModifierSerializer implements IModifierSeralizer<TemporaryModifier> {

        private static final ResourceLocation UID = GunsRPG.makeResource("attribute_temporary");

        @Override
        public ResourceLocation getSerizalizerUid() {
            return UID;
        }

        @Override
        public void toNbtStructure(TemporaryModifier modifier, CompoundNBT data) {
            data.putUUID("uid", modifier.getUid());
            data.putString("op", modifier.getOperation().getId().toString());
            data.putDouble("value", modifier.getModifierValue());
            data.putInt("ticks", modifier.getTicksLeft());
        }

        @Override
        public TemporaryModifier fromNbtStructure(CompoundNBT data) {
            UUID uuid = data.getUUID("uid");
            ResourceLocation opKey = new ResourceLocation(data.getString("op"));
            double value = data.getDouble("value");
            IModifierOp op = AttributeOps.find(opKey);
            int ticks = data.getInt("ticks");
            return new TemporaryModifier(uuid, op, value, ticks);
        }
    }
}
