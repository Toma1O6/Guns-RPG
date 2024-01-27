package dev.toma.gunsrpg.config.world;

import dev.toma.configuration.config.Configurable;
import dev.toma.gunsrpg.util.Interval;

public final class WorldConfiguration {

    @Configurable
    @Configurable.Comment("Creates crate with player's items on death")
    public boolean createCrateOnPlayerDeath = false;

    @Configurable
    @Configurable.Comment({"Disables replacement of iron ore / gold ore to chunks", "BEWARE: This also disables extra drops from skills"})
    public boolean replaceOresAsChunks = true;

    @Configurable
    @Configurable.Range(min = -1, max = 999)
    @Configurable.Synchronized
    @Configurable.Comment({"Frequency in days specifying how often bloodmoon happens", "Value -1 means no bloodmoons"})
    public int bloodmoonCycle = 7;

    @Configurable
    @Configurable.Range(min = -1, max = 999)
    @Configurable.Comment({"Frequency in days specifying how ofter airdrops happen", "Value -1 means no airdrops"})
    public int airdropFrequency = 3;

    @Configurable
    @Configurable.Range(min = 0)
    @Configurable.Comment("Delay in ticks before crystal station can be used again")
    public int crystalStationUseCooldown = Interval.minutes(10).getTicks();

    @Configurable
    @Configurable.Comment({
            "Allows you to restrict sleeping",
            "ALWAYS - Players can never sleep",
            "BLOODMOON - Players cannot sleep during bloodmoons",
            "NEVER - Vanilla behaviour, no restrictions"
    })
    public SleepRestriction sleepRestriction = SleepRestriction.ALWAYS;

    @Configurable
    @Configurable.DecimalRange(min = 0.0, max = 0.1)
    @Configurable.Comment("Chance per chunk for loot stash generation")
    public double lootStashChance = 0.005;

    @Configurable
    @Configurable.Range(min = 20, max = 1000)
    @Configurable.Comment({"Defines how often will be new stash detection updates sent to players", "This may affect server performance when set to small number"})
    public int lootStashUpdateInterval = 100;

    @Configurable
    @Configurable.DecimalRange(min = 0.0, max = 0.9)
    @Configurable.Comment("Chance that multiple airdrops spawn")
    public double anotherAirdropSpawnChance = 0.005;

    @Configurable
    @Configurable.Comment("Mob related configurations")
    public MobConfig mobConfig = new MobConfig();

    @Configurable
    @Configurable.Comment("World generation related configs for ores etc")
    public WorldGenConfig generationConfig = new WorldGenConfig();
}