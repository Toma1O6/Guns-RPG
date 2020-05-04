package dev.toma.gunsrpg.config;

import dev.toma.gunsrpg.config.client.ClientConfiguration;
import dev.toma.gunsrpg.config.world.WorldConfiguration;
import toma.config.IConfig;
import toma.config.datatypes.ConfigObject;
import toma.config.object.builder.ConfigBuilder;

public class GRPGConfig implements IConfig {

    public static ClientConfiguration client = new ClientConfiguration();
    public static WorldConfiguration world = new WorldConfiguration();

    @Override
    public ConfigObject getConfig(ConfigBuilder builder) {
        return builder
                .exec(client)
                .exec(world)
                .build();
    }
}
