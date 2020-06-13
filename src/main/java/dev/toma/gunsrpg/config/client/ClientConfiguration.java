package dev.toma.gunsrpg.config.client;

import dev.toma.gunsrpg.config.util.Coord2Di;
import toma.config.ConfigSubcategory;
import toma.config.object.builder.ConfigBuilder;

public class ClientConfiguration extends ConfigSubcategory {

    public Coord2Di debuffs = new Coord2Di("Debuff offset", 0, -60);
    public boolean loadAnimationTool = false;

    @Override
    public ConfigBuilder toConfigFormat(ConfigBuilder builder) {
        return builder
                .push().name("Client Config").init()
                .addBoolean(loadAnimationTool).name("Load animation tool").add(t -> loadAnimationTool = t.value())
                .run(debuffs::asConfig)
                .pop();
    }
}
