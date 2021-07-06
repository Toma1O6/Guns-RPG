package dev.toma.gunsrpg.network;

import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class AbstractNetworkPacket<P> implements INetworkPacket<P> {

    protected abstract void handlePacket(NetworkEvent.Context context);

    @Override
    public final void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> handlePacket(context));
        context.setPacketHandled(true);
    }
}
