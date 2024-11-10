package dev.toma.gunsrpg.common.quests.condition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;
import java.util.function.Function;

public class SpecificWeaponConditionProvider extends AbstractQuestConditionProvider<SpecificWeaponConditionProvider.Condition> {

    private final Item[] validItems;
    private final Selector selector;
    private final String group;

    private SpecificWeaponConditionProvider(QuestConditionProviderType<SpecificWeaponConditionProvider> type, Selector selector, String group, Item[] items) {
        super(type);
        this.selector = selector;
        this.validItems = items;
        this.group = group;
    }

    public static SpecificWeaponConditionProvider fromNbt(QuestConditionProviderType<SpecificWeaponConditionProvider> type, CompoundNBT nbt) {
        Selector selector = Selector.values()[nbt.getInt("selector")];
        ListNBT validItemList = nbt.getList("validItems", Constants.NBT.TAG_STRING);
        Item[] items = new Item[validItemList.size()];
        int itemIndex = 0;
        for (INBT inbt : validItemList) {
            StringNBT stringNBT = (StringNBT) inbt;
            ResourceLocation itemId = new ResourceLocation(stringNBT.getAsString());
            items[itemIndex++] = ForgeRegistries.ITEMS.getValue(itemId);
        }
        String group = nbt.getString("group");
        return new SpecificWeaponConditionProvider(type, selector, group, items);
    }

    @Override
    public Condition makeConditionInstance() {
        return new Condition(this.selector.filterItems(this.validItems));
    }

    @Override
    public void saveInternalData(CompoundNBT nbt) {
        nbt.putInt("selector", selector.ordinal());
        ListNBT list = new ListNBT();
        for (Item item : validItems) {
            list.add(StringNBT.valueOf(item.getRegistryName().toString()));
        }
        nbt.put("validItems", list);
        nbt.putString("group", group);
    }

    private enum Selector {

        ONE(items -> {
            Random random = new Random();
            Item[] result = new Item[1];
            result[0] = items[random.nextInt(items.length)];
            return result;
        }),
        ANY(items -> items);

        final Function<Item[], Item[]> filter;

        Selector(Function<Item[], Item[]> filter) {
            this.filter = filter;
        }

        public Item[] filterItems(Item[] items) {
            return filter.apply(items);
        }
    }

    public static class Serializer implements IQuestConditionProviderSerializer<SpecificWeaponConditionProvider> {

        @Override
        public SpecificWeaponConditionProvider deserialize(QuestConditionProviderType<SpecificWeaponConditionProvider> conditionType, JsonElement data) {
            JsonObject object = JsonHelper.asJsonObject(data);
            String selectorId = JSONUtils.getAsString(object, "selector", "any").toUpperCase();
            Selector selector = Selector.ANY;
            try {
                selector = Selector.valueOf(selectorId);
            } catch (Exception ignored) {
            }
            String group = JSONUtils.getAsString(object, "group", "");
            Item[] items = JsonHelper.deserializeInto(JSONUtils.getAsJsonArray(object, "items"), Item[]::new, JsonHelper::resolveItem);
            return new SpecificWeaponConditionProvider(conditionType, selector, group, items);
        }
    }

    public class Condition implements IQuestCondition {

        private Item[] validItems;
        private ITextComponent[] descriptors;

        public Condition(Item[] validItems) {
            this.validItems = validItems;
            this.updateDescriptor();
        }

        @Override
        public boolean isValid(QuestingGroup group, IPropertyReader reader) {
            ItemStack stack = reader.getProperty(QuestProperties.USED_ITEM);
            return ModUtils.contains(stack.getItem(), validItems);
        }

        @Override
        public Boolean isValidInClientContext(Quest<?> quest, PlayerEntity player) {
            ItemStack itemStack = player.getMainHandItem();
            for (Item item : validItems) {
                if (itemStack.getItem() == item)
                    return true;
            }
            return false;
        }

        @Override
        public ITextComponent getDescriptor(boolean shortDesc) {
            return descriptors[shortDesc ? 1 : 0];
        }

        @Override
        public IQuestConditionProvider<?> getProviderType() {
            return SpecificWeaponConditionProvider.this;
        }

        @Override
        public void saveData(CompoundNBT nbt) {
            ListNBT conditionItemList = new ListNBT();
            for (Item item : validItems) {
                conditionItemList.add(StringNBT.valueOf(item.getRegistryName().toString()));
            }
            nbt.put("condition.items", conditionItemList);
        }

        @Override
        public void loadData(CompoundNBT nbt) {
            ListNBT conditionItemList = nbt.getList("condition.items", Constants.NBT.TAG_STRING);
            this.validItems = conditionItemList.stream().map(inbt -> {
                ResourceLocation location = new ResourceLocation(inbt.getAsString());
                return ForgeRegistries.ITEMS.getValue(location);
            }).toArray(Item[]::new);
            this.updateDescriptor();
        }

        private void updateDescriptor() {
            SpecificWeaponConditionProvider provider = SpecificWeaponConditionProvider.this;
            String text = provider.selector == Selector.ANY ? provider.group : validItems[0].getName(ItemStack.EMPTY).getString();
            this.descriptors = expandWithShortLocalizations(new TranslationTextComponent(provider.getLocalizationString(), text));
        }
    }
}
