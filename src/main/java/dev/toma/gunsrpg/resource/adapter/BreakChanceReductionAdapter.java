package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.perks.FusionConfiguration;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;

public class BreakChanceReductionAdapter implements JsonDeserializer<FusionConfiguration.BreakChanceReduction> {

    @Override
    public FusionConfiguration.BreakChanceReduction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject data = JsonHelper.asJsonObject(json);
        int orbcount = JSONUtils.getAsInt(data, "count", 0);
        float multiplier = JSONUtils.getAsFloat(data, "multiplier", 1.0F);
        return new FusionConfiguration.BreakChanceReduction(orbcount, multiplier);
    }
}
