package com.wf.firearms.config.settings;

import com.wf.firearms.GunsRpg;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;

@Config(id = "combat", group = GunsRpg.MOD_ID)
public final class CombatSettings {
    @Configurable
    public boolean useScriptDamage = true;

    @Configurable
    public boolean disableCritForGuns = true;

    @Configurable
    public int testDamageBonus = 0;

    @Configurable
    public int defaultBulletBase = 5;
}
