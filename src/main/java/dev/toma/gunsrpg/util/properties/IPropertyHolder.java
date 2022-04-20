package dev.toma.gunsrpg.util.properties;

import net.minecraft.network.PacketBuffer;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface IPropertyHolder extends IPropertyWriter, IPropertyReader {

    <V> void handleConditionally(PropertyKey<V> key, Predicate<V> condition, Consumer<V> handler);

    <V> void moveContents(IPropertyHolder holder);

    <V> void encode(PacketBuffer buffer);

    <V> void decode(PacketBuffer buffer);
}
