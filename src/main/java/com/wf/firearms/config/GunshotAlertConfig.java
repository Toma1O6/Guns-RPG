package com.wf.firearms.config;

import com.wf.firearms.combat.WeaponClass;
import com.wf.firearms.config.settings.WorldSettings;

import java.util.Locale;
import java.util.Map;

/** Facade over {@link WorldSettings} gunshot aggro fields. */
public final class GunshotAlertConfig {
    private GunshotAlertConfig() {}

    public static void reload() {}

    private static WorldSettings cfg() {
        return GunsRpgConfigs.WORLD;
    }

    public static double baseAggroRange() {
        return cfg().shootingMobAggroRange;
    }

    public static double minAggroRange() {
        return cfg().gunshotMinAggroRange;
    }

    public static double maxAggroRange() {
        return cfg().gunshotMaxAggroRange;
    }

    public static double directAggroDistance() {
        return cfg().gunshotDirectAggroDistance;
    }

    public static boolean zombiesOnly() {
        return cfg().gunshotZombiesOnly;
    }

    public static double classNoise(WeaponClass weaponClass) {
        Map<String, Double> map = cfg().gunshotClassNoise;
        if (weaponClass == null) {
            return map.getOrDefault("other", 0.70);
        }
        String key = weaponClass.name().toLowerCase(Locale.ROOT);
        return map.getOrDefault(key, map.getOrDefault("other", 0.70));
    }

    public static double weaponNoiseOverride(String weaponKey) {
        if (weaponKey == null || weaponKey.isEmpty()) {
            return 1.0;
        }
        Map<String, Double> map = cfg().gunshotWeaponNoise;
        return map.getOrDefault(weaponKey, 1.0);
    }
}
