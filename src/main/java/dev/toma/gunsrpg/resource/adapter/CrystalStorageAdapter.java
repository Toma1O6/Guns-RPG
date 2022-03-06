package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.perks.CrystalConfiguration;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;

public class CrystalStorageAdapter implements JsonDeserializer<CrystalConfiguration.Storage> {

    @Override
    public CrystalConfiguration.Storage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        int buff = JSONUtils.getAsInt(object, "buff");
        int debuff = JSONUtils.getAsInt(object, "debuff");
        return new CrystalConfiguration.Storage(buff, debuff);
    }
}
