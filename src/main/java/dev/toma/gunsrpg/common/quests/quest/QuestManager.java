package dev.toma.gunsrpg.common.quests.quest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.common.quests.adapters.QuestSchemeAdapter;
import dev.toma.gunsrpg.common.quests.condition.QuestConditionLoader;
import dev.toma.gunsrpg.common.quests.mayor.ReputationStatus;
import dev.toma.gunsrpg.util.ILogHandler;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public final class QuestManager extends JsonReloadListener {

    private static final Gson GSON = new GsonBuilder()
            .create();
    private final Map<ResourceLocation, QuestScheme<?>> quests = new HashMap<>();
    private final ILogHandler log;
    private final QuestSchemeAdapter adapter;

    public QuestManager(ILogHandler handler, QuestConditionLoader loader) {
        super(GSON, "quest/quests");
        this.log = handler;
        this.adapter = new QuestSchemeAdapter(loader);
    }

    @SuppressWarnings("unchecked")
    public <D extends IQuestData> QuestScheme<D> getScheme(ResourceLocation id) {
        return (QuestScheme<D>) quests.get(id);
    }

    public Set<ResourceLocation> getQuestIds() {
        return quests.keySet();
    }

    public Set<QuestScheme<?>> getSchemes(int count, float reputation, PlayerEntity player) {
        ReputationStatus status = ReputationStatus.getStatus(reputation);
        int maxTier = status.getBaseTier();
        List<QuestScheme<?>> schemePool = new LinkedList<>();
        this.quests.values().stream()
                .filter(scheme -> !scheme.isSpecialTaskQuest() && scheme.getTier() <= maxTier && scheme.canAssign(player))
                .forEach(schemePool::add);
        Set<QuestScheme<?>> results = new HashSet<>();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int poolSize = schemePool.size();
            if (poolSize == 0) {
                this.log.warning("Unable to generate full quest list due to insufficient quests, skipping generation. Generated {}/{}", results.size(), count);
                break;
            }
            int schemeIndex = random.nextInt(poolSize);
            QuestScheme<?> randomScheme = schemePool.get(schemeIndex);
            results.add(randomScheme);
            schemePool.remove(schemeIndex);
        }
        return results;
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
