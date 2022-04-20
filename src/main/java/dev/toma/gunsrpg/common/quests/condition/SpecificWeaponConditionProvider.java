package dev.toma.gunsrpg.common.quests.condition;

import dev.toma.gunsrpg.common.init.GunDamageSource;
import dev.toma.gunsrpg.common.quests.QuestProperties;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.properties.IPropertyReader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

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

    public static class Serializer {

    }

    public static class Condition implements IQuestCondition {

        private final Item[] validItems;

        public Condition(Item[] validItems) {
            this.validItems = validItems;
        }

        @Override
        public boolean isValid(PlayerEntity player, IPropertyReader reader) {
            DamageSource source = reader.getProperty(QuestProperties.DAMAGE_SOURCE);
            Entity entity = source.getEntity();
            ItemStack stack = source instanceof GunDamageSource ? ((GunDamageSource) source).getStacc() : entity instanceof LivingEntity ? ((LivingEntity) entity).getMainHandItem() : ItemStack.EMPTY;
            return ModUtils.contains(stack.getItem(), validItems);
        }
    }
}
