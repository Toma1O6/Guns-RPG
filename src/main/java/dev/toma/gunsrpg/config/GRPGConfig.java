package dev.toma.gunsrpg.config;

import dev.toma.configuration.api.*;
import dev.toma.configuration.api.client.IClientSettings;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.config.client.ClientConfiguration;
import dev.toma.gunsrpg.config.debuff.DebuffConfig;
import dev.toma.gunsrpg.config.gun.WeaponConfig;
import dev.toma.gunsrpg.config.world.WorldConfiguration;
import dev.toma.gunsrpg.sided.ClientSideManager;

@Config
public class GRPGConfig implements IConfigPlugin {

    public static ClientConfiguration clientConfig;
    public static WorldConfiguration worldConfig;
    public static WeaponConfig weaponConfig;
    public static DebuffConfig debuffConfig;
    public static SkillsConfig skillConfig;

    @Override
    public void buildConfig(IConfigWriter writer) {
        clientConfig = writer.writeObject(ClientConfiguration::new, "Client", "Contains all client related stuff", "Example: Overlay settings");
        worldConfig = writer.writeObject(WorldConfiguration::new, "World", "Contains world related stuff like ore gen");
        weaponConfig = writer.writeObject(WeaponConfig::new, "Weapons", "Contains all gun related stuff");
        debuffConfig = writer.writeObject(DebuffConfig::new, "Debuffs", "Allows you to blacklist specific debuffs");
        skillConfig = writer.writeObject(SkillsConfig::new, "Skills", "Allows you to modify some skills");
    }

    @Override
    public String getModID() {
        return GunsRPG.MODID;
    }
}
