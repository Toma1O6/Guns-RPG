package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.perks.FusionConfiguration;
import dev.toma.gunsrpg.util.helper.JsonHelper;

import java.lang.reflect.Type;

public class FusionUpgradesAdapter implements JsonDeserializer<FusionConfiguration.Upgrades> {

    @Override
    public FusionConfiguration.Upgrades deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray array = JsonHelper.asJsonArray(json);
        FusionConfiguration.Upgrade[] upgrades = JsonHelper.deserializeInto(array, FusionConfiguration.Upgrade[]::new, element -> context.deserialize(element, FusionConfiguration.Upgrade.class));
        return new FusionConfiguration.Upgrades(upgrades);
    }
}
