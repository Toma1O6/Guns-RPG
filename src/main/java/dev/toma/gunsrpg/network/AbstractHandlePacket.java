package dev.toma.gunsrpg.network;

import net.minecraft.network.PacketBuffer;

public abstract class AbstractHandlePacket<P> extends AbstractNetworkPacket<P> {

    @Override
    public final void encode(PacketBuffer buffer) {
    }

    @Override
    public P decode(PacketBuffer buffer) {
        return recreate();
    }

    public abstract P recreate();
}
