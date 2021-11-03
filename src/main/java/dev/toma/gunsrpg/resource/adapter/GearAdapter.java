package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.resource.startgear.Gear;
import dev.toma.gunsrpg.resource.startgear.IGear;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.lang.reflect.Type;

public class GearAdapter implements JsonDeserializer<IGear> {

    private static final Marker MARKER = MarkerManager.getMarker("StartGearAdapter");

    @Override
    public IGear deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject()) {
            throw new JsonSyntaxException("Expected JsonObject to be root element.");
        }
        JsonObject data = json.getAsJsonObject();
        JsonArray elements = JSONUtils.getAsJsonArray(data, "items");
        ItemStack[] stacks = new ItemStack[elements.size()];
        int i = 0;
        for (JsonElement element : elements) {
            try {
                stacks[i++] = context.deserialize(element, ItemStack.class);
            } catch (JsonParseException exc) {
                GunsRPG.log.error(MARKER, "Error loading start gear item: " + exc);
            }
        }
        return new Gear(ModUtils.trimArray(stacks, ItemStack.class));
    }
}
