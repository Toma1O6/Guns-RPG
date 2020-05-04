package toma.config.util;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import toma.config.Config;
import toma.config.test.ConfigImplementation;

//@Mod.EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public static void registerConfig(RegisterConfigEvent event) {
        event.register(Config.class, ConfigImplementation::new);
    }
}
