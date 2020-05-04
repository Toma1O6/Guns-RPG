package toma.config;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import toma.config.datatypes.ConfigObject;
import toma.config.object.builder.ConfigBuilder;
import toma.config.util.Proxy;
import toma.config.util.RegisterConfigEvent;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Mod(modid = Config.MODID, name = "Config Lib", version = "1.1", acceptedMinecraftVersions = "[1.12.2]")
public class Config {

    protected static final Map<String, ConfigObject> CONFIGS = new HashMap<>();

    /** Use this to register your config */
    public void registerConfig(@Nonnull Class<?> modClass, @Nonnull Supplier<IConfig> configObject) {
        Mod mod = modClass.getAnnotation(Mod.class);
        if(mod == null) {
            throw new IllegalArgumentException("Provided " + modClass.toString() + " is not valid @Mod class!");
        }
        IConfig config = configObject.get();
        ConfigObject core = new ConfigObject(mod.modid(), null);
        CONFIGS.put(mod.modid(), config.getConfig(ConfigBuilder.create(core)));
    }

    public static Map<String, ConfigObject> configs() {
        return CONFIGS;
    }

    /* ====================================================================================================== */

    public static final String MODID = "configlib";
    public static Logger log = LogManager.getLogger("CONFIG");
    @SidedProxy(clientSide = "toma.config.util.ClientProxy", serverSide = "toma.config.util.ServerProxy")
    public static Proxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.post(new RegisterConfigEvent(this));
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
