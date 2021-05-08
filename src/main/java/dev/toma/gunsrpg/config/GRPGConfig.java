package dev.toma.gunsrpg.config;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.config.client.ClientConfiguration;
import dev.toma.gunsrpg.config.debuff.DebuffConfig;
import dev.toma.gunsrpg.config.gun.WeaponConfig;
import dev.toma.gunsrpg.config.world.WorldConfiguration;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

    @Config.Name("Debuffs")
    @Config.Comment("Allows you to blacklist specific debuffs")
    public static DebuffConfig debuffConfig = new DebuffConfig();

    @Mod.EventBusSubscriber
    public static class EventHandler {

        @SubscribeEvent
        public static void onConfigUpdate(ConfigChangedEvent event) {
            if (event.getModID().equals(GunsRPG.MODID)) {
                ConfigManager.sync(GunsRPG.MODID, Config.Type.INSTANCE);
            }
        }
    }
}
