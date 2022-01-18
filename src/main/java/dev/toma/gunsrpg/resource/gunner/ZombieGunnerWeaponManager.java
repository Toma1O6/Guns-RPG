package dev.toma.gunsrpg.resource.gunner;

import com.google.gson.*;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.resource.SingleJsonFileReloadListener;
import dev.toma.gunsrpg.resource.adapter.GunnerGlobalPropertyAdapter;
import dev.toma.gunsrpg.resource.adapter.GunnerLoadoutsAdapter;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class ZombieGunnerWeaponManager extends SingleJsonFileReloadListener {

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(GunnerLoadouts.class, new GunnerLoadoutsAdapter())
            .registerTypeAdapter(GunnerGlobalProperties.class, new GunnerGlobalPropertyAdapter())
            .create();
    private static final Marker MARKER = MarkerManager.getMarker("ZombieGunnerLoadouts");
    private WeightedRandom<GunnerLoadoutInstance> randomGear;

    public ZombieGunnerWeaponManager() {
        super(GunsRPG.makeResource("loadout/gunner_settings.json"), GSON);
    }

    public GunnerLoadoutInstance getLoadout() {
        return randomGear != null ? randomGear.getRandom() : null;
    }

    @Override
    protected void apply(JsonElement data, IResourceManager manager, IProfiler profiler) {
        try {
            GunsRPG.log.info(MARKER, "Loading zombie gunner loadouts");
            if (!data.isJsonObject()) {
                throw new JsonSyntaxException("Data must be in object structure!");
            }
            JsonObject object = data.getAsJsonObject();
            GunnerLoadouts loadouts = GSON.fromJson(object, GunnerLoadouts.class);
            GunnerLoadoutInstance[] instances = loadouts.getLoadouts();
            randomGear = new WeightedRandom<>(GunnerLoadoutInstance::getWeight, instances);
            GunsRPG.log.info(MARKER, "Zombie gunner loadouts loaded, total {} entries", instances.length);
        } catch (JsonParseException jpe) {
            GunsRPG.log.error(MARKER, "Error loading zombie gunner loadouts, ", jpe);
        }
    }
}
