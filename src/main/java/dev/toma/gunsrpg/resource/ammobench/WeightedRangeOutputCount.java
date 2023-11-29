package dev.toma.gunsrpg.resource.ammobench;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import dev.toma.gunsrpg.util.math.WeightedRandom;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.common.util.Constants;

public class WeightedRangeOutputCount implements AmmoBenchOutputCount {

    private final WeightedRandom<Entry> weightedRandom;

    public WeightedRangeOutputCount(Entry[] values) {
        this.weightedRandom = new WeightedRandom<>(Entry::getWeight, values);
    }

    @Override
    public int getCount(int count) {
        Entry entry = weightedRandom.getRandom();
        if (entry != null) {
            return entry.modifier.getCount(count);
        }
        return count;
    }

    @Override
    public AmmoBenchOutputCountType<?> getType() {
        return AmmoBenchOutputCountType.WEIGHTED;
    }

    public static final class Serializer implements AmmoBenchOutputCountType.AmmoBenchOutputCountSerializer<WeightedRangeOutputCount> {

        @Override
        public WeightedRangeOutputCount parseJson(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            JsonArray entriesJson = JSONUtils.getAsJsonArray(object, "entries");
            Entry[] entries = new Entry[entriesJson.size()];
            int i = 0;
            for (JsonElement entryElement : entriesJson) {
                JsonObject data = JsonHelper.asJsonObject(entryElement);
                int weight = JSONUtils.getAsInt(data, "weight", 1);
                AmmoBenchOutputCount modifier = AmmoBenchOutputCountType.parseJson(JSONUtils.getAsJsonObject(data, "entry"));
                entries[i++] = new Entry(weight, modifier);
            }
            return new WeightedRangeOutputCount(entries);
        }

        @Override
        public void toNetwork(WeightedRangeOutputCount weightedRangeOutputCount, PacketBuffer buffer) {
            Entry[] entries = weightedRangeOutputCount.weightedRandom.getValues();
            buffer.writeInt(entries.length);
            for (Entry entry : entries) {
                buffer.writeInt(entry.weight);
                AmmoBenchOutputCountType.toNetwork(entry.modifier, buffer);
            }
        }

        @Override
        public WeightedRangeOutputCount fromNetwork(PacketBuffer buffer) {
            int count = buffer.readInt();
            Entry[] entries = new Entry[count];
            for (int i = 0; i < count; i++) {
                entries[i] = new Entry(buffer.readInt(), AmmoBenchOutputCountType.fromNetwork(buffer));
            }
            return new WeightedRangeOutputCount(entries);
        }

        @Override
        public CompoundNBT toNbt(WeightedRangeOutputCount weightedRangeOutputCount) {
            CompoundNBT nbt = new CompoundNBT();
            ListNBT list = new ListNBT();
            Entry[] entries = weightedRangeOutputCount.weightedRandom.getValues();
            for (Entry entry : entries) {
                CompoundNBT tag = new CompoundNBT();
                tag.putInt("weight", entry.weight);
                tag.put("entry", AmmoBenchOutputCountType.toNbt(entry.modifier));
                list.add(tag);
            }
            nbt.put("entries", list);
            return nbt;
        }

        @Override
        public WeightedRangeOutputCount fromNbt(CompoundNBT nbt) {
            ListNBT entriesNbt = nbt.getList("entries", Constants.NBT.TAG_COMPOUND);
            Entry[] entries = new Entry[entriesNbt.size()];
            for (int i = 0; i < entriesNbt.size(); i++) {
                CompoundNBT tag = entriesNbt.getCompound(i);
                entries[i] = new Entry(
                        tag.getInt("weight"),
                        AmmoBenchOutputCountType.fromNbt(tag.getCompound("entry"))
                );
            }
            return new WeightedRangeOutputCount(entries);
        }
    }

    private static final class Entry {

        private final int weight;
        private final AmmoBenchOutputCount modifier;

        public Entry(int weight, AmmoBenchOutputCount modifier) {
            this.weight = weight;
            this.modifier = modifier;
        }

        public int getWeight() {
            return weight;
        }
    }
}
