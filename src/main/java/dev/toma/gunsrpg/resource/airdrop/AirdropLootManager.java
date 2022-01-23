package dev.toma.gunsrpg.resource.airdrop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.resource.SingleJsonFileReloadListener;
import dev.toma.gunsrpg.resource.adapter.*;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class AirdropLootManager extends SingleJsonFileReloadListener {

    public static final Marker MARKER = MarkerManager.getMarker("AirdropLootManager");
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(AirdropLootConfiguration.class, new LootConfigurationAdapter())
            .registerTypeAdapter(LootConfigurationCategory.class, new LootConfigurationCategoryAdapter())
            .registerTypeAdapter(LootEntry.class, new LootEntryAdapter())
            .registerTypeAdapter(SlotConfiguration.class, new SlotConfigurationAdapter())
            .registerTypeHierarchyAdapter(ICountFunction.class, new CountFunctionAdapter())
            .create();

    private AirdropLootConfiguration configuration;

    public AirdropLootManager() {
        super(GunsRPG.makeResource("loot/airdrops.json"), GSON);
    }

    public IAirdropContentProvider getGeneratedContent() {
        return configuration == null ? () -> new ItemStack[0] : configuration.getRandomContent();
    }

    @Override
    protected void apply(JsonElement element, IResourceManager resourceManager, IProfiler profiler) {
        try {
            GunsRPG.log.info(MARKER, "Loading airdrop loot configuration");
            configuration = GSON.fromJson(element, AirdropLootConfiguration.class);
            GunsRPG.log.info(MARKER, "Airdrop loot configuration has been loaded");
        } catch (JsonParseException jpe) {
            GunsRPG.log.error(MARKER, "Error loading airdrop configurations, {}", jpe.toString());
        }
    }
}
