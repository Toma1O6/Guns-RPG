package dev.toma.gunsrpg.common.quests.condition;

import com.google.gson.*;
import dev.toma.gunsrpg.util.ILogHandler;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ConditionListManager extends JsonReloadListener {

    private static final Gson GSON = new GsonBuilder().create();
    private final ILogHandler handler;
    private final QuestConditionManager manager;
    private final Map<ResourceLocation, ConditionList> conditionLists = new HashMap<>();

    public ConditionListManager(ILogHandler logHandler, QuestConditionManager questConditionManager) {
        super(GSON, "quest_system/lists");
        this.handler = logHandler;
        this.manager = questConditionManager;
    }

    public ConditionList getValue(ResourceLocation key) {
        return conditionLists.get(key);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, IResourceManager iResourceManager, IProfiler iProfiler) {
        long time = System.currentTimeMillis();
        handler.info("Loading condition lists");
        conditionLists.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : resourceLocationJsonElementMap.entrySet()) {
            ResourceLocation location = entry.getKey();
            JsonElement element = entry.getValue();
            try {
                if (!element.isJsonObject())
                    throw new JsonSyntaxException("Expected JsonObject, got " + element.getClass().getSimpleName());
                load(location, element.getAsJsonObject());
            } catch (JsonParseException jpe) {
                handler.err("Error loading condition list {}: {}", location, jpe.getMessage());
            }
        }
        handler.info("Condition lists loaded, took {}ms", System.currentTimeMillis() - time);
    }

    private void load(ResourceLocation location, JsonObject data) throws JsonParseException {
        JsonArray array = JSONUtils.getAsJsonArray(data, "list");
        ConditionList.ConditionEntry[] entryArray = new ConditionList.ConditionEntry[array.size()];
        int i = 0;
        for (JsonElement element : array) {
            if (!element.isJsonObject())
                throw new JsonSyntaxException("Expected JsonObject, got " + element.getClass().getSimpleName());
            JsonObject entry = element.getAsJsonObject();
            int weight = JSONUtils.getAsInt(entry, "weight", 1);
            String id = JSONUtils.getAsString(entry, "condition");
            ResourceLocation conditionID = new ResourceLocation(id);
            IQuestCondition condition = manager.getValue(conditionID);
            if (condition == null) {
                throw new JsonSyntaxException("Unknown condition " + id);
            }
            entryArray[i] = ConditionList.ConditionEntry.of(weight, condition);
            ++i;
        }
        conditionLists.put(location, new ConditionList(location, entryArray));
    }
}
