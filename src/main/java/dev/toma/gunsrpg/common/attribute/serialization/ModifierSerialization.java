package dev.toma.gunsrpg.common.attribute.serialization;

import dev.toma.gunsrpg.api.common.attribute.IAttributeModifier;
import dev.toma.gunsrpg.api.common.attribute.IModifierSerializer;
import dev.toma.gunsrpg.common.attribute.AttributeModifier;
import dev.toma.gunsrpg.common.attribute.ExpiringModifier;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class ModifierSerialization {

    private static final Map<ResourceLocation, IModifierSerializer<?>> SERALIZER_MAP = new HashMap<>();

    public static final IModifierSerializer<AttributeModifier> DEFAULT = registerSerializer(new DefaultModifierSerializer());
    public static final IModifierSerializer<ExpiringModifier> EXPIRING = registerSerializer(new ExpiringModifierSerializer());

    @SuppressWarnings("unchecked")
    public static <M extends IAttributeModifier, S extends IModifierSerializer<M>> S getSerializer(ResourceLocation uid) {
        return (S) SERALIZER_MAP.get(uid);
    }

    public static <S extends IModifierSerializer<?>> S registerSerializer(S serializer) {
        ModUtils.noDupInsert(SERALIZER_MAP, serializer, IModifierSerializer::getSerializerUid);
        return serializer;
    }

}
