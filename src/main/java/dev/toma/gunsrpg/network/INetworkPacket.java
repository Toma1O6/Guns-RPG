package dev.toma.gunsrpg.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface INetworkPacket<P> {

    void encode(PacketBuffer buffer);

    P decode(PacketBuffer buffer);

    void handle(Supplier<NetworkEvent.Context> contextSupplier);
}
