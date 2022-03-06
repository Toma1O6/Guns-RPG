package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.perks.PurificationConfiguration;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;

public class PurificationEntryAdapter implements JsonDeserializer<PurificationConfiguration.Entry> {

    @Override
    public PurificationConfiguration.Entry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        int count = JSONUtils.getAsInt(object, "count");
        int price = JSONUtils.getAsInt(object, "price");
        float successChance = JSONUtils.getAsFloat(object, "chance");
        float breakChance = JSONUtils.getAsFloat(object, "breakChance");
        return new PurificationConfiguration.Entry(count, price, successChance, breakChance);
    }
}
