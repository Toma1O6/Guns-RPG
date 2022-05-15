package dev.toma.gunsrpg.common.quests.quest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;

import java.util.Arrays;

public class ItemHandoverData implements IQuestData {

    private final ItemStack[] items;

    public ItemHandoverData(ItemStack[] items) {
        this.items = items;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <Q extends IQuestData> Q copy() {
        return (Q) new ItemHandoverData(items);
    }

    @Override
    public String toString() {
        String[] itemArray = Arrays.stream(items).map(item -> String.format("%sx%s", item.getCount(), item.getDisplayName().getString().replaceAll("[]\\[]", ""))).toArray(String[]::new);
        return String.format("ItemHandover - Items [ %s ]", String.join(", ", itemArray));
    }

    public static final class Serializer implements QuestType.IQuestDataResolver<ItemHandoverData> {

        @Override
        public ItemHandoverData resolve(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            JsonArray array = JSONUtils.getAsJsonArray(object, "items");
            ItemStack[] items = JsonHelper.deserializeInto(array, ItemStack[]::new, this::resolveItemStack);
            return new ItemHandoverData(items);
        }

        private ItemStack resolveItemStack(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            JsonElement itemId = object.get("item");
            Item item = JsonHelper.resolveItem(itemId);
            int count = JSONUtils.getAsInt(object, "count", 1);
            return new ItemStack(item, count);
        }
    }
}
