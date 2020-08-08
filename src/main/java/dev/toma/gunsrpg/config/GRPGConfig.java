package dev.toma.gunsrpg.config;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.config.client.ClientConfiguration;
import dev.toma.gunsrpg.config.gun.WeaponConfig;
import dev.toma.gunsrpg.config.world.WorldConfiguration;
import net.minecraftforge.common.config.Config;

@Config(modid = GunsRPG.MODID, name = "GunsRPG Config")
public class GRPGConfig {

    @Config.Name("Client")
    @Config.Comment({"Contains all client related stuff", "For example overlay settings"})
    public static ClientConfiguration clientConfig = new ClientConfiguration();

    @Config.Name("World")
    @Config.Comment("Contains world related stuff like ore gen")
    public static WorldConfiguration worldConfig = new WorldConfiguration();

    @Config.Name("Weapon")
    @Config.Comment("Contains all weapon related stuff - damage, velocity...")
    public static WeaponConfig weaponConfig = new WeaponConfig();
}
