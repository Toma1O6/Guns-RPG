package dev.toma.gunsrpg.config.world;

import toma.config.ConfigSubcategory;
import toma.config.object.builder.ConfigBuilder;

public class WorldConfiguration extends ConfigSubcategory {

    public SimpleOreGenConfig amethyst = new SimpleOreGenConfig("amethyst", 8, 0, 16);
    public int bloodMoonMobAgroRange = 50;

    @Override
    public ConfigBuilder toConfigFormat(ConfigBuilder builder) {
        return builder
                .push().name("World config").init()
                .run(amethyst::toConfigFormat)
                .addInt(bloodMoonMobAgroRange).name("Mob attack range").range(35, 128).sliderRendering().add(t -> bloodMoonMobAgroRange = t.value())
                .pop();
    }
}
