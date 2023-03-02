package dev.toma.gunsrpg.config.world;

import dev.toma.configuration.config.Configurable;

public final class MobConfig {

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
    @Configurable.Comment("Configs for mob health buffs")
    public MobHealthBuffConfig mobHealthBuffs = new MobHealthBuffConfig();

    @Configurable
    @Configurable.Comment("Zombie gunner spawn configuration")
    public DimensionalMobSpawnConfig zombieGunnerSpawn = new DimensionalMobSpawnConfig(25, 25, 1);

    @Configurable
    @Configurable.Comment("Grenadier spawn configuration")
    public DimensionalMobSpawnConfig grenadierSpawn = new DimensionalMobSpawnConfig(16, 20, 1);

    @Configurable
    @Configurable.Comment("Zombie knight spawn configuration")
    public DimensionalMobSpawnConfig zombieKnightSpawn = new DimensionalMobSpawnConfig(5, 12, 1);

    @Configurable
    @Configurable.Comment({"Rocket angel spawn configuration", "Note: Overworld spawn is active only during bloodmoon"})
    public DimensionalMobSpawnConfig rocketAngelSpawn = new DimensionalMobSpawnConfig(2, 2, 0);

    @Configurable
    @Configurable.Comment("Zombie nightmare spawn configuration")
    public DimensionalMobSpawnConfig zombieNightmareSpawn = new DimensionalMobSpawnConfig(3, 0, 0);
}
