package toma.config.util;

import net.minecraftforge.fml.common.eventhandler.Event;
import toma.config.Config;
import toma.config.IConfig;

import java.util.function.Supplier;

public class RegisterConfigEvent extends Event {

    private Config config;

    public RegisterConfigEvent(Config config) {
        this.config = config;
    }

    public void register(Class<?> modClass, Supplier<IConfig> configSupplier) {
        config.registerConfig(modClass, configSupplier);
    }
}
