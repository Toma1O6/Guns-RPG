package dev.toma.gunsrpg.resource.perks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.resource.SingleJsonFileReloadListener;
import dev.toma.gunsrpg.resource.adapter.*;
import dev.toma.gunsrpg.resource.crate.ICountFunction;
import dev.toma.gunsrpg.resource.util.functions.ComparatorFunction;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class PerkConfigurationLoader extends SingleJsonFileReloadListener {

    private static final Marker MARKER = MarkerManager.getMarker("Perks");
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(PerkConfiguration.class, new PerkConfigurationAdapter())
            .registerTypeHierarchyAdapter(ICountFunction.class, new CountFunctionAdapter(new ComparatorFunction(ComparatorFunction.BIGGER_EQUAL, -1)))
            .registerTypeAdapter(CrystalConfiguration.class, new CrystalConfigurationAdapter())
            .registerTypeAdapter(CrystalConfiguration.Spawns.class, new CrystalSpawnsAdapter())
            .registerTypeAdapter(CrystalConfiguration.Spawn.class, new CrystalSpawnAdapter())
            .registerTypeAdapter(CrystalConfiguration.Types.class, new CrystalTypesAdapter())
            .registerTypeAdapter(CrystalConfiguration.Storage.class, new CrystalStorageAdapter())
            .registerTypeAdapter(FusionConfiguration.class, new FusionConfigurationAdapter())
            .registerTypeAdapter(FusionConfiguration.Upgrades.class, new FusionUpgradesAdapter())
            .registerTypeAdapter(FusionConfiguration.Upgrade.class, new FusionUpgradeAdapter())
            .registerTypeAdapter(FusionConfiguration.Swaps.class, new FusionSwapsAdapter())
            .registerTypeAdapter(FusionConfiguration.Swap.class, new FusionSwapAdapter())
            .registerTypeAdapter(PurificationConfiguration.class, new PurificationConfigurationAdapter())
            .registerTypeAdapter(PurificationConfiguration.Entry.class, new PurificationEntryAdapter())
            .create();
    private PerkConfiguration configuration;

    public PerkConfigurationLoader() {
        super(GunsRPG.makeResource("perk_configuration.json"), GSON);
    }

    public PerkConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(PerkConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void apply(JsonElement element, IResourceManager manager, IProfiler profiler) {
        GunsRPG.log.info(MARKER, "Loading perk configuration");
        try {
            configuration = GSON.fromJson(element, PerkConfiguration.class);
            GunsRPG.log.info(MARKER, "Perk configuration has been loaded");
        } catch (JsonParseException e) {
            GunsRPG.log.error(MARKER, "Error loading perk configuration: {}", e.toString());
        }
    }
}
