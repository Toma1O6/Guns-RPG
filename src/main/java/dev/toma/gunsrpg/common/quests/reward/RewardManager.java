package dev.toma.gunsrpg.common.quests.reward;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.toma.gunsrpg.util.ILogHandler;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.stream.Collectors;

public class RewardManager extends JsonReloadListener {

    public static final int MAX_TIER = 8;
    private static final Gson GSON = new GsonBuilder().create();
    private final Map<ResourceLocation, QuestAwardList> awards = new HashMap<>();
    private final Map<String, List<ResourceLocation>> byCategory = new HashMap<>();
    private final ILogHandler logger;

    public RewardManager(ILogHandler logger) {
        super(GSON, "quest_system/rewards");
        this.logger = logger;
    }

    public QuestAwardList getValue(ResourceLocation location) {
        return awards.get(location);
    }

    public Reward byTier(int tier) {
        Random random = new Random();
        int rewardTier = random.nextInt(tier + 1);
        List<QuestAwardList> available = awards.values().stream().filter(questAwardList -> questAwardList.withinRange(rewardTier)).collect(Collectors.toList());
        QuestAwardList list = ModUtils.getRandomListElement(available, random);
        return list.getRandomReward(rewardTier, random);
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
        long startTime = System.currentTimeMillis();
        logger.info("Loading quest rewards");
        for (Map.Entry<ResourceLocation, JsonElement> entry : resourceLocationJsonElementMap.entrySet()) {
            ResourceLocation id = entry.getKey();
            JsonElement json = entry.getValue();
            try {
                if (!json.isJsonObject())
                    throw new JsonSyntaxException("Expected JsonObject, got " + json.getClass().getSimpleName());
                load(id, json.getAsJsonObject());
            } catch (JsonParseException exc) {
                logger.err("Unable to load quest reward {}: {}", id.toString(), exc.getMessage());
            }
        }
        logger.info("Populating reward categories");
        fillCategories();
        logger.info("Quest rewards loaded, took {}ms", System.currentTimeMillis() - startTime);
    }

    private void load(ResourceLocation id, JsonObject obj) {
        TierRange range = TierRange.fromJson(obj, MAX_TIER);
        String category = JSONUtils.getAsString(obj, "category");
        JsonObject awardObj = JSONUtils.getAsJsonObject(obj, "award");
        List<Reward> rewardList = loadRewards(awardObj, range);
        awards.put(id, new QuestAwardList(range, rewardList, category));
    }

    private List<Reward> loadRewards(JsonObject data, TierRange range) {
        List<Reward> list = new ArrayList<>();
        JsonArray itemEntries = JSONUtils.getAsJsonArray(data, "entries");
        JsonArray counts = JSONUtils.getAsJsonArray(data, "count");
        JsonArray nbts = JSONUtils.getAsJsonArray(data, "data", null);
        Item[] items = ModUtils.convertToList(itemEntries, this::itemFromJson, itemEntries.size()).toArray(new Item[0]);
        int expectedCount = range.length();
        if (counts.size() != expectedCount || (nbts != null && nbts.size() != counts.size())) {
            int invalidCount = nbts != null ? nbts.size() != expectedCount ? nbts.size() : counts.size() : counts.size();
            throw new JsonSyntaxException(String.format("Invalid argument count, expected %d, but instead got %d", expectedCount, invalidCount));
        }
        for (int i = 0; i < expectedCount; i++) {
            int tierCount = counts.get(i).getAsInt();
            CompoundNBT applyData = null;
            if (nbts != null) {
                String nbtEntry = nbts.get(i).getAsString();
                try {
                    applyData = JsonToNBT.parseTag(nbtEntry);
                } catch (CommandSyntaxException cse) {
                    throw new JsonSyntaxException("Invalid NBT: " + cse.getMessage());
                }
            }
            list.add(Reward.newAward(items, tierCount, applyData));
        }
        return list;
    }

    private Item itemFromJson(JsonElement element) throws JsonParseException {
        String path = element.getAsString();
        ResourceLocation id = new ResourceLocation(path);
        Item it = ForgeRegistries.ITEMS.getValue(id);
        if (it == Items.AIR)
            throw new JsonSyntaxException("Unknown item " + path);
        return it;
    }

    private void fillCategories() {
        byCategory.clear();
        awards.forEach((key, value) -> byCategory.computeIfAbsent(value.getCategory(), k -> new ArrayList<>()).add(key));
    }
}
