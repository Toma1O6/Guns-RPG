package dev.toma.gunsrpg.common.quests.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.common.quests.quest.area.*;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public final class MobSpawnerAdapter {

    public IMobSpawner deserialize(JsonElement json) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        ResourceLocation entityId = new ResourceLocation(JSONUtils.getAsString(object, "entity"));
        EntityType<? extends LivingEntity> entityType = tryParseAsLivingEntity(entityId);
        int weight = JsonHelper.getAsBoundedInt(object, "weight", 1, 1, Integer.MAX_VALUE);
        int minCount = JsonHelper.getAsBoundedInt(object, "minCount", 1, 1, 64);
        int maxCount = JsonHelper.getAsBoundedInt(object, "maxCount", 1, 1, 64);
        List<IMobSpawnProcessor> processors = JsonHelper.deserializeAsList("processors", object, MobSpawnerAdapter::resolveSpawnProcessor);
        return new MobSpawner(entityType, weight, minCount, maxCount, processors);
    }

    public static <P extends IMobSpawnProcessor> P resolveSpawnProcessor(JsonElement element) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(element);
        ResourceLocation id = new ResourceLocation(JSONUtils.getAsString(object, "type"));
        MobSpawnProcessorType<P> type = MobSpawnProcessorType.findById(id);
        if (type == null) {
            throw new JsonSyntaxException("Unknown spawn processor: " + id);
        }
        IMobSpawnProcessorSerializer<P> serializer = type.getSerializer();
        return serializer.deserialize(type, object);
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
