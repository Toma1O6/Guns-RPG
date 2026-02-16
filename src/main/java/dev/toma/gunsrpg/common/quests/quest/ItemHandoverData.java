package dev.toma.gunsrpg.common.quests.quest;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class ItemHandoverData implements IQuestData {

    private final int min;
    private final int max;
    private final WeightedRandom<ResourceGroup> groups;

    public ItemHandoverData(int min, int max, WeightedRandom<ResourceGroup> groups) {
        this.min = min;
        this.max = max;
        this.groups = groups;
    }

    public int getMinItems() {
        return min;
    }

    public int getMaxItems() {
        return max;
    }

    public WeightedRandom<ResourceGroup> getGroups() {
        return groups;
    }

    @Override
    public String toString() {
        return "ItemHandover - [" +
                "min=" + min +
                ", max=" + max +
                ", groups=" + groups +
                ']';
    }

    public static final class ResourceGroup {

        private final int weight;
        private final int min;
        private final int max;
        private final List<Item> items;

        public ResourceGroup(int weight, int min, int max, List<Item> items) {
            this.weight = weight;
            this.min = min;
            this.max = max;
            this.items = items;
        }

        public int getWeight() {
            return weight;
        }

        public ItemStack generate(Random random) {
            int count = this.min + random.nextInt(Math.max(0, this.max - this.min) + 1);
            Item item = ModUtils.getRandomListElement(this.items, random);
            return new ItemStack(item, count);
        }

        public CompoundNBT toNbt() {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("weight", weight);
            nbt.putInt("min", min);
            nbt.putInt("max", max);
            ListNBT list = new ListNBT();
            for (Item item : items) {
                list.add(StringNBT.valueOf(ForgeRegistries.ITEMS.getKey(item).toString()));
            }
            nbt.put("items", list);
            return nbt;
        }

        public static ResourceGroup fromNbt(CompoundNBT nbt) {
            int weight = nbt.getInt("weight");
            int min = nbt.getInt("min");
            int max = nbt.getInt("max");
            ListNBT list = nbt.getList("items", Constants.NBT.TAG_STRING);
            List<Item> items = new ArrayList<>(list.size());
            for (INBT inbt : list) {
                StringNBT itemNbt = (StringNBT) inbt;
                items.add(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemNbt.getAsString())));
            }
            return new ResourceGroup(weight, min, max, items);
        }

        public static ResourceGroup parseJson(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            int weight = JSONUtils.getAsInt(object, "weight", 1);
            int min = JSONUtils.getAsInt(object, "min");
            int max = JSONUtils.getAsInt(object, "max");
            if (min > max) {
                throw new JsonSyntaxException("Min item count cannot be larger than max item count! Got min:" + min + ", max: " + max);
            }
            Set<Item> entries = new HashSet<>();
            if (object.has("item")) {
                String id = JSONUtils.getAsString(object, "item");
                Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(id));
                if (item == Items.AIR)
                    throw new JsonSyntaxException("Unknown item: " + id);
                entries.add(item);
            }
            if (object.has("tag")) {
                String id = JSONUtils.getAsString(object, "tag");
                ITagCollection<Item> collection = TagCollectionManager.getInstance().getItems();
                ITag<Item> tag = collection.getTag(ResourceLocation.tryParse(id));
                if (tag == null)
                    throw new JsonSyntaxException("Unknown item tag: " + id);
                entries.addAll(tag.getValues());
            }
            if (entries.isEmpty()) {
                throw new JsonSyntaxException("No valid item or tag provided!");
            }
            return new ResourceGroup(weight, min, max, ImmutableList.copyOf(entries));
        }
    }

    public static final class Serializer implements QuestType.IQuestDataResolver<ItemHandoverData> {

        @Override
        public ItemHandoverData resolve(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            int min = JSONUtils.getAsInt(object, "min");
            int max = JSONUtils.getAsInt(object, "max");
            if (min > max) {
                throw new JsonSyntaxException("Min item count cannot be larger than max item count! Got min:" + min + ", max: " + max);
            }
            JsonArray groupsJson = JSONUtils.getAsJsonArray(object, "groups");
            List<ResourceGroup> groups = JsonHelper.deserializeAsList(groupsJson, ResourceGroup::parseJson);
            if (groups.isEmpty()) {
                throw new JsonSyntaxException("No valid groups provided!");
            }
            WeightedRandom<ResourceGroup> random = new WeightedRandom<>(ResourceGroup::getWeight, groups);
            return new ItemHandoverData(min, max, random);
        }

        @Override
        public CompoundNBT serialize(ItemHandoverData data) {
            CompoundNBT nbt = new CompoundNBT();
            ListNBT list = new ListNBT();
            for (ResourceGroup group : data.groups) {
                CompoundNBT stackNbt = group.toNbt();
                list.add(stackNbt);
            }
            nbt.putInt("min", data.min);
            nbt.putInt("max", data.max);
            nbt.put("groups", list);
            return nbt;
        }

        @Override
        public ItemHandoverData deserialize(CompoundNBT nbt) {
            ListNBT listNBT = nbt.getList("groups", Constants.NBT.TAG_COMPOUND);
            List<ResourceGroup> groups = new ArrayList<>(listNBT.size());
            for (INBT inbt : listNBT) {
                CompoundNBT groupNbt = (CompoundNBT) inbt;
                groups.add(ResourceGroup.fromNbt(groupNbt));
            }
            int min = nbt.getInt("min");
            int max = nbt.getInt("max");
            WeightedRandom<ResourceGroup> random = new WeightedRandom<>(ResourceGroup::getWeight, groups);
            return new ItemHandoverData(min, max, random);
        }
    }
}
