package dev.toma.gunsrpg.config.world;

import toma.config.object.builder.ConfigBuilder;

public class VeinOreGenConfig extends SimpleOreGenConfig {

    public int veinSize;

    public VeinOreGenConfig(String name, int spawns, int minHeight, int maxHeight, int size) {
        super(name, spawns, minHeight, maxHeight);
        this.veinSize = size;
    }

    @Override
    public ConfigBuilder toConfigFormat(ConfigBuilder builder) {
        return builder
                .push().name(name).init()
                .addInt(spawns).name("Spawns/chunk").range(0, 128).sliderRendering().add(t -> spawns = t.value())
                .addInt(minHeight).name("Min height").range(0, 255).sliderRendering().add(t -> minHeight = t.value())
                .addInt(maxHeight).name("Max height").range(0, 255).sliderRendering().add(t -> maxHeight = t.value())
                .addInt(veinSize).name("Ore count").range(1, 32).sliderRendering().add(t -> veinSize = t.value())
                .pop();
    }
}
