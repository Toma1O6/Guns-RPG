package dev.toma.gunsrpg.common.quests.reward;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.*;
import java.util.stream.Collectors;

public class RewardManager extends JsonReloadListener {

    private static final Gson GSON = new GsonBuilder().create();
    private final Map<ResourceLocation, QuestAwardList> awards = new HashMap<>();
    private final Map<String, List<ResourceLocation>> byCategory = new HashMap<>();

    public RewardManager() {
        super(GSON, "quest_system/rewards");
    }

    public QuestAwardList getValue(ResourceLocation location) {
        return awards.get(location);
    }

    public List<ResourceLocation> byCategory(String category) {
        List<ResourceLocation> locations = byCategory.get(category);
        return locations != null ? locations : Collections.emptyList();
    }

    public List<QuestAwardList> mapByCategory(String category) {
        return byCategory(category).stream().map(this::getValue).collect(Collectors.toList());
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, IResourceManager iResourceManager, IProfiler iProfiler) {
        fillCategories();
    }

    private void fillCategories() {
        byCategory.clear();
        awards.forEach((key, value) -> byCategory.computeIfAbsent(value.getCategory(), k -> new ArrayList<>()).add(key));
    }
}
