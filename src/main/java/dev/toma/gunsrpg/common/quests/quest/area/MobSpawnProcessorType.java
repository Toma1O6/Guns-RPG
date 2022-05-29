package dev.toma.gunsrpg.common.quests.quest.area;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class MobSpawnProcessorType<P extends IMobSpawnProcessor> {

    // Store object
    private static final Map<ResourceLocation, MobSpawnProcessorType<?>> MAP = new HashMap<>();

    // Default processors
    public static final MobSpawnProcessorType<SetEntityControllerProcessor> CONTROLLER = register(GunsRPG.makeResource("controller"), new SetEntityControllerProcessor.Serializer());
    public static final MobSpawnProcessorType<SetEquipmentProcessor> EQUIPMENT = register(GunsRPG.makeResource("set_equipment"), new SetEquipmentProcessor.Serializer());
    public static final MobSpawnProcessorType<SetEffectProcessor> EFFECT = register(GunsRPG.makeResource("set_effect"), new SetEffectProcessor.Serializer());

    // Registry methods
    public static <P extends IMobSpawnProcessor> MobSpawnProcessorType<P> register(ResourceLocation id, IMobSpawnProcessorSerializer<P> serializer) {
        MobSpawnProcessorType<P> type = new MobSpawnProcessorType<>(id, serializer);
        MAP.put(id, type);
        return type;
    }

    @SuppressWarnings("unchecked")
    public static <P extends IMobSpawnProcessor> MobSpawnProcessorType<P> findById(ResourceLocation location) {
        return (MobSpawnProcessorType<P>) MAP.get(location);
    }

    // Implementation
    private final ResourceLocation id;
    private final IMobSpawnProcessorSerializer<P> serializer;

    public MobSpawnProcessorType(ResourceLocation id, IMobSpawnProcessorSerializer<P> serializer) {
        this.id = id;
        this.serializer = serializer;
    }

    public ResourceLocation getId() {
        return id;
    }

    public IMobSpawnProcessorSerializer<P> getSerializer() {
        return serializer;
    }

    public CompoundNBT toNbt(P processor) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("type", this.id.toString());
        this.serializer.toNbt(processor, nbt);
        return nbt;
    }

    public static <P extends IMobSpawnProcessor> P fromNbt(CompoundNBT nbt) {
        ResourceLocation typeId = new ResourceLocation(nbt.getString("type"));
        MobSpawnProcessorType<P> type = findById(typeId);
        return type.serializer.fromNbt(type, nbt);
    }
}
