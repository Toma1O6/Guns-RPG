package dev.toma.gunsrpg.common.quests.adapters;

import com.google.gson.*;
import dev.toma.gunsrpg.common.quests.quest.area.*;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Type;
import java.util.List;

public final class MobSpawnerAdapter {

    public IMobSpawner deserialize(JsonElement json) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        ResourceLocation entityId = new ResourceLocation(JSONUtils.getAsString(object, "entity"));
        EntityType<? extends LivingEntity> entityType = tryParseAsLivingEntity(entityId);
        float spawnPropability = JSONUtils.getAsFloat(object, "propability");
        int spawnLimit = JSONUtils.getAsInt(object, "limit", 1);
        List<IMobSpawnProcessor> processors = JsonHelper.deserializeAsList("processors", object, this::resolveSpawnProcessor);
        return new MobSpawner(entityType, spawnPropability, spawnLimit, processors);
    }

    private <P extends IMobSpawnProcessor> P resolveSpawnProcessor(JsonElement element) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(element);
        ResourceLocation id = new ResourceLocation(JSONUtils.getAsString(object, "type"));
        MobSpawnProcessorType<P> type = MobSpawnProcessorType.findById(id);
        if (type == null) {
            throw new JsonSyntaxException("Unknown spawn processor: " + id);
        }
        IMobSpawnProcessorSerializer<P> serializer = type.getSerializer();
        return serializer.deserialize(object);
    }

    @SuppressWarnings("unchecked")
    public static EntityType<? extends LivingEntity> tryParseAsLivingEntity(ResourceLocation location) throws JsonParseException {
        EntityType<?> type = ForgeRegistries.ENTITIES.getValue(location);
        if (type == null)
            throw new JsonSyntaxException("Unknown entity: " + location);
        try {
            return (EntityType<? extends LivingEntity>) type;
        } catch (ClassCastException e) {
            throw new JsonSyntaxException("Not a living entity: " + location);
        }
    }
}
