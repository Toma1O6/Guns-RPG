package dev.toma.gunsrpg.resource.perks;

import dev.toma.gunsrpg.resource.crate.ICountFunction;
import dev.toma.gunsrpg.util.math.WeightedRandom;

public final class CrystalConfiguration {

    private final Spawns spawns;
    private final Storage storage;

    public CrystalConfiguration(Spawns spawns, Storage storage) {
        this.spawns = spawns;
        this.storage = storage;
    }

    public Spawns getSpawns() {
        return spawns;
    }

    public Storage getStorage() {
        return storage;
    }

    public static final class Spawns {

        private final WeightedRandom<Spawn> spawns;
        private final Types typeRanges;

        public Spawns(Spawn[] spawns, Types typeRanges) {
            this.spawns = new WeightedRandom<>(Spawn::getWeight, spawns);
            this.typeRanges = typeRanges;
        }

        public Types getTypeRanges() {
            return typeRanges;
        }

        public Spawn getRandomSpawn() {
            return spawns.getRandom();
        }
    }

    public static final class Spawn {

        private final int level;
        private final int weight;

        public Spawn(int level, int weight) {
            this.level = level;
            this.weight = weight;
        }

        public int getLevel() {
            return level;
        }

        public int getWeight() {
            return weight;
        }
    }

    public static final class Types {

        private final ICountFunction buff;
        private final ICountFunction debuff;

        public Types(ICountFunction buff, ICountFunction debuff) {
            this.buff = buff;
            this.debuff = debuff;
        }

        public int getBuffCount() {
            return buff.getCount();
        }

        public int getDebuffCount() {
            return debuff.getCount();
        }
    }

    public static final class Storage {

        private final int buffCapacity;
        private final int debuffCapacity;

        public Storage(int buffCapacity, int debuffCapacity) {
            this.buffCapacity = buffCapacity;
            this.debuffCapacity = debuffCapacity;
        }

        public int getBuffCapacity() {
            return buffCapacity;
        }

        public int getDebuffCapacity() {
            return debuffCapacity;
        }

        public boolean areBuffsLimited() {
            return buffCapacity >= 0;
        }

        public boolean areDebuffsLimited() {
            return debuffCapacity >= 0;
        }
    }
}
