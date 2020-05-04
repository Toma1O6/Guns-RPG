package toma.config.util;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import toma.config.ConfigLoader;

public class ServerProxy extends Proxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        ConfigLoader.loadFor(Side.SERVER);
    }
}
