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
    public WorldConfiguration world = new WorldConfiguration();

    @Configurable
    @Configurable.Comment("Debuff related configurations")
    public DebuffConfig debuffs = new DebuffConfig();

    @Configurable
    @Configurable.Comment("Skill related configurations")
    public SkillsConfig skills = new SkillsConfig();

    @Configurable
    @Configurable.Comment("Weapon related configurations")
    public WeaponConfig weapon = new WeaponConfig();
}
