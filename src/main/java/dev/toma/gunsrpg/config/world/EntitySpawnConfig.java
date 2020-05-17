package dev.toma.gunsrpg.config.world;

import net.minecraft.entity.EnumCreatureType;
import toma.config.object.builder.ConfigBuilder;

public class EntitySpawnConfig {

    private final String name;
    public EnumCreatureType type;
    public int weight;
    public int min;
    public int max;

    public EntitySpawnConfig(String name, EnumCreatureType type, int weight, int min, int max) {
        this.name = name;
        this.type = type;
        this.weight = weight;
        this.min = min;
        this.max = max;
    }

    public ConfigBuilder toConfig(ConfigBuilder builder) {
        return builder.push().name(name).init()
                .addEnum(type).name("Entity type").add(t -> type = t.value())
                .addInt(weight).name("Spawn weight").range(0, 128).sliderRendering().add(t -> weight = t.value())
                .addInt(min).name("Min amount").range(0, 10).sliderRendering().add(t -> min = t.value())
                .addInt(max).name("Max amount").range(0, 10).sliderRendering().add(t -> max = t.value())
                .pop();
    }
}
