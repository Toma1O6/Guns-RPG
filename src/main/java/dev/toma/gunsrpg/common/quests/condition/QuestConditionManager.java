package dev.toma.gunsrpg.common.quests.condition;

import com.google.gson.*;
import dev.toma.gunsrpg.util.ILogHandler;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public final class QuestConditionManager extends JsonReloadListener implements IConditionProvider {

    private static final Gson GSON = new Gson();
    private final Map<ResourceLocation, IQuestConditionProvider> conditionProviders = new HashMap<>();
    private final ILogHandler logger;

    public QuestConditionManager(ILogHandler logger) {
        super(GSON, "quest/condition");
        this.logger = logger;
    }

    @Override
    public IQuestConditionProvider getProvider(ResourceLocation id) {
        return ModUtils.getNonnullFromMap(conditionProviders, id, NoConditionProvider.NO_CONDITION);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, IResourceManager manager, IProfiler profiler) {
        logger.info("Loading quest conditions");
        conditionProviders.clear();
        try {
            for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
                ResourceLocation id = entry.getKey();
                JsonElement element = entry.getValue();
                try {
                    JsonObject object = JsonHelper.asJsonObject(element);
                    ResourceLocation providerType = new ResourceLocation(JSONUtils.getAsString(object, "type"));
                    loadProvider(id, providerType, object);
                } catch (JsonParseException e) {
                    logger.err("Couldn't load condition {}: {}", id, e.getMessage());
                }
            }
            logger.info("Quest conditions loaded");
        } catch (JsonParseException e) {
            logger.err("Couldn't load quest conditions, " + e.getMessage());
        } catch (Exception e) {
            logger.fatal("Fatal error while loading quest conditions: " + e);
        }
    }

    private <P extends IQuestConditionProvider> void loadProvider(ResourceLocation fileId, ResourceLocation typeId, JsonObject data) {
        QuestConditionProviderType<P> type = QuestConditions.getByKey(typeId);
        if (type == null) {
            throw new JsonSyntaxException("Unknown condition type: " + typeId);
        }
        IQuestConditionProviderSerializer<P> serializer = type.getSerializer();
        P provider = serializer.deserialize(type, data);
        this.conditionProviders.put(fileId, provider);
    }
}
