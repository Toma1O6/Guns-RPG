package com.wf.firearms.config;

import com.wf.firearms.compat.TaczCompat;
import com.wf.firearms.config.settings.TaczBackendSettings;

import java.util.Map;

/** Facade over {@link TaczBackendSettings}. */
public final class TaczBackendConfig {
    private TaczBackendConfig() {}

    public static void reload() {}

    private static TaczBackendSettings cfg() {
        return GunsRpgConfigs.TACZ_BACKEND;
    }

    public static boolean useTaczShooting() {
        return cfg().useTaczAsShootingBackend && TaczCompat.isTaczLoaded();
    }

    public static boolean disableTaczNativeCrafting() {
        return cfg().disableTaczNativeCrafting;
    }

    public static Map<String, String> weaponMap() {
        return Map.copyOf(cfg().weaponMap);
    }
}
