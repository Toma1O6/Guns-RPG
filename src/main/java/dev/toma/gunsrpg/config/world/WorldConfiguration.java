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

    @Config.Name("Zombie gunner spawn")
    @Config.Comment("Spawn chance for zombie gunner entity")
    @Config.RequiresMcRestart
    public int zombieGunnerSpawn = 15;

    @Config.Name("Explosive skeleton spawn")
    @Config.Comment("Spawn chance for explosive skeleton entity")
    @Config.RequiresMcRestart
    public int explosiveSkeletonSpawn = 15;

    @Config.Name("Bloodmoon cycle")
    @Config.Comment({"Defines bloodmoon cycle", "Set to -1 to disable"})
    @Config.RangeInt(min = -1)
    @Config.RequiresMcRestart
    public int bloodmoonCycle = 7;

    @Config.Name("Airdrop frequency (days)")
    @Config.Comment({"Defines airdrop spawn frequency", "Set to -1 to disable"})
    @Config.RangeInt(min = -1)
    @Config.RequiresMcRestart
    public int airdropFrequency = 3;
}
