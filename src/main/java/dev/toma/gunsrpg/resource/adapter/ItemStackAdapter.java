package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Type;

public class ItemStackAdapter implements JsonDeserializer<ItemStack> {

    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonSyntaxException("Expected JsonObject.");
        JsonObject obj = json.getAsJsonObject();
        String registryName = JSONUtils.getAsString(obj, "item");
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(registryName));
        if (item == Items.AIR)
            throw new JsonSyntaxException("Unknown item '" + registryName + "'.");
        int count = JSONUtils.getAsInt(obj, "count", 1);
        return new ItemStack(item, count);
    }
}
