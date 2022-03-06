package dev.toma.gunsrpg.resource.perks;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

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
    }
}
