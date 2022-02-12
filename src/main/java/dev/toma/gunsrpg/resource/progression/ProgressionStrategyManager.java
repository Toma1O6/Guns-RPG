package dev.toma.gunsrpg.resource.progression;

import com.google.gson.*;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidator;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidatorFactory;
import dev.toma.gunsrpg.common.skills.core.TransactionValidatorRegistry;
import dev.toma.gunsrpg.resource.SingleJsonFileReloadListener;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.*;

public final class ProgressionStrategyManager extends SingleJsonFileReloadListener {

    public static final Marker MARKER = MarkerManager.getMarker("Levels");
    private static final Gson GSON = new GsonBuilder()
            .registerTypeHierarchyAdapter(IProgressionStrategy.class, new ProgressionStrategy.Adapter())
            .create();
    private final Map<ITransactionValidator, IProgressionStrategy> definedStrategies = new IdentityHashMap<>();

    public ProgressionStrategyManager() {
        super(GunsRPG.makeResource("leveling_strategy.json"), GSON);
    }

    public IProgressionStrategy getStrategy(ITransactionValidator validator) {
        return definedStrategies.get(validator);
    }

    @Override
    protected void apply(JsonElement json, IResourceManager resourceManager, IProfiler profiler) {
        try {
            GunsRPG.log.info(MARKER, "Loading leveling strategies");
            definedStrategies.clear();
            load(json);
            GunsRPG.log.info(MARKER, "Leveling strategies have been loaded");
        } catch (JsonParseException exception) {
            GunsRPG.log.error(MARKER, "Exception occurred while loading leveling strategies, aborting... {}", exception.toString());
        }
    }

    private void load(JsonElement element) {
        JsonObject object = JsonHelper.asJsonObject(element);
        Set<String> keyDefinitions = loadKeys(JSONUtils.getAsJsonArray(object, "types"));
        Map<String, Target> targetDefinitions = loadTargets(JSONUtils.getAsJsonArray(object, "targets"));
        ensureAllTargetsDefined(keyDefinitions, targetDefinitions);
        JsonObject definitions = JSONUtils.getAsJsonObject(object, "definitions");
        for (String key : keyDefinitions) {
            JsonObject strategyJson = JSONUtils.getAsJsonObject(definitions, key);
            IProgressionStrategy strategy = GSON.fromJson(strategyJson, IProgressionStrategy.class);
            Target target = targetDefinitions.get(key);
            ITransactionValidatorFactory<?, ?> factory = TransactionValidatorRegistry.getValidatorFactory(target.type);
            for (ResourceLocation location : target.identifiers) {
                JsonElement data = JsonHelper.toSimpleJson(location);
                ITransactionValidator validator = TransactionValidatorRegistry.getTransactionValidator(factory, data);
                if (validator == null) {
                    throw new JsonSyntaxException("Invalid identifier for type " + target.type + ", no match found for " + location + " identifier.");
                }
                definedStrategies.put(validator, strategy);
            }
        }
    }

    private void ensureAllTargetsDefined(Set<String> keys, Map<String, Target> targets) {
        for (String key : keys) {
            Target target = targets.get(key);
            if (target == null) {
                throw new JsonSyntaxException("Missing target definition for " + key);
            }
        }
    }

    private Map<String, Target> loadTargets(JsonArray array) {
        Map<String, Target> targets = new HashMap<>();
        for (JsonElement element : array) {
            JsonObject object = JsonHelper.asJsonObject(element);
            Target target = Target.resolveJson(object);
            targets.put(target.targetId, target);
        }
        return targets;
    }

    private Set<String> loadKeys(JsonArray array) {
        Set<String> strings = new HashSet<>(array.size());
        for (JsonElement element : array) {
            strings.add(element.getAsString());
        }
        return strings;
    }

    static class Target {
        private String targetId;
        private ResourceLocation type;
        private ResourceLocation[] identifiers;

        public static Target resolveJson(JsonObject object) {
            Target target = new Target();
            target.targetId = JSONUtils.getAsString(object, "strategy");
            target.type = new ResourceLocation(JSONUtils.getAsString(object, "key"));
            target.identifiers = resolveIdentifiers(JSONUtils.getAsJsonArray(object, "identification"));
            return target;
        }

        private static ResourceLocation[] resolveIdentifiers(JsonArray array) {
            ResourceLocation[] locations = new ResourceLocation[array.size()];
            int index = 0;
            for (JsonElement element : array) {
                locations[index++] = element.isJsonNull() ? null : new ResourceLocation(element.getAsString());
            }
            return locations;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Target target = (Target) o;
            return targetId.equals(target.targetId);
        }

        @Override
        public int hashCode() {
            return targetId.hashCode();
        }
    }
}
