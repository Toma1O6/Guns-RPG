package dev.toma.gunsrpg.integration.questing.area.spawner;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.common.init.QuestRegistry;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import dev.toma.questing.common.component.area.instance.Area;
import dev.toma.questing.common.component.area.spawner.Spawner;
import dev.toma.questing.common.component.area.spawner.SpawnerType;
import dev.toma.questing.common.quest.instance.Quest;
import dev.toma.questing.utils.Utils;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WeightedSpawner implements Spawner {

    public static final Codec<WeightedSpawner> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            WeightedRandom.Entry.codec(SpawnerType.CODEC.listOf()).listOf().fieldOf("values").forGetter(t -> Arrays.asList(t.values.getValues()))
    ).apply(instance, WeightedSpawner::new));
    private final WeightedRandom<WeightedRandom.Entry<List<Spawner>>> values;

    public WeightedSpawner(List<WeightedRandom.Entry<List<Spawner>>> values) {
        this.values = WeightedRandom.fromEntries(values);
    }

    @Override
    public void trySpawn(World world, Area area, Quest quest) {
        WeightedRandom.Entry<List<Spawner>> entry = this.values.getRandom();
        if (entry != null) {
            entry.get().forEach(spawner -> spawner.trySpawn(world, area, quest));
        }
    }

    @Override
    public SpawnerType<?> getType() {
        return QuestRegistry.WEIGHTED_SPAWNER;
    }

    @Override
    public Spawner copy() {
        List<WeightedRandom.Entry<List<Spawner>>> copied = Arrays.stream(this.values.getValues())
                .map(t -> new WeightedRandom.Entry<>(Utils.instantiate(t.getValue(), Spawner::copy), t.getWeight()))
                .collect(Collectors.toList());
        return new WeightedSpawner(copied);
    }
}
