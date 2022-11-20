package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.perks.FusionConfiguration;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.lang.reflect.Type;

public class BreakChanceReductionsAdapter implements JsonDeserializer<FusionConfiguration.BreakChanceReductions> {

    @Override
    public FusionConfiguration.BreakChanceReductions deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray data = JsonHelper.asJsonArray(json);
        Int2ObjectMap<FusionConfiguration.BreakChanceReduction> map = new Int2ObjectOpenHashMap<>();
        for (JsonElement element : data) {
            FusionConfiguration.BreakChanceReduction reduction = context.deserialize(element, FusionConfiguration.BreakChanceReduction.class);
            map.put(reduction.getOrbCount(), reduction);
        }
        return new FusionConfiguration.BreakChanceReductions(map);
    }
}
