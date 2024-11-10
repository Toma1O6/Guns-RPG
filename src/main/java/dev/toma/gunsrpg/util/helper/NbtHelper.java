package dev.toma.gunsrpg.util.helper;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public final class NbtHelper {

    public static <K, V> CompoundNBT serializeMap(Map<K, V> src, Function<K, String> keySerializer, Function<V, INBT> valueSerializer) {
        CompoundNBT tag = new CompoundNBT();
        for (Map.Entry<K, V> entry : src.entrySet()) {
            String key = keySerializer.apply(entry.getKey());
            INBT value = valueSerializer.apply(entry.getValue());
            tag.put(key, value);
        }
        return tag;
    }

    public static <V> ListNBT serializeCollection(Collection<V> src, Function<V, INBT> valueSerializer) {
        ListNBT listNBT = new ListNBT();
        for (V v : src) {
            listNBT.add(valueSerializer.apply(v));
        }
        return listNBT;
    }

    public static <K, V, M extends Map<K, V>> M deserializeMap(M dest, CompoundNBT src, Function<String, K> keyDeserializer, Function<INBT, V> valueDeserializer) {
        dest.clear();
        Set<String> keySet = src.getAllKeys();
        for (String objKey : keySet) {
            K key = keyDeserializer.apply(objKey);
            INBT value = src.get(objKey);
            dest.put(key, valueDeserializer.apply(value));
        }
        return dest;
    }

    public static <V, C extends Collection<V>> C deserializeCollection(C dest, ListNBT src, Function<INBT, V> deserializer) {
        dest.clear();
        for (INBT inbt : src) {
            dest.add(deserializer.apply(inbt));
        }
        return dest;
    }
}
