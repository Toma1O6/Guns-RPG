package dev.toma.gunsrpg.common.quests.quest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.common.quests.adapters.QuestSchemeAdapter;
import dev.toma.gunsrpg.common.quests.condition.QuestConditionLoader;
import dev.toma.gunsrpg.common.quests.condition.list.QuestConditionListManager;
import dev.toma.gunsrpg.util.ILogHandler;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class QuestManager extends JsonReloadListener {

    private static final Gson GSON = new GsonBuilder()
            .create();
    private final Map<ResourceLocation, QuestScheme<?>> quests = new HashMap<>();
    private final ILogHandler log;
    private final QuestConditionListManager listManager;
    private final QuestConditionLoader loader;
    private final QuestSchemeAdapter adapter;

    public QuestManager(ILogHandler handler, QuestConditionListManager listManager, QuestConditionLoader loader) {
        super(GSON, "quest/quests");
        this.log = handler;
        this.listManager = listManager;
        this.loader = loader;
        this.adapter = new QuestSchemeAdapter(loader);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, IResourceManager resourceManager, IProfiler profiler) {
        try {
            log.info("Loading quests");
            quests.clear();
            for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
                ResourceLocation id = entry.getKey();
                JsonElement data = entry.getValue();
                try {
                    QuestScheme<?> scheme = adapter.resolveQuestFile(id, data);
                    quests.put(id, scheme);
                } catch (JsonParseException e) {
                    log.err("Failed loading of quest: {}, invalid JSON: {}", id.toString(), e.getMessage());
                }
            }
            log.info("Quests loaded, total {} quests", quests.size());
        } catch (JsonParseException e) {
            log.err("Could not load quests, JSON exception ocurred: {}", e.toString());
        } catch (Exception e) {
            log.fatal("Fatal error ocurred while loading quests: {}", e.toString());
        }
    }
}
