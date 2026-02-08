package dev.toma.gunsrpg.common.quests.condition;

import com.google.gson.*;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.common.quests.quest.IQuestFactory;
import dev.toma.gunsrpg.common.quests.quest.Quest;
import dev.toma.gunsrpg.common.quests.sharing.QuestingGroup;
import dev.toma.gunsrpg.common.skills.core.SkillType;
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
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SpecificWeaponConditionProvider extends AbstractQuestConditionProvider<SpecificWeaponConditionProvider.Condition> {

    private final List<ItemGroup> items;
    private final Selector selector;
    private final String group;

    private SpecificWeaponConditionProvider(QuestConditionProviderType<SpecificWeaponConditionProvider> type, Selector selector, String group, List<ItemGroup> items) {
        super(type);
        this.selector = selector;
        this.items = items;
        this.group = group;
    }

    public static SpecificWeaponConditionProvider fromNbt(QuestConditionProviderType<SpecificWeaponConditionProvider> type, CompoundNBT nbt) {
        Selector selector = Selector.values()[nbt.getInt("selector")];
        ListNBT validItemList = nbt.getList("item_groups", Constants.NBT.TAG_COMPOUND);
        List<ItemGroup> items = new ArrayList<>(validItemList.size());
        for (INBT inbt : validItemList) {
            CompoundNBT groupNbt = (CompoundNBT) inbt;
            items.add(ItemGroup.deserialize(groupNbt));
        }
        String group = nbt.getString("group");
        return new SpecificWeaponConditionProvider(type, selector, group, items);
    }

    @Override
    public Condition createDefaultInstance() {
        return new Condition();
    }

    @Override
    public Condition createWithContext(IQuestFactory.InstanceContext<?, ?> context) {
        PlayerEntity player = context.getPartyLeader();
        List<Item> validItems = this.selector.pick(this.items, player);
        return new Condition(validItems);
    }

    @Override
    public void saveInternalData(CompoundNBT nbt) {
        nbt.putInt("selector", selector.ordinal());
        ListNBT list = new ListNBT();
        for (ItemGroup itemGroup : this.items) {
            list.add(itemGroup.serialize());
        }
        nbt.put("item_groups", list);
        nbt.putString("group", group);
    }

    private enum Selector {

        ONE(Selector::single),
        ANY(Selector::any);

        final Function<List<ItemGroup>, List<Item>> filter;

        Selector(Function<List<ItemGroup>, List<Item>> filter) {
            this.filter = filter;
        }

        public List<Item> pick(List<ItemGroup> items, PlayerEntity player) {
            List<ItemGroup> validGroups = items.stream()
                    .filter(group -> group.canApply(player))
                    .collect(Collectors.toList());

            return filter.apply(validGroups);
        }

        private static List<Item> single(List<ItemGroup> items) {
            Random random = new Random();
            ItemGroup group = ModUtils.getRandomListElement(items, random);
            return Collections.singletonList(ModUtils.getRandomListElement(group.items, random));
        }

        private static List<Item> any(List<ItemGroup> items) {
            return items.stream()
                    .flatMap(group -> group.items.stream())
                    .collect(Collectors.toList());
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
            JsonArray itemGroupsJson = JSONUtils.getAsJsonArray(object, "item_groups");
            List<ItemGroup> itemGroups = JsonHelper.deserializeAsList(itemGroupsJson, ItemGroup::parse);
            return new SpecificWeaponConditionProvider(conditionType, selector, group, itemGroups);
        }
    }

    public class Condition implements IQuestCondition {

        private List<Item> validItems;
        private ITextComponent[] descriptors;

        public Condition() {
            this.descriptors = new ITextComponent[]{StringTextComponent.EMPTY, StringTextComponent.EMPTY};
        }

        public Condition(List<Item> validItems) {
            this.validItems = validItems;
            this.updateDescriptor();
        }

        @Override
        public boolean isValid(QuestingGroup group, IPropertyReader reader) {
            ItemStack stack = reader.getProperty(QuestProperties.USED_ITEM);
            return this.validItems.contains(stack.getItem());
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
            }).collect(Collectors.toList());
            this.updateDescriptor();
        }

        private void updateDescriptor() {
            SpecificWeaponConditionProvider provider = SpecificWeaponConditionProvider.this;
            String text = provider.selector == Selector.ANY ? provider.group : validItems.get(0).getName(ItemStack.EMPTY).getString();
            this.descriptors = expandWithShortLocalizations(new TranslationTextComponent(provider.getLocalizationString(), text));
        }
    }

    private static final class ItemGroup {

        private final List<Item> items;
        private final List<SkillType<?>> requirements;

        public ItemGroup(List<Item> items, List<SkillType<?>> requirements) {
            this.items = items;
            this.requirements = requirements;
        }

        public boolean canApply(PlayerEntity player) {
            for (SkillType<?> requirement : requirements) {
                if (!PlayerData.hasActiveSkill(player, requirement)) {
                    return false;
                }
            }
            return true;
        }

        public static ItemGroup parse(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            JsonArray itemsJson = JSONUtils.getAsJsonArray(object, "items");
            List<Item> items = JsonHelper.deserializeAsList(itemsJson, JsonHelper::resolveItem);
            if (items.isEmpty())
                throw new JsonSyntaxException("No items specified for item group!");
            JsonArray requirementsJson = JSONUtils.getAsJsonArray(object, "requirements", new JsonArray());
            List<SkillType<?>> requirements = JsonHelper.deserializeAsList(requirementsJson, JsonHelper::resolveSkill);
            return new ItemGroup(items, requirements);
        }

        public CompoundNBT serialize() {
            CompoundNBT nbt = new CompoundNBT();
            ListNBT itemNbt = new ListNBT();
            for (Item item : items) {
                itemNbt.add(StringNBT.valueOf(item.getRegistryName().toString()));
            }
            nbt.put("items", itemNbt);
            ListNBT requirementNbt = new ListNBT();
            for (SkillType<?> requirement : requirements) {
                requirementNbt.add(StringNBT.valueOf(requirement.getRegistryName().toString()));
            }
            nbt.put("requirements", requirementNbt);
            return nbt;
        }

        public static ItemGroup deserialize(CompoundNBT nbt) {
            ListNBT itemNbt = nbt.getList("items", Constants.NBT.TAG_STRING);
            List<Item> items = new ArrayList<>(itemNbt.size());
            for (INBT inbt : itemNbt) {
                StringNBT stringNBT = (StringNBT) inbt;
                ResourceLocation itemId = new ResourceLocation(stringNBT.getAsString());
                items.add(ForgeRegistries.ITEMS.getValue(itemId));
            }
            ListNBT requirementNbt = nbt.getList("requirements", Constants.NBT.TAG_STRING);
            List<SkillType<?>> requirements = new ArrayList<>(requirementNbt.size());
            for (INBT inbt : requirementNbt) {
                StringNBT stringNBT = (StringNBT) inbt;
                ResourceLocation skillId = new ResourceLocation(stringNBT.getAsString());
                requirements.add(ModRegistries.SKILLS.getValue(skillId));
            }
            return new ItemGroup(items, requirements);
        }
    }
}
