package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.perks.PurificationConfiguration;
import dev.toma.gunsrpg.util.helper.JsonHelper;

import java.lang.reflect.Type;

public class PurificationConfigurationAdapter implements JsonDeserializer<PurificationConfiguration> {

    @Override
    public PurificationConfiguration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray array = JsonHelper.asJsonArray(json);
        PurificationConfiguration.Entry[] entries = JsonHelper.deserializeInto(array, PurificationConfiguration.Entry[]::new, e -> context.deserialize(e, PurificationConfiguration.Entry.class));
        return new PurificationConfiguration(entries);
    }
}
