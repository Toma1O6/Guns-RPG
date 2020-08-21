package dev.toma.gunsrpg.config.world;

import net.minecraftforge.common.config.Config;

public class WorldConfiguration {

    @Config.Name("Amethyst Ore")
    @Config.Comment("Configure amethyst spawning")
    public SimpleOreGenConfig amethyst = new SimpleOreGenConfig(7, 0, 16);

    @Config.Name("Bloodmoon aggro range")
    @Config.Comment("Defines at which range will mobs aggro on you during bloodmoon")
    @Config.RangeInt(min = 1, max = 64)
    public int bloodMoonMobAgroRange = 50;

    @Config.Name("Rocket angel spawn chance")
    @Config.Comment({"Defines spawning chance for rocket angels", "0 = disable spawning"})
    public int rocketAngelSpawnChance = 2;
}
