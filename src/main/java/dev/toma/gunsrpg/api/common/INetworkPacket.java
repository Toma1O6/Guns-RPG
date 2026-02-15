package dev.toma.gunsrpg.api.common;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface INetworkPacket {

    void encode(PacketBuffer buffer);

    void handle(Supplier<NetworkEvent.Context> contextSupplier);
}
