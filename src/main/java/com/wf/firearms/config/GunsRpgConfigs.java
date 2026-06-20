package com.wf.firearms.config;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.config.settings.AirdropSettings;
import com.wf.firearms.config.settings.CombatSettings;
import com.wf.firearms.config.settings.TaczBackendSettings;
import com.wf.firearms.config.settings.WorldSettings;
import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.format.ConfigFormats;

/**
 * Registers mod settings through the Configuration library (in-game GUI + file sync).
 * Data registries with large nested JSON (mob_spawn, gunner_loadout, weapon_levels) still
 * use dedicated loaders until migrated to nested config categories.
 */
public final class GunsRpgConfigs {
    public static AirdropSettings AIRDROP;
    public static WorldSettings WORLD;
    public static TaczBackendSettings TACZ_BACKEND;
    public static CombatSettings COMBAT;

    private GunsRpgConfigs() {}

    public static void register() {
        AIRDROP = Configuration.registerConfig(AirdropSettings.class, ConfigFormats.json()).getConfigInstance();
        WORLD = Configuration.registerConfig(WorldSettings.class, ConfigFormats.json()).getConfigInstance();
        TACZ_BACKEND = Configuration.registerConfig(TaczBackendSettings.class, ConfigFormats.json()).getConfigInstance();
        COMBAT = Configuration.registerConfig(CombatSettings.class, ConfigFormats.json()).getConfigInstance();
        GunsRpg.LOGGER.info("[gunsrpg] Configuration library settings registered");
    }
}
