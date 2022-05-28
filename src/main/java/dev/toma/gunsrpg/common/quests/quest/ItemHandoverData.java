package dev.toma.gunsrpg.common.quests.quest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.util.Constants;

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

    public ItemStack[] getItems() {
        return items;
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

        @Override
        public CompoundNBT serialize(ItemHandoverData data) {
            CompoundNBT nbt = new CompoundNBT();
            ListNBT list = new ListNBT();
            for (ItemStack stack : data.items) {
                CompoundNBT stackNbt = stack.serializeNBT();
                list.add(stackNbt);
            }
            nbt.put("items", list);
            return nbt;
        }

        @Override
        public ItemHandoverData deserialize(CompoundNBT nbt) {
            ListNBT listNBT = nbt.getList("items", Constants.NBT.TAG_COMPOUND);
            ItemStack[] stacks = listNBT.stream().map(inbt -> {
                CompoundNBT itemNbt = (CompoundNBT) inbt;
                return ItemStack.of(itemNbt);
            }).toArray(ItemStack[]::new);
            return new ItemHandoverData(stacks);
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
