package dev.toma.gunsrpg;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.PlayerDataStorage;
import dev.toma.gunsrpg.sided.SideManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = GunsRPG.MODID, name = "Guns RPG", version = "0.1-dev", acceptedMinecraftVersions = "[1.12.2]")
public class GunsRPG {

    public static final String MODID = "gunsrpg";
    public static Logger log = LogManager.getLogger();
    @SidedProxy(modId = MODID, clientSide = "dev.toma.gunsrpg.sided.ClientSideManager", serverSide = "dev.toma.gunsrpg.sided.ServerSideManager")
    public static SideManager sideManager;
    @Mod.Instance
    public static GunsRPG modInstance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        sideManager.preInit(event);
        CapabilityManager.INSTANCE.register(PlayerData.class, new PlayerDataStorage(), PlayerDataFactory::new);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        sideManager.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        sideManager.postInit(event);
    }

    public static ResourceLocation makeResource(String path) {
        return new ResourceLocation(MODID, path);
    }
}
