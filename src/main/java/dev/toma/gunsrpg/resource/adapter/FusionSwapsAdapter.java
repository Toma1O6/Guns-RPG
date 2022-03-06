package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.perks.FusionConfiguration;
import dev.toma.gunsrpg.util.helper.JsonHelper;

import java.lang.reflect.Type;

public class FusionSwapsAdapter implements JsonDeserializer<FusionConfiguration.Swaps> {

    @Override
    public FusionConfiguration.Swaps deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray array = JsonHelper.asJsonArray(json);
        FusionConfiguration.Swap[] swaps = JsonHelper.deserializeInto(array, FusionConfiguration.Swap[]::new, e -> context.deserialize(e, FusionConfiguration.Swap.class));
        return new FusionConfiguration.Swaps(swaps);
    }
}
