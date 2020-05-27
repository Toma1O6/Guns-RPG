package dev.toma.gunsrpg.network;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.network.packet.*;
import dev.toma.gunsrpg.util.object.ShootingManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkManager {

    private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(GunsRPG.MODID);

    public static void toDimension(IMessage packet, int dimension) {
        INSTANCE.sendToDimension(packet, dimension);
    }

    public static void toClient(EntityPlayerMP user, IMessage packet) {
        INSTANCE.sendTo(packet, user);
    }

    public static void toServer(IMessage packet) {
        INSTANCE.sendToServer(packet);
    }

    public static void init() {
        c_register(CPacketUpdateCap.Handler.class, CPacketUpdateCap.class);
        c_register(CPacketParticle.Handler.class, CPacketParticle.class);
        s_register(SPacketShoot.Handler.class, SPacketShoot.class);
        s_register(SPacketRequestDataUpdate.Handler.class, SPacketRequestDataUpdate.class);
        s_register(ShootingManager.Update.Handler.class, ShootingManager.Update.class);
        s_register(SPacketAbilityUpdate.Handler.class, SPacketAbilityUpdate.class);
        s_register(SPacketSelectAmmo.Handler.class, SPacketSelectAmmo.class);
        s_register(SPacketSetAiming.Handler.class, SPacketSetAiming.class);
        s_register(SPacketSetReloading.Handler.class, SPacketSetReloading.class);
        s_register(SPacketSetShooting.Handler.class, SPacketSetShooting.class);
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
