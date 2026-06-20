package com.wf.firearms.config.settings;

import com.wf.firearms.GunsRpg;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;

import java.util.LinkedHashMap;
import java.util.Map;

@Config(id = "world", group = GunsRpg.MOD_ID)
public final class WorldSettings {
    @Configurable
    public int bloodmoonCycle = 15;

    @Configurable
    public int bloodMoonMobAgroRange = 64;

    @Configurable
    public float health2xChance = 0.45f;

    @Configurable
    public float health3xChance = 0.70f;

    @Configurable
    public float health4xChance = 0.85f;

    @Configurable
    public int rocketAngelSpawnWeight = 2;

    @Configurable
    public float playerDamageTakenMultiplier = 1.3f;

    @Configurable
    public float rocketExplosionMeleeRatio = 0.2f;

    @Configurable
    public double shootingMobAggroRange = 28.0;

    @Configurable
    public double gunshotMinAggroRange = 6.0;

    @Configurable
    public double gunshotMaxAggroRange = 96.0;

    @Configurable
    public double gunshotDirectAggroDistance = 16.0;

    @Configurable
    public boolean gunshotZombiesOnly = true;

    @Configurable
    public Map<String, Double> gunshotClassNoise = defaultClassNoise();

    @Configurable
    public Map<String, Double> gunshotWeaponNoise = defaultWeaponNoise();

    private static Map<String, Double> defaultClassNoise() {
        Map<String, Double> map = new LinkedHashMap<>();
        map.put("pistol", 0.50);
        map.put("smg", 0.65);
        map.put("rifle", 0.80);
        map.put("dmr", 0.95);
        map.put("sniper", 1.15);
        map.put("shotgun", 1.05);
        map.put("heavy", 1.25);
        map.put("bow", 0.40);
        map.put("other", 0.70);
        return map;
    }

    private static Map<String, Double> defaultWeaponNoise() {
        Map<String, Double> map = new LinkedHashMap<>();
        map.put("vss", 0.12);
        return map;
    }
}
