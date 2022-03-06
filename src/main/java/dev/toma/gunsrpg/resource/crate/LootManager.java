package dev.toma.gunsrpg.resource.crate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.resource.adapter.*;
import dev.toma.gunsrpg.resource.util.functions.RangedFunction;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.HashMap;
import java.util.Map;

public class LootManager extends JsonReloadListener {

    public static final Marker MARKER = MarkerManager.getMarker("LootManager");
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LootConfig.class, new LootConfigurationAdapter())
            .registerTypeAdapter(LootConfigurationCategory.class, new LootConfigurationCategoryAdapter())
            .registerTypeAdapter(LootEntry.class, new LootEntryAdapter())
            .registerTypeAdapter(SlotConfiguration.class, new SlotConfigurationAdapter())
            .registerTypeHierarchyAdapter(ICountFunction.class, new CountFunctionAdapter(new RangedFunction(RangedFunction.BETWEEN_INCLUSIVE, 1, 64)))
            .create();

    private final Map<ResourceLocation, LootConfig> configurationMap = new HashMap<>();

    public LootManager() {
        super(GSON, "crate");
    }

    public ILootContentProvider getGeneratedContent(ResourceLocation configurationPath) {
        LootConfig config = configurationMap.get(configurationPath);
        if (config == null) {
            LootConfig emptyCfg = LootConfig.EMPTY;
            configurationMap.put(configurationPath, emptyCfg);
            GunsRPG.log.error(MARKER, "Unknown loot configuration {}, will use empty configuration instead", configurationPath);
            return emptyCfg.getRandomContent();
        }
        return config.getRandomContent();
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, IResourceManager manager, IProfiler profiler) {
        GunsRPG.log.info(MARKER, "Loading loot configurations");
        configurationMap.clear();
        for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
            ResourceLocation key = entry.getKey();
            try {
                LootConfig config = loadConfiguration(entry.getValue());
                configurationMap.put(key, config);
            } catch (JsonParseException e) {
                GunsRPG.log.error(MARKER, "Error loading loot configuration for {}, {}", key, e.toString());
            }
        }
        GunsRPG.log.info(MARKER, "Loot configurations have been loaded");
    }

    private LootConfig loadConfiguration(JsonElement element) throws JsonParseException {
        return GSON.fromJson(element, LootConfig.class);
    }
}
