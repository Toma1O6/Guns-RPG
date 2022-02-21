package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.crate.ICountFunction;
import dev.toma.gunsrpg.resource.crate.LootEntry;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Type;

public class LootEntryAdapter implements JsonDeserializer<LootEntry> {

    @Override
    public LootEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        int weight = JSONUtils.getAsInt(object, "weight", 1);
        String itemId = JSONUtils.getAsString(object, "item", null);
        if (ModUtils.isNullOrEmpty(itemId))
            throw new JsonSyntaxException("Invalid property value: 'item' must be defined and it's value cannot be empty");
        ResourceLocation id = new ResourceLocation(itemId);
        Item item = ForgeRegistries.ITEMS.getValue(id);
        if (item == null || item == Items.AIR) {
            throw new JsonSyntaxException(String.format("Invalid property value of 'item': '%s' is not a valid item id", itemId));
        }
        JsonObject countObj = JSONUtils.getAsJsonObject(object, "count");
        ICountFunction function = context.deserialize(countObj, ICountFunction.class);
        return new LootEntry(weight, item, function);
    }
}
