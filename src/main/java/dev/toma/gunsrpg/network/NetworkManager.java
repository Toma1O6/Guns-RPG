package dev.toma.gunsrpg.network;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkManager {

    private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(GunsRPG.MODID);

    public static void init() {

    }

    private static <A extends IMessage, B extends IMessage> void c_register(Class<? extends IMessageHandler<A, B>> hClass, Class<A> pClass) {
        register(hClass, pClass, Side.CLIENT);
    }

    private static <A extends IMessage, B extends IMessage> void s_register(Class<? extends IMessageHandler<A, B>> hClass, Class<A> pClass) {
        register(hClass, pClass, Side.SERVER);
    }

    private static byte id = -1;

    private static <A extends IMessage, B extends IMessage> void register(Class<? extends IMessageHandler<A, B>> hClass, Class<A> pClass, Side side) {
        INSTANCE.registerMessage(hClass, pClass, ++id, side);
    }
}
