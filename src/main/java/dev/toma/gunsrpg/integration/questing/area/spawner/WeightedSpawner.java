package dev.toma.gunsrpg.integration.questing.area.spawner;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import dev.toma.questing.common.component.area.instance.Area;
import dev.toma.questing.common.component.area.spawner.Spawner;
import dev.toma.questing.common.component.area.spawner.SpawnerType;
import dev.toma.questing.common.quest.Quest;
import dev.toma.questing.utils.Utils;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WeightedSpawner implements Spawner {

    public static final Codec<WeightedSpawner> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            WeightedEntry.ENTRY_CODEC.listOf().fieldOf("values").forGetter(t -> Arrays.asList(t.values.getValues()))
    ).apply(instance, WeightedSpawner::new));
    private final WeightedRandom<WeightedEntry> values;

    public WeightedSpawner(List<WeightedEntry> values) {
        this.values = new WeightedRandom<>(WeightedEntry::getWeight, values.toArray(new WeightedEntry[0]));
    }

    @Override
    public void tick(World world, Area area, Quest quest) {
        WeightedEntry entry = this.values.getRandom();
        entry.spawners.forEach(spawner -> spawner.tick(world, area, quest));
    }

    @Override
    public SpawnerType<?> getType() {
        return QuestRegistry.WEIGHTED_SPAWNER;
    }

    @Override
    public Spawner copy() {
        List<WeightedEntry> copied = Arrays.stream(this.values.getValues())
                .map(t -> new WeightedEntry(t.weight, Utils.instantiate(t.spawners, Spawner::copy)))
                .collect(Collectors.toList());
        return new WeightedSpawner(copied);
    }

    public static final class WeightedEntry {

        public static final Codec<WeightedEntry> ENTRY_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("weight", 1).forGetter(WeightedEntry::getWeight),
                SpawnerType.CODEC.listOf().fieldOf("spawners").forGetter(t -> t.spawners)
        ).apply(instance, WeightedEntry::new));
        private final int weight;
        private final List<Spawner> spawners;

        public WeightedEntry(int weight, List<Spawner> spawners) {
            this.weight = weight;
            this.spawners = spawners;
        }

        public int getWeight() {
            return weight;
        }
    }
}
