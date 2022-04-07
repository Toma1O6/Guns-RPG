package dev.toma.gunsrpg.util.properties;

import net.minecraft.network.PacketBuffer;

public interface IPropertySerializer<V> {

    void encode(PacketBuffer buffer, V value);

    V decode(PacketBuffer buffer);
}
