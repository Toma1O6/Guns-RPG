package dev.toma.gunsrpg.config.world;

import toma.config.object.builder.ConfigBuilder;

public class SimpleOreGenConfig {

    protected final String name;
    public int spawns;
    public int minHeight;
    public int maxHeight;

    public SimpleOreGenConfig(String name, int spawns, int minHeight, int maxHeight) {
        this.name = name;
        this.spawns = spawns;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    public ConfigBuilder toConfigFormat(ConfigBuilder builder) {
        return builder
                .push().name(name).init()
                .addInt(spawns).name("Spawns/chunk").range(0, 128).sliderRendering().add(t -> spawns = t.value())
                .addInt(minHeight).name("Min height").range(0, 255).sliderRendering().add(t -> minHeight = t.value())
                .addInt(maxHeight).name("Max height").range(0, 255).sliderRendering().add(t -> maxHeight = t.value())
                .pop();
    }
}
