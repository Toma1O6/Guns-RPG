package com.wf.firearms.config;

import com.wf.firearms.config.settings.WorldSettings;

/** Facade over {@link WorldSettings} blood moon fields. */
public final class BloodmoonConfig {
    private BloodmoonConfig() {}

    public static void reload() {}

    private static WorldSettings cfg() {
        return GunsRpgConfigs.WORLD;
    }

    public static int bloodmoonCycle() {
        return cfg().bloodmoonCycle;
    }

    public static int bloodMoonMobAgroRange() {
        return cfg().bloodMoonMobAgroRange;
    }

    public static float health2xChance() {
        return cfg().health2xChance;
    }

    public static float health3xChance() {
        return cfg().health3xChance;
    }

    public static float health4xChance() {
        return cfg().health4xChance;
    }

    public static int rocketAngelSpawnWeight() {
        return cfg().rocketAngelSpawnWeight;
    }

    public static float playerDamageTakenMultiplier() {
        return cfg().playerDamageTakenMultiplier;
    }

    public static float rocketExplosionMeleeRatio() {
        return cfg().rocketExplosionMeleeRatio;
    }
}
