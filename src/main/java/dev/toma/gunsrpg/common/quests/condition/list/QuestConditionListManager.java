package dev.toma.gunsrpg.common.quests.condition.list;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.common.quests.condition.QuestConditionLoader;
import dev.toma.gunsrpg.util.ILogHandler;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class QuestConditionListManager extends JsonReloadListener {

    private static final Gson GSON = new Gson();
    private final Map<ResourceLocation, WeightedConditionList> map = new HashMap<>();
    private final ILogHandler logger;
    private final QuestConditionLoader conditionManager;

    public QuestConditionListManager(ILogHandler logger, QuestConditionLoader conditionManager) {
        super(GSON, "quest/conditions");
        this.logger = logger;
        this.conditionManager = conditionManager;
    }

    public WeightedConditionList getList(ResourceLocation id) {
        return ModUtils.firstNonnull(map.get(id), WeightedConditionList.EMPTY_LIST);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, IResourceManager resourceManager, IProfiler profiler) {
        logger.info("Loading condition lists");
        this.map.clear();
        try {
            for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
                ResourceLocation id = entry.getKey();
                JsonElement value = entry.getValue();
                WeightedConditionList list = WeightedConditionList.resolve(value, conditionManager);
                this.map.put(id, list);
            }
            logger.info("Condition lists loaded");
        } catch (JsonParseException e) {
            logger.err("Error loading condition lists, " + e.getMessage());
        } catch (Exception e) {
            logger.fatal("Fatal error while loading condition lists: " + e);
        }
    }
}
