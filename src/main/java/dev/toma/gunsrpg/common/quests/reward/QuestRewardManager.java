package dev.toma.gunsrpg.common.quests.reward;

import com.google.gson.*;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.quests.adapters.AssemblyFunctionAdapter;
import dev.toma.gunsrpg.resource.SingleJsonFileReloadListener;
import dev.toma.gunsrpg.resource.adapter.CountFunctionAdapter;
import dev.toma.gunsrpg.resource.crate.ICountFunction;
import dev.toma.gunsrpg.util.ILogHandler;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class QuestRewardManager extends SingleJsonFileReloadListener {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(ICountFunction.class, CountFunctionAdapter.positive())
            .registerTypeHierarchyAdapter(IAssemblyFunction.class, new AssemblyFunctionAdapter())
            .create();
    private final ILogHandler logger;
    private final Int2ObjectMap<QuestRewardList> rewardTierMap = new Int2ObjectOpenHashMap<>();

    public QuestRewardManager(ILogHandler logger) {
        super(GunsRPG.makeResource("quest/rewards.json"), GSON);
        this.logger = logger;
    }

    public QuestRewardList getTieredRewards(int tier) {
        return ModUtils.getNonnullFromMap(rewardTierMap, tier, QuestRewardList.EMPTY_LIST);
    }

    @Override
    protected void apply(JsonElement element, IResourceManager manager, IProfiler profiler) {
        logger.info("Loading quest reward list");
        rewardTierMap.clear();
        try {
            this.loadRewardsFromJson(element);
            logger.info("Quest rewards loaded");
        } catch (JsonParseException e) {
            logger.err("Quest reward loading failed: " + e.getMessage());
        } catch (Exception e) {
            logger.fatal("Fatal error while loading quest rewards: " + e);
        }
    }

    private void loadRewardsFromJson(JsonElement element) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(element);
        Map<String, ItemGroup> itemGroupMap = this.loadItemGroups(JSONUtils.getAsJsonArray(object, "itemGroups"));
        JsonArray array = JSONUtils.getAsJsonArray(object, "rewards");
        for (JsonElement tieredRewards : array) {
            loadTieredRewards(tieredRewards, itemGroupMap);
        }
    }

    private void loadTieredRewards(JsonElement element, Map<String, ItemGroup> map) throws JsonParseException {
        JsonObject object = JsonHelper.asJsonObject(element);
        int tier = JSONUtils.getAsInt(object, "tier");
        JsonArray array = JSONUtils.getAsJsonArray(object, "items");
        IQuestItemProvider[] itemProviders = JsonHelper.deserializeInto(array, IQuestItemProvider[]::new, json -> {
            JsonObject provider = JsonHelper.asJsonObject(json);
            ResourceLocation type = new ResourceLocation(JSONUtils.getAsString(provider, "type"));
            RewardProviderType providerType = RewardProviderType.getById(type);
            if (providerType == null) {
                throw new JsonSyntaxException("Unknown reward type: " + type);
            }
            IQuestRewardResolver resolver = providerType.getResolver();
            int count = JSONUtils.getAsInt(object, "count", 1);
            int weight = JSONUtils.getAsInt(object, "weight", 1);
            IAssemblyFunction[] functions = provider.has("assemblyFunctions") ? loadFunctions(JSONUtils.getAsJsonArray(provider, "assemblyFunctions")) : null;
            return resolver.resolve(provider, providerType, count, weight, functions, map);
        });
        QuestRewardList list = new QuestRewardList(itemProviders);
        rewardTierMap.put(tier, list);
    }

    private Map<String, ItemGroup> loadItemGroups(JsonArray array) throws JsonParseException {
        ItemGroup[] groups = JsonHelper.deserializeInto(array, ItemGroup[]::new, ItemGroup::resolve);
        Map<String, ItemGroup> map = new HashMap<>();
        for (ItemGroup group : groups) {
            ItemGroup prev = map.put(group.id, group);
            if (prev != null) {
                logger.warning("Duplicate item group: " + group.id);
            }
        }
        return map;
    }

    private static IAssemblyFunction[] loadFunctions(JsonArray array) throws JsonParseException {
        return JsonHelper.deserializeInto(array, IAssemblyFunction[]::new, element -> GSON.fromJson(element, IAssemblyFunction.class));
    }

    public static final class ItemGroup {

        private final String id;
        private final int weight;
        private final Item[] items;
        private final IAssemblyFunction[] functions;

        ItemGroup(String id, int weight, Item[] items, IAssemblyFunction[] functions) {
            this.id = id;
            this.weight = weight;
            this.items = items;
            this.functions = functions;
        }

        public int getWeight() {
            return weight;
        }

        public Item[] getItems() {
            return items;
        }

        public IAssemblyFunction[] getFunctions() {
            return functions;
        }

        static ItemGroup resolve(JsonElement element) {
            JsonObject object = JsonHelper.asJsonObject(element);
            String id = JSONUtils.getAsString(object, "id");
            int weight = JSONUtils.getAsInt(object, "weight", 1);
            Item[] items = JsonHelper.deserializeInto(JSONUtils.getAsJsonArray(object, "items"), Item[]::new, JsonHelper::resolveItem);
            IAssemblyFunction[] functions = object.has("assemblyFunctions") ? loadFunctions(JSONUtils.getAsJsonArray(object, "assemblyFunctions")) : null;
            return new ItemGroup(id, weight, items, functions);
        }
    }
}
