package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.perks.FusionConfiguration;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;

public class FusionUpgradeAdapter implements JsonDeserializer<FusionConfiguration.Upgrade> {

    @Override
    public FusionConfiguration.Upgrade deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        int target = JSONUtils.getAsInt(object, "targetLevel");
        float breakChance = JSONUtils.getAsFloat(object, "chanceOfBreaking");
        int price = JSONUtils.getAsInt(object, "price");
        return new FusionConfiguration.Upgrade(target, breakChance, price);
    }
}
