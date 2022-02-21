package dev.toma.gunsrpg.resource.crate;

import dev.toma.gunsrpg.util.math.WeightedRandom;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LootConfig {

    public static final LootConfig EMPTY = new Empty();

    private final WeightedRandom<SlotConfiguration> slotConfigs;
    private final WeightedRandom<LootConfigurationCategory> categories;
    private final Map<LootConfigurationCategory, WeightedRandom<LootEntry>> entriesByCategory;

    public LootConfig(SlotConfiguration[] slots, LootConfigurationCategory[] categories, Map<LootConfigurationCategory, List<LootEntry>> entriesByCategory) {
        this.slotConfigs = new WeightedRandom<>(SlotConfiguration::getWeight, slots);
        this.categories = new WeightedRandom<>(LootConfigurationCategory::getWeight, categories);
        this.entriesByCategory = entriesByCategory.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, this::mapValues));
    }

    public ILootContentProvider getRandomContent() {
        int size = slotConfigs.getRandom().getCount();
        LootConfigurationCategory category = categories.getRandom();
        ItemStack[] items = new ItemStack[size];
        for (int i = 0; i < size; i++) {
            items[i] = getByCategory(category);
        }
        return () -> items;
    }

    public ItemStack getByCategory(LootConfigurationCategory category) {
        LootEntry entry = entriesByCategory.get(category).getRandom();
        return entry.produceItem();
    }

    private WeightedRandom<LootEntry> mapValues(Map.Entry<LootConfigurationCategory, List<LootEntry>> entry) {
        return new WeightedRandom<>(LootEntry::getWeight, entry.getValue().toArray(new LootEntry[0]));
    }

    private static final class Empty extends LootConfig {

        private static final ILootContentProvider NO_CONTENT = () -> new ItemStack[0];

        private Empty() {
            super(new SlotConfiguration[0], new LootConfigurationCategory[0], Collections.emptyMap());
        }

        @Override
        public ILootContentProvider getRandomContent() {
            return NO_CONTENT;
        }
    }
}
