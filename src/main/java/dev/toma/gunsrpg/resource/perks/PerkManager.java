package dev.toma.gunsrpg.resource.perks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.perk.Perk;
import dev.toma.gunsrpg.common.perk.PerkRegistry;
import dev.toma.gunsrpg.common.perk.PerkValueSpec;
import dev.toma.gunsrpg.resource.adapter.PerkAdapter;
import dev.toma.gunsrpg.resource.adapter.PerkValueSpecAdapter;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.Map;

public class PerkManager extends JsonReloadListener {

    private static final Marker MARKER = MarkerManager.getMarker("Perks");
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Perk.class, new PerkAdapter())
            .registerTypeAdapter(PerkValueSpec.class, new PerkValueSpecAdapter())
            .create();
    public final PerkConfigurationLoader configLoader = new PerkConfigurationLoader();

    public PerkManager() {
        super(GSON, "perks");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, IResourceManager resourceManager, IProfiler profiler) {
        GunsRPG.log.info(MARKER, "Loading perks");
        PerkRegistry registry = PerkRegistry.getRegistry();
        registry.dropRegistry();
        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            loadPerk(entry.getKey(), entry.getValue(), registry);
        }
        GunsRPG.log.info(MARKER, "Perks loaded, total {}", registry.size());
    }

    private void loadPerk(ResourceLocation id, JsonElement data, PerkRegistry registry) {
        try {
            Perk perk = GSON.fromJson(data, Perk.class);
            registry.register(id, perk);
        } catch (JsonParseException e) {
            GunsRPG.log.error(MARKER, "Error loading perk {}, {}", id, e.toString());
        }
    }
}
