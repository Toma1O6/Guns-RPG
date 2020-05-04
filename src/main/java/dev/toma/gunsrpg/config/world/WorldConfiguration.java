package dev.toma.gunsrpg.config.world;

import toma.config.ConfigSubcategory;
import toma.config.object.builder.ConfigBuilder;

public class WorldConfiguration extends ConfigSubcategory {

    public SimpleOreGenConfig amethyst = new SimpleOreGenConfig("amethyst", 12, 0, 16);

    @Override
    public ConfigBuilder toConfigFormat(ConfigBuilder builder) {
        return builder
                .push().name("World config").init()
                .exec(amethyst::toConfigFormat)
                .pop();
    }
}
