package dev.toma.gunsrpg.common.quests.condition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;

import java.util.Random;
import java.util.function.Function;

public class SpecificWeaponConditionProvider extends AbstractQuestConditionProvider {

    private final Item[] validItems;
    private final Selector selector;

    private SpecificWeaponConditionProvider(QuestConditionProviderType<SpecificWeaponConditionProvider> type, Selector selector, Item[] items) {
        super(type);
        this.selector = selector;
        this.validItems = items;
    }

    @Override
    public IQuestCondition getCondition() {
        return new Condition(this.selector.filterItems(this.validItems));
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
            Item[] items = JsonHelper.deserializeInto(JSONUtils.getAsJsonArray(object, "items"), Item[]::new, JsonHelper::resolveItem);
            return new SpecificWeaponConditionProvider(conditionType, selector, items);
        }
    }

    public static class Condition implements IQuestCondition {

        private final Item[] validItems;

        public Condition(Item[] validItems) {
            this.validItems = validItems;
        }

        @Override
        public boolean isValid(PlayerEntity player, IPropertyReader reader) {
            ItemStack stack = reader.getProperty(QuestProperties.USED_ITEM);
            return ModUtils.contains(stack.getItem(), validItems);
        }
    }
}
