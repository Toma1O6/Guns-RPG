package dev.toma.gunsrpg.common.quests.condition;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.util.ILogHandler;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class QuestConditionManager extends JsonReloadListener {

    private static final Gson GSON = new GsonBuilder().create();
    private final ILogHandler logHandler;
    private final Map<ResourceLocation, IQuestCondition> conditionMap = new HashMap<>();

    public QuestConditionManager(ILogHandler logHandler) {
        super(GSON, "quest_system/conditions");
        this.logHandler = logHandler;
    }

    public IQuestCondition getValue(ResourceLocation key) {
        return conditionMap.get(key);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, IResourceManager iResourceManager, IProfiler iProfiler) {
        long time = System.currentTimeMillis();
        conditionMap.put(GunsRPG.makeResource("empty"), EmptyCondition.empty());
        logHandler.info("Loading quest conditions");

        logHandler.info("Quest conditions loaded, took {}ms", System.currentTimeMillis() - time);
    }
}
