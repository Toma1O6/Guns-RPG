package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.perks.CrystalConfiguration;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;

public class CrystalConfigurationAdapter implements JsonDeserializer<CrystalConfiguration> {

    @Override
    public CrystalConfiguration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        CrystalConfiguration.Spawns spawns = context.deserialize(JSONUtils.getAsJsonObject(object, "spawns"), CrystalConfiguration.Spawns.class);
        CrystalConfiguration.Storage storage = context.deserialize(JSONUtils.getAsJsonObject(object, "storage"), CrystalConfiguration.Storage.class);
        return new CrystalConfiguration(spawns, storage);
    }
}
