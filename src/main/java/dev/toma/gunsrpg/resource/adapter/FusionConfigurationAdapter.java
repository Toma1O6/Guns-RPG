package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.perks.FusionConfiguration;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;

public class FusionConfigurationAdapter implements JsonDeserializer<FusionConfiguration> {

    @Override
    public FusionConfiguration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        FusionConfiguration.Upgrades upgrades = context.deserialize(JSONUtils.getAsJsonArray(object, "upgrades"), FusionConfiguration.Upgrades.class);
        FusionConfiguration.Swaps swaps = context.deserialize(JSONUtils.getAsJsonArray(object, "crystalSwap"), FusionConfiguration.Swaps.class);
        FusionConfiguration.BreakChanceReductions reductions = context.deserialize(JSONUtils.getAsJsonArray(object, "breakChanceReductions"), FusionConfiguration.BreakChanceReductions.class);
        return new FusionConfiguration(upgrades, swaps, reductions);
    }
}
