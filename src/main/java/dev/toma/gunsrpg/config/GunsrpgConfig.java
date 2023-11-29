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

    @Configurable
    @Configurable.Comment("Quest related configurations")
    public QuestConfig quests = new QuestConfig();

    @Configurable
    @Configurable.Comment("Control whether new players will get starter package")
    public boolean giveStartingPackage = true;

    @Configurable
    @Configurable.Comment("Control whether welcome book is given to new players")
    public boolean giveWelcomeBook = true;
}
