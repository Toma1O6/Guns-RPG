package dev.toma.gunsrpg.resource.perks;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.PacketBuffer;

public final class PurificationConfiguration {

    private final Int2ObjectMap<Entry> int2ObjectMap = new Int2ObjectOpenHashMap<>();

    public PurificationConfiguration(Entry[] entries) {
        for (Entry entry : entries) {
            int2ObjectMap.put(entry.count, entry);
        }
    }

    public Entry getValueForAmount(int amount) {
        return int2ObjectMap.get(amount);
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(int2ObjectMap.size());
        int2ObjectMap.values().forEach(entry -> entry.encode(buffer));
    }

    public static PurificationConfiguration decode(PacketBuffer buffer) {
        Entry[] entries = new Entry[buffer.readInt()];
        for (int i = 0; i < entries.length; i++) {
            entries[i] = Entry.decode(buffer);
        }
        return new PurificationConfiguration(entries);
    }

    public static final class Entry {

        private final int count;
        private final int price;
        private final float successChance;
        private final float breakChance;

        public Entry(int count, int price, float successChance, float breakChance) {
            this.count = count;
            this.price = price;
            this.successChance = successChance;
            this.breakChance = breakChance;
        }

        public int getPrice() {
            return price;
        }

        public float getSuccessChance() {
            return successChance;
        }

        public float getBreakChance() {
            return breakChance;
        }

        public void encode(PacketBuffer buffer) {
            buffer.writeInt(count);
            buffer.writeInt(price);
            buffer.writeFloat(successChance);
            buffer.writeFloat(breakChance);
        }

        public static Entry decode(PacketBuffer buffer) {
            return new Entry(
                    buffer.readInt(),
                    buffer.readInt(),
                    buffer.readFloat(),
                    buffer.readFloat()
            );
        }
    }
}
