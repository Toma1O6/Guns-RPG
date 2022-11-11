package dev.toma.gunsrpg.config.world;

import dev.toma.configuration.config.Configurable;
import dev.toma.gunsrpg.util.Interval;

public final class WorldConfiguration {

    @Configurable
    @Configurable.Comment("Creates crate with player's items on death")
    public boolean createCrateOnPlayerDeath = false;

    @Configurable
    @Configurable.Comment("Disables mob spawning from spawners")
    public boolean disableMobSpawners = true;

    @Configurable
    @Configurable.Range(min = 1, max = 96)
    @Configurable.Comment("Defines at which range will mobs aggro on you during bloodmoon")
    public int bloodMoonMobAgroRange = 40;

    @Configurable
    @Configurable.DecimalRange(min = 0.0, max = 256.0)
    @Configurable.Comment("Defines at which range will mobs aggro on you after shooting")
    @Configurable.Gui.NumberFormat("0.0#")
    public double shootingMobAggroRange = 28.0;

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
    @Configurable.DecimalRange(min = 0.0, max = 0.9)
    @Configurable.Comment("Chance that multiple airdrops spawn")
    public double anotherAirdropSpawnChance = 0.005;

    @Configurable
    @Configurable.Comment("Configure amethyst generation")
    public SimpleOreGenConfig amethyst = new SimpleOreGenConfig(3, 1, 16);

    @Configurable
    @Configurable.Comment("Configure black crystal generation")
    public SimpleOreGenConfig blackCrystal = new SimpleOreGenConfig(3, 1, 56);

    @Configurable
    @Configurable.Comment("Configure blue crystal generation")
    public SimpleOreGenConfig blueCrystal = new SimpleOreGenConfig(3, 1, 56);

    @Configurable
    @Configurable.Comment("Configure green crystal generation")
    public SimpleOreGenConfig greenCrystal = new SimpleOreGenConfig(3, 1, 56);

    @Configurable
    @Configurable.Comment("Configure red crystal generation")
    public SimpleOreGenConfig redCrystal = new SimpleOreGenConfig(3, 1, 56);

    @Configurable
    @Configurable.Comment("Configure white crystal generation")
    public SimpleOreGenConfig whiteCrystal = new SimpleOreGenConfig(3, 1, 56);

    @Configurable
    @Configurable.Comment("Configure yellow crystal generation")
    public SimpleOreGenConfig yellowCrystal = new SimpleOreGenConfig(3, 1, 56);

    @Configurable
    @Configurable.Comment("Configure black orb generation")
    public SimpleOreGenConfig blackOrb = new SimpleOreGenConfig(4, 1, 56);

    @Configurable
    @Configurable.Comment("Configure blue orb generation")
    public SimpleOreGenConfig blueOrb = new SimpleOreGenConfig(4, 1, 56);

    @Configurable
    @Configurable.Comment("Configure green orb generation")
    public SimpleOreGenConfig greenOrb = new SimpleOreGenConfig(4, 1, 56);

    @Configurable
    @Configurable.Comment("Configure red orb generation")
    public SimpleOreGenConfig redOrb = new SimpleOreGenConfig(4, 1, 56);

    @Configurable
    @Configurable.Comment("Configure white orb generation")
    public SimpleOreGenConfig whiteOrb = new SimpleOreGenConfig(4, 1, 56);

    @Configurable
    @Configurable.Comment("Configure yellow orb generation")
    public SimpleOreGenConfig yellowOrb = new SimpleOreGenConfig(4, 1, 56);

    @Configurable
    @Configurable.Comment("Configure spawns for mayor house in villages")
    public MayorHouseGeneratorConfig mayorHouseGen = new MayorHouseGeneratorConfig();

    @Configurable
    @Configurable.Comment("Zombie gunner spawn configuration")
    public DimensionalMobSpawnConfig zombieGunnerSpawn = new DimensionalMobSpawnConfig(25, 25, 1);

    @Configurable
    @Configurable.Comment("Grenadier spawn configuration")
    public DimensionalMobSpawnConfig grenadierSpawn = new DimensionalMobSpawnConfig(16, 20, 1);

    @Configurable
    @Configurable.Comment("Zombie knight spawn configuration")
    public DimensionalMobSpawnConfig zombieKnightSpawn = new DimensionalMobSpawnConfig(8, 12, 1);

    @Configurable
    @Configurable.Comment({"Rocket angel spawn configuration", "Note: Overworld spawn is active only during bloodmoon"})
    public DimensionalMobSpawnConfig rocketAngelSpawn = new DimensionalMobSpawnConfig(2, 2, 0);

    @Configurable
    @Configurable.Comment("Zombie nightmare spawn configuration")
    public DimensionalMobSpawnConfig zombieNightmareSpawn = new DimensionalMobSpawnConfig(1, 0, 0);
}