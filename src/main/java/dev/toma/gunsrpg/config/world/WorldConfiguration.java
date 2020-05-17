package dev.toma.gunsrpg.config.world;

import net.minecraft.entity.EnumCreatureType;
import toma.config.ConfigSubcategory;
import toma.config.object.builder.ConfigBuilder;

public class WorldConfiguration extends ConfigSubcategory {

    public SimpleOreGenConfig amethyst = new SimpleOreGenConfig("amethyst", 8, 0, 16);
    public EntitySpawnConfig explosiveSkeleton = new EntitySpawnConfig("Explosive skeleton (restart needed)", EnumCreatureType.MONSTER, 8, 1, 3);
    public int bloodMoonMobAgroRange = 50;

    @Override
    public ConfigBuilder toConfigFormat(ConfigBuilder builder) {
        return builder
                .push().name("World config").init()
                .run(amethyst::toConfigFormat)
                .addInt(bloodMoonMobAgroRange).name("Mob attack range").range(35, 128).sliderRendering().add(t -> bloodMoonMobAgroRange = t.value())
                .run(explosiveSkeleton::toConfig)
                .pop();
    }
}
