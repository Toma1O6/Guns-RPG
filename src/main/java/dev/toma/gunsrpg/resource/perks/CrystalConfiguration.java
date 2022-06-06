package dev.toma.gunsrpg.resource.perks;

import dev.toma.gunsrpg.resource.crate.CountFunctionRegistry;
import dev.toma.gunsrpg.resource.crate.ICountFunction;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import net.minecraft.network.PacketBuffer;

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

    public void encode(PacketBuffer buffer) {
        spawns.encode(buffer);
        storage.encode(buffer);
    }

    public static CrystalConfiguration decode(PacketBuffer buffer) {
        return new CrystalConfiguration(
                Spawns.decode(buffer),
                Storage.decode(buffer)
        );
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

        public void encode(PacketBuffer buffer) {
            Spawn[] spawns = this.spawns.getValues();
            int l = spawns.length;
            buffer.writeInt(l);
            for (Spawn spawn : spawns) {
                spawn.encode(buffer);
            }
            typeRanges.encode(buffer);
        }

        public static Spawns decode(PacketBuffer buffer) {
            Spawn[] spawns = new Spawn[buffer.readInt()];
            for (int i = 0; i < spawns.length; i++) {
                spawns[i] = Spawn.decode(buffer);
            }
            Types types = Types.decode(buffer);
            return new Spawns(spawns, types);
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

        public void encode(PacketBuffer buffer) {
            buffer.writeInt(level);
            buffer.writeInt(weight);
        }

        public static Spawn decode(PacketBuffer buffer) {
            return new Spawn(
                    buffer.readInt(),
                    buffer.readInt()
            );
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

        public void encode(PacketBuffer buffer) {
            CountFunctionRegistry.encode(buff, buffer);
            CountFunctionRegistry.encode(debuff, buffer);
        }

        public static Types decode(PacketBuffer buffer) {
            return new Types(
                    CountFunctionRegistry.decode(buffer),
                    CountFunctionRegistry.decode(buffer)
            );
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

        public void encode(PacketBuffer buffer) {
            buffer.writeInt(buffCapacity);
            buffer.writeInt(debuffCapacity);
        }

        public static Storage decode(PacketBuffer buffer) {
            return new Storage(
                    buffer.readInt(),
                    buffer.readInt()
            );
        }
    }
}
