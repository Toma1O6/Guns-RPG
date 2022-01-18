package dev.toma.gunsrpg.resource.airdrop;

import dev.toma.gunsrpg.util.math.WeightedRandom;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class AirdropLootConfiguration {

    private final WeightedRandom<SlotConfiguration> slotConfigs;
    private final WeightedRandom<LootConfigurationCategory> categories;
    private final Map<LootConfigurationCategory, WeightedRandom<LootEntry>> entriesByCategory;

    public AirdropLootConfiguration(SlotConfiguration[] slots, LootConfigurationCategory[] categories, Map<LootConfigurationCategory, List<LootEntry>> entriesByCategory) {
        this.slotConfigs = new WeightedRandom<>(SlotConfiguration::getWeight, slots);
        this.categories = new WeightedRandom<>(LootConfigurationCategory::getWeight, categories);
        this.entriesByCategory = entriesByCategory.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, this::mapValues));
    }

    public IAirdropContentProvider getRandomContent() {
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
}
