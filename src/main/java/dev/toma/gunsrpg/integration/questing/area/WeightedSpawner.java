package dev.toma.gunsrpg.integration.questing.area;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import dev.toma.questing.area.Area;
import dev.toma.questing.area.spawner.Spawner;
import dev.toma.questing.area.spawner.SpawnerProvider;
import dev.toma.questing.area.spawner.SpawnerType;
import dev.toma.questing.quest.Quest;
import dev.toma.questing.utils.JsonHelper;
import dev.toma.questing.utils.Utils;
import net.minecraft.util.JSONUtils;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

public class WeightedSpawner implements Spawner {

    private final WeightedRandom<WeightedEntry> values;

    public WeightedSpawner(WeightedEntry[] values) {
        this.values = new WeightedRandom<>(WeightedEntry::getWeight, values);
    }

    @Override
    public void tick(World world, Area area, Quest quest) {
        WeightedEntry entry = this.values.getRandom();
        entry.spawners.forEach(spawner -> spawner.tick(world, area, quest));
    }

    @Override
    public SpawnerType<?> getType() {
        return null;
    }

    public static final class WeightedEntry {

        private final int weight;
        private final List<Spawner> spawners;

        public WeightedEntry(int weight, SpawnerProvider<?>[] providers) {
            this.weight = weight;
            this.spawners = Utils.getProvidedSpawners(Arrays.stream(providers));
        }

        public int getWeight() {
            return weight;
        }

        static WeightedEntry fromJson(JsonElement element) {
            JsonObject object = JsonHelper.requireObject(element);
            int weight = JSONUtils.getAsInt(object, "weight", 1);
            SpawnerProvider<?>[] spawners = JsonHelper.mapArray(JSONUtils.getAsJsonArray(object, "spawners"), SpawnerProvider[]::new, SpawnerType::fromJson);
            return new WeightedEntry(weight, spawners);
        }
    }

    public static final class Serializer implements SpawnerType.Serializer<WeightedSpawner> {

        @Override
        public SpawnerProvider<WeightedSpawner> spawnerFromJson(JsonObject data) {
            JsonArray array = JSONUtils.getAsJsonArray(data, "values");
            WeightedEntry[] entries = JsonHelper.mapArray(array, WeightedEntry[]::new, WeightedEntry::fromJson);
            return () -> new WeightedSpawner(entries);
        }
    }
}
