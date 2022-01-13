package dev.toma.gunsrpg.resource.gunner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.resource.SingleJsonFileReloadListener;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class ZombieGunnerWeaponManager extends SingleJsonFileReloadListener {

    private static final Gson GSON = new GsonBuilder().create();
    private static final Marker MARKER = MarkerManager.getMarker("ZombieGunnerLoadouts");
    private WeightedRandom<ZombieGunnerLoadout> randomGear;

    public ZombieGunnerWeaponManager() {
        super(GunsRPG.makeResource("loadout/gunner_settings.json"), GSON);
    }

    public ZombieGunnerLoadout getLoadout() {
        return randomGear != null ? randomGear.getRandom() : null;
    }

    @Override
    protected void apply(JsonElement data, IResourceManager manager, IProfiler profiler) {
       /* if (!data.isJsonObject()) {
            throw new JsonSyntaxException("Data must be in array structure!");
        }
        JsonObject object = data.getAsJsonObject();
        List<ZombieGunnerLoadout> list = new ArrayList<>();
        for (JsonElement element : array) {
            try {
                ZombieGunnerLoadout loadout = GSON.fromJson(element, ZombieGunnerLoadout.class); // TODO create type adapter
                list.add(loadout);
            } catch (JsonSyntaxException e) {
                GunsRPG.log.error(MARKER, "Error processing element ({}), error occurred: {}", element.toString(), e.toString());
            }
        }
        randomGear = new WeightedRandom<>(ZombieGunnerLoadout::getWeight, list.toArray(new ZombieGunnerLoadout[0]));*/
    }
}
