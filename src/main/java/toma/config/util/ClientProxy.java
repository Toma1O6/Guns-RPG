package toma.config.util;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import toma.config.ConfigLoader;

public class ClientProxy extends Proxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new ClientHandler());
        ConfigLoader.loadFor(Side.CLIENT);
    }
}
