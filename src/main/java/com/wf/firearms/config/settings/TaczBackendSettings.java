package com.wf.firearms.config.settings;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.compat.tacz.TaczWeaponCatalog;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;

import java.util.LinkedHashMap;
import java.util.Map;

@Config(id = "tacz_backend", group = GunsRpg.MOD_ID)
public final class TaczBackendSettings {
    @Configurable
    @Configurable.Comment("When true and TaCZ is installed, use TaCZ for shooting/reload UX")
    public boolean useTaczAsShootingBackend = false;

    @Configurable
    public boolean disableTaczNativeCrafting = true;

    @Configurable
    public Map<String, String> weaponMap = new LinkedHashMap<>(TaczWeaponCatalog.builtinWeaponToGunMap());
}
