package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.perks.CrystalConfiguration;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;

public class CrystalSpawnsAdapter implements JsonDeserializer<CrystalConfiguration.Spawns> {

    @Override
    public CrystalConfiguration.Spawns deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        JsonArray array = JSONUtils.getAsJsonArray(object, "levels");
        CrystalConfiguration.Spawn[] spawns = JsonHelper.deserializeInto(array, CrystalConfiguration.Spawn[]::new, e -> context.deserialize(e, CrystalConfiguration.Spawn.class));
        CrystalConfiguration.Types types = context.deserialize(JSONUtils.getAsJsonObject(object, "types"), CrystalConfiguration.Types.class);
        return new CrystalConfiguration.Spawns(spawns, types);
    }
}
