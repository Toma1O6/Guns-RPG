package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.perks.FusionConfiguration;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;

public class FusionSwapAdapter implements JsonDeserializer<FusionConfiguration.Swap> {

    @Override
    public FusionConfiguration.Swap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        int count = JSONUtils.getAsInt(object, "count");
        float chance = JSONUtils.getAsFloat(object, "chance");
        int price = JSONUtils.getAsInt(object, "price");
        return new FusionConfiguration.Swap(count, price, chance);
    }
}
