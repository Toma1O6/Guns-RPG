package dev.toma.gunsrpg.common.quests.reward;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.item.Item;
import net.minecraft.util.JSONUtils;

import java.util.Map;
import java.util.Random;

public interface IQuestRewardResolver {

    IQuestItemProvider resolve(JsonObject object, RewardProviderType type, int count, int weight, IAssemblyFunction[] functions, Map<String, QuestRewardManager.ItemGroup> itemGroups);

    class SingleItem implements IQuestRewardResolver {

        @Override
        public IQuestItemProvider resolve(JsonObject object, RewardProviderType type, int count, int weight, IAssemblyFunction[] functions, Map<String, QuestRewardManager.ItemGroup> itemGroups) {
            if (!object.has("item")) throw new JsonSyntaxException("Missing 'item' property");
            Item item = JsonHelper.resolveItem(object.get("item"));
            return new IQuestItemProvider.Impl(() -> item, count, weight, functions);
        }
    }

    class ItemList implements IQuestRewardResolver {

        @Override
        public IQuestItemProvider resolve(JsonObject object, RewardProviderType type, int count, int weight, IAssemblyFunction[] functions, Map<String, QuestRewardManager.ItemGroup> itemGroups) {
            JsonArray array = JSONUtils.getAsJsonArray(object, "list");
            Item[] items = JsonHelper.deserializeInto(array, Item[]::new, JsonHelper::resolveItem);
            Random random = new Random();
            return new IQuestItemProvider.Impl(() -> items[random.nextInt(items.length)], count, weight, functions);
        }
    }

    class ItemGroup implements IQuestRewardResolver {

        @Override
        public IQuestItemProvider resolve(JsonObject object, RewardProviderType type, int count, int weight, IAssemblyFunction[] functions, Map<String, QuestRewardManager.ItemGroup> itemGroups) {
            IAssemblyFunction[] loadedFunctions = functions;
            String id = JSONUtils.getAsString(object, "group");
            QuestRewardManager.ItemGroup group = itemGroups.get(id);
            if (group == null) throw new JsonSyntaxException("Unknown item group: " + id);
            Item[] items = group.getItems();
            IAssemblyFunction[] groupFunctions = group.getFunctions();
            if (groupFunctions != null && groupFunctions.length > 0 && loadedFunctions == null) {
                loadedFunctions = groupFunctions;
            }
            int rewardWeight = group.getWeight();
            weight = weight > 1 ? weight : rewardWeight;
            int rewardCount = JSONUtils.getAsInt(object, "count", 1);
            count = rewardCount > 1 ? rewardCount : count;
            Random random = new Random();
            return new IQuestItemProvider.Impl(() -> items[random.nextInt(items.length)], count, weight, loadedFunctions);
        }
    }
}
