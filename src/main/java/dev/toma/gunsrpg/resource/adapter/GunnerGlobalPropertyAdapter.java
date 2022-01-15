package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.gunner.GunnerGlobalProperties;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;

public class GunnerGlobalPropertyAdapter implements JsonDeserializer<GunnerGlobalProperties> {

    @Override
    public GunnerGlobalProperties deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        float multiplier = Math.max(0.0F, JSONUtils.getAsFloat(object, "damageMultiplier"));
        float inaccuracy = Math.max(0.0F, JSONUtils.getAsFloat(object, "inaccuracy"));
        float accuracyBonus = Math.max(0.0F, JSONUtils.getAsFloat(object, "accuracyBonus"));
        return new GunnerGlobalProperties(multiplier, inaccuracy, accuracyBonus);
    }
}
