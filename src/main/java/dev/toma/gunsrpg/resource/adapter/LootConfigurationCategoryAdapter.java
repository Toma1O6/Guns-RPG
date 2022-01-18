package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.airdrop.LootConfigurationCategory;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;

public class LootConfigurationCategoryAdapter implements JsonDeserializer<LootConfigurationCategory> {

    @Override
    public LootConfigurationCategory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        int weight = JSONUtils.getAsInt(object, "weight", 1);
        String identifier = JSONUtils.getAsString(object, "id", null);
        if (weight <= 0) {
            throw new JsonSyntaxException("Invalid property value: 'weight' property value must be bigger than 0");
        }
        if (ModUtils.isNullOrEmpty(identifier)) {
            throw new JsonSyntaxException("Invalid property value: 'id' property must be defined and it's value cannot be empty");
        }
        return new LootConfigurationCategory(weight, identifier);
    }
}
