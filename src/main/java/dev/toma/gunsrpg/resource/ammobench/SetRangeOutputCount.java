package dev.toma.gunsrpg.resource.ammobench;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

import java.util.Random;

public class SetRangeOutputCount implements AmmoBenchOutputCount {

    private static final Random RANDOM = new Random();
    private final int min;
    private final int max;

    public SetRangeOutputCount(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public int getCount(int count) {
        if (max - min < 1) {
            return min;
        }
        return min + RANDOM.nextInt(max - min + 1);
    }

    @Override
    public AmmoBenchOutputCountType<?> getType() {
        return AmmoBenchOutputCountType.SET;
    }

    public static final class Serializer implements AmmoBenchOutputCountType.AmmoBenchOutputCountSerializer<SetRangeOutputCount> {

        @Override
        public SetRangeOutputCount parseJson(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            int min = JSONUtils.getAsInt(object, "min");
            int max = JSONUtils.getAsInt(object, "max", min);
            if (min > max) {
                throw new JsonSyntaxException(String.format("Min value cannot be larger than max value - Min: %d, Max: %d", min, max));
            }
            return new SetRangeOutputCount(min, max);
        }

        @Override
        public void toNetwork(SetRangeOutputCount setRangeOutputCount, PacketBuffer buffer) {
            buffer.writeInt(setRangeOutputCount.min);
            buffer.writeInt(setRangeOutputCount.max);
        }

        @Override
        public SetRangeOutputCount fromNetwork(PacketBuffer buffer) {
            int min = buffer.readInt();
            int max = buffer.readInt();
            return new SetRangeOutputCount(min, max);
        }

        @Override
        public CompoundNBT toNbt(SetRangeOutputCount setRangeOutputCount) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("min", setRangeOutputCount.min);
            nbt.putInt("max", setRangeOutputCount.max);
            return nbt;
        }

        @Override
        public SetRangeOutputCount fromNbt(CompoundNBT nbt) {
            int min = nbt.getInt("min");
            int max = nbt.getInt("max");
            return new SetRangeOutputCount(min, max);
        }
    }
}
