package dev.toma.gunsrpg.resource.perks;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

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
    }
}
