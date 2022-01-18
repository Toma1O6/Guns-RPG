package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.airdrop.SlotConfiguration;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;

public class SlotConfigurationAdapter implements JsonDeserializer<SlotConfiguration> {

    @Override
    public SlotConfiguration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        int weight = JSONUtils.getAsInt(object, "weight", 1);
        int slotCount = JSONUtils.getAsInt(object, "count");
        if (weight <= 0) {
            throw new JsonSyntaxException("Invalid property value of 'weight': Value must be bigger than 0");
        }
        if (slotCount <= 0 || slotCount > 9) {
            throw new JsonSyntaxException("Invalid property value of 'count': Value must be from interval <1; 9>");
        }
        return new SlotConfiguration(weight, slotCount);
    }
}
