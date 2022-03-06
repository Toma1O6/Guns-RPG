package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.perks.CrystalConfiguration;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;

public class CrystalSpawnAdapter implements JsonDeserializer<CrystalConfiguration.Spawn> {

    @Override
    public CrystalConfiguration.Spawn deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        int level = JSONUtils.getAsInt(object, "level");
        int weight = JSONUtils.getAsInt(object, "weight");
        return new CrystalConfiguration.Spawn(level, weight);
    }
}
