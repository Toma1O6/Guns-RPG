package dev.toma.gunsrpg.config;

import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.config.debuff.DebuffConfig;
import dev.toma.gunsrpg.config.gun.WeaponConfig;
import dev.toma.gunsrpg.config.world.WorldConfiguration;

@Config(id = GunsRPG.MODID)
public final class GunsrpgConfig {

    @Configurable
    @Configurable.Comment("World related configurations")
    public final WorldConfiguration world = new WorldConfiguration();

    @Configurable
    @Configurable.Comment("Debuff related configurations")
    public final DebuffConfig debuffs = new DebuffConfig();

    @Configurable
    @Configurable.Comment("Skill related configurations")
    public final SkillsConfig skills = new SkillsConfig();

    @Configurable
    @Configurable.Comment("Weapon related configurations")
    public final WeaponConfig weapon = new WeaponConfig();
}
