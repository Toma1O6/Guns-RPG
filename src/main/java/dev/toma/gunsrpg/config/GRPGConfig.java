package dev.toma.gunsrpg.config;

import dev.toma.gunsrpg.config.client.ClientConfiguration;
import dev.toma.gunsrpg.config.gun.WeaponConfig;
import dev.toma.gunsrpg.config.world.WorldConfiguration;
import toma.config.IConfig;
import toma.config.datatypes.ConfigObject;
import toma.config.object.builder.ConfigBuilder;

public class GRPGConfig implements IConfig {

    public static ClientConfiguration client = new ClientConfiguration();
    public static WorldConfiguration world = new WorldConfiguration();
    public static WeaponConfig weapon = new WeaponConfig();

    @Override
    public ConfigObject getConfig(ConfigBuilder builder) {
        return builder
                .run(client)
                .run(world)
                .run(weapon::toConfigFormat)
                .build();
    }
}
