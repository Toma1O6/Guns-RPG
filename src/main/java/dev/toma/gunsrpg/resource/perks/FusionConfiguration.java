package dev.toma.gunsrpg.resource.perks;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.PacketBuffer;

public final class FusionConfiguration {

    private final Upgrades upgrades;
    private final Swaps swaps;

    public FusionConfiguration(Upgrades upgrades, Swaps swaps) {
        this.upgrades = upgrades;
        this.swaps = swaps;
    }

    public Upgrades getUpgrades() {
        return upgrades;
    }

    public Swaps getSwaps() {
        return swaps;
    }

    public void encode(PacketBuffer buffer) {
        upgrades.encode(buffer);
        swaps.encode(buffer);
    }

    public static FusionConfiguration decode(PacketBuffer buffer) {
        return new FusionConfiguration(
                Upgrades.decode(buffer),
                Swaps.decode(buffer)
        );
    }

    public static final class Upgrades {

        private final Int2ObjectMap<Upgrade> levelUpgradeMap = new Int2ObjectOpenHashMap<>();

        public Upgrades(Upgrade[] upgrades) {
            for (Upgrade upgrade : upgrades) {
                levelUpgradeMap.put(upgrade.level, upgrade);
            }
        }

        public Upgrade getUpgradeStat(int targetLevel) {
            return levelUpgradeMap.get(targetLevel);
        }

        public void encode(PacketBuffer buffer) {
            buffer.writeInt(levelUpgradeMap.size());
            levelUpgradeMap.values().forEach(upgrade -> upgrade.encode(buffer));
        }

        public static Upgrades decode(PacketBuffer buffer) {
            int l = buffer.readInt();
            Upgrade[] upgrades = new Upgrade[l];
            for (int i = 0; i < l; i++) {
                upgrades[i] = Upgrade.decode(buffer);
            }
            return new Upgrades(upgrades);
        }
    }

    public static final class Swaps {

        private final Int2ObjectMap<Swap> countToSwapMap = new Int2ObjectOpenHashMap<>();

        public Swaps(Swap[] swaps) {
            for (Swap swap : swaps) {
                countToSwapMap.put(swap.count, swap);
            }
        }

        public Swap getSwapStat(int orbCount) {
            return countToSwapMap.get(orbCount);
        }

        public void encode(PacketBuffer buffer) {
            buffer.writeInt(countToSwapMap.size());
            countToSwapMap.values().forEach(swap -> swap.encode(buffer));
        }

        public static Swaps decode(PacketBuffer buffer) {
            Swap[] swaps = new Swap[buffer.readInt()];
            for (int i = 0; i < swaps.length; i++) {
                swaps[i] = Swap.decode(buffer);
            }
            return new Swaps(swaps);
        }
    }

    public static final class Upgrade {

        private final int level;
        private final float breakChance;
        private final int price;

        public Upgrade(int level, float breakChance, int price) {
            this.level = level;
            this.breakChance = breakChance;
            this.price = price;
        }

        public float getBreakChance() {
            return breakChance;
        }

        public int getPrice() {
            return price;
        }

        public void encode(PacketBuffer buffer) {
            buffer.writeInt(level);
            buffer.writeFloat(breakChance);
            buffer.writeInt(price);
        }

        public static Upgrade decode(PacketBuffer buffer) {
            return new Upgrade(
                    buffer.readInt(),
                    buffer.readFloat(),
                    buffer.readInt()
            );
        }
    }

    public static final class Swap {

        private final int count;
        private final int price;
        private final float chance;

        public Swap(int count, int price, float chance) {
            this.count = count;
            this.price = price;
            this.chance = chance;
        }

        public int getPrice() {
            return price;
        }

        public float getChance() {
            return chance;
        }

        public void encode(PacketBuffer buffer) {
            buffer.writeInt(count);
            buffer.writeInt(price);
            buffer.writeFloat(chance);
        }

        public static Swap decode(PacketBuffer buffer) {
            return new Swap(
                    buffer.readInt(),
                    buffer.readInt(),
                    buffer.readFloat()
            );
        }
    }
}
