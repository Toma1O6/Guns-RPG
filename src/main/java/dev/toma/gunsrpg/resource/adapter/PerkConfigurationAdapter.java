package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.perks.CrystalConfiguration;
import dev.toma.gunsrpg.resource.perks.FusionConfiguration;
import dev.toma.gunsrpg.resource.perks.PerkConfiguration;
import dev.toma.gunsrpg.resource.perks.PurificationConfiguration;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;

public class PerkConfigurationAdapter implements JsonDeserializer<PerkConfiguration> {

    @Override
    public PerkConfiguration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        CrystalConfiguration crystals = context.deserialize(JSONUtils.getAsJsonObject(object, "crystals"), CrystalConfiguration.class);
        FusionConfiguration fusion = context.deserialize(JSONUtils.getAsJsonObject(object, "fusion"), FusionConfiguration.class);
        PurificationConfiguration purification = context.deserialize(JSONUtils.getAsJsonArray(object, "purification"), PurificationConfiguration.class);
        return new PerkConfiguration(crystals, fusion, purification);
    }
}
