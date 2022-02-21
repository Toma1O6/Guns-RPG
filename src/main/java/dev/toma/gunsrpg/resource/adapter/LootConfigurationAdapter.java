package dev.toma.gunsrpg.resource.adapter;

import com.google.gson.*;
import dev.toma.gunsrpg.resource.crate.LootConfig;
import dev.toma.gunsrpg.resource.crate.LootConfigurationCategory;
import dev.toma.gunsrpg.resource.crate.LootEntry;
import dev.toma.gunsrpg.resource.crate.SlotConfiguration;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class LootConfigurationAdapter implements JsonDeserializer<LootConfig> {

    @Override
    public LootConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(json);
        // categories
        JsonArray categoryArray = JSONUtils.getAsJsonArray(object, "categories");
        LootConfigurationCategory[] categories = new LootConfigurationCategory[categoryArray.size()];
        for (int i = 0; i < categoryArray.size(); i++) {
            JsonElement element = categoryArray.get(i);
            categories[i] = context.deserialize(element, LootConfigurationCategory.class);
        }
        // slots
        JsonArray slotArray = JSONUtils.getAsJsonArray(object, "slots");
        SlotConfiguration[] slots = new SlotConfiguration[slotArray.size()];
        for (int i = 0; i < slotArray.size(); i++) {
            JsonElement element = slotArray.get(i);
            slots[i] = context.deserialize(element, SlotConfiguration.class);
        }
        // pools
        JsonObject mappedPools = JSONUtils.getAsJsonObject(object, "lootConfiguration");
        Set<String> keySet = mappedPools.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toSet());
        checkAllKeysContained(keySet, categories);
        Map<LootConfigurationCategory, List<LootEntry>> poolsByCategory = new HashMap<>();
        for (LootConfigurationCategory category : categories) {
            JsonArray entries = JSONUtils.getAsJsonArray(mappedPools, category.getIdentifier());
            List<LootEntry> list = new ArrayList<>();
            for (JsonElement element : entries) {
                list.add(context.deserialize(element, LootEntry.class));
            }
            poolsByCategory.put(category, list);
        }
        return new LootConfig(slots, categories, poolsByCategory);
    }

    private void checkAllKeysContained(Set<String> keySet, LootConfigurationCategory[] categories) {
        Set<String> missing = new HashSet<>();
        for (LootConfigurationCategory category : categories) {
            String key = category.getIdentifier();
            if (!keySet.contains(key)) {
                missing.add(key);
            }
        }
        if (!missing.isEmpty()) {
            throw new JsonSyntaxException(String.format("Missing loot pool definitions for keys [%s]", String.join(", ", missing)));
        }
    }
}
