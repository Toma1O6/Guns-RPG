package dev.toma.gunsrpg.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public final class SimpleRegistry<V extends Identifiable> implements Codec<V> {

    private final String registryName;
    private final Map<ResourceLocation, V> map = new HashMap<>();

    public SimpleRegistry(String registryName) {
        this.registryName = Objects.requireNonNull(registryName);
    }

    public synchronized void register(V value) {
        ResourceLocation key = Objects.requireNonNull(value.getIdentifier());
        if (map.put(key, value) != null) {
            throw new IllegalArgumentException("Duplicate registry key: " + key + " for registry " + registryName);
        }
    }

    public V get(ResourceLocation key) {
        return map.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getGeneric(ResourceLocation key) {
        return (T) map.get(key);
    }

    public Optional<V> getOptional(ResourceLocation key) {
        return Optional.ofNullable(this.get(key));
    }

    public <T> Optional<T> getGenericOptional(ResourceLocation key) {
        return Optional.ofNullable(this.getGeneric(key));
    }

    @Override
    public <T> DataResult<T> encode(V input, DynamicOps<T> ops, T prefix) {
        ResourceLocation key = input.getIdentifier();
        if (key == null) {
            return DataResult.error("Missing key for object " + input);
        }
        return ops.mergeToPrimitive(prefix, ops.createString(key.toString()));
    }

    @Override
    public <T> DataResult<Pair<V, T>> decode(DynamicOps<T> ops, T input) {
        return ResourceLocation.CODEC.decode(ops, input)
                .flatMap(pair -> {
                    V value = this.get(pair.getFirst());
                    return value != null ? DataResult.success(Pair.of(value, pair.getSecond())) : DataResult.error("Unknown " + registryName + " key " + pair.getFirst());
                });
    }
}
