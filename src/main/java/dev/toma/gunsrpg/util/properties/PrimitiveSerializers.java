package dev.toma.gunsrpg.util.properties;

import net.minecraft.network.PacketBuffer;

public final class PrimitiveSerializers {

    public static final IPropertySerializer<Boolean> BOOLEAN = of(PacketBuffer::writeBoolean, PacketBuffer::readBoolean);
    public static final IPropertySerializer<Integer> INT = of(PacketBuffer::writeInt, PacketBuffer::readInt);
    public static final IPropertySerializer<Float> FLOAT = of(PacketBuffer::writeFloat, PacketBuffer::readFloat);
    public static final IPropertySerializer<String> STRING = of(PacketBuffer::writeUtf, PacketBuffer::readUtf);

    public static <T> IPropertySerializer<T> of(IEncoder<T> encoder, IDecoder<T> decoder) {
        return new PrimitiveSerializer<>(encoder, decoder);
    }

    private interface IEncoder<T> {
        void encode(PacketBuffer buffer, T value);
    }

    private interface IDecoder<T> {
        T decode(PacketBuffer buffer);
    }

    private static class PrimitiveSerializer<V> implements IPropertySerializer<V> {

        private final IEncoder<V> encoder;
        private final IDecoder<V> decoder;

        PrimitiveSerializer(IEncoder<V> encoder, IDecoder<V> decoder) {
            this.encoder = encoder;
            this.decoder = decoder;
        }

        @Override
        public void encode(PacketBuffer buffer, V value) {
            encoder.encode(buffer, value);
        }

        @Override
        public V decode(PacketBuffer buffer) {
            return decoder.decode(buffer);
        }
    }
}
