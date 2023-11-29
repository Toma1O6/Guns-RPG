package dev.toma.gunsrpg.resource.ammobench;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

import javax.annotation.Nullable;
import java.util.Random;

public class AddRangeOutputCount implements AmmoBenchOutputCount {

    private static final Random RANDOM = new Random();
    private final int value;
    @Nullable
    private final Float chance;

    public AddRangeOutputCount(int value, @Nullable Float chance) {
        this.value = value;
        this.chance = chance;
    }

    @Override
    public int getCount(int count) {
        if (chance != null && RANDOM.nextFloat() >= chance) {
            return count;
        }
        return count + value;
    }

    @Override
    public AmmoBenchOutputCountType<?> getType() {
        return AmmoBenchOutputCountType.ADD;
    }

    public static final class Serializer implements AmmoBenchOutputCountType.AmmoBenchOutputCountSerializer<AddRangeOutputCount> {

        @Override
        public AddRangeOutputCount parseJson(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            int value = JSONUtils.getAsInt(object, "value");
            Float chance = JsonHelper.optionally(object, "chance", JSONUtils::getAsFloat).orElse(null);
            return new AddRangeOutputCount(value, chance);
        }

        @Override
        public void toNetwork(AddRangeOutputCount addRangeOutputCount, PacketBuffer buffer) {
            buffer.writeInt(addRangeOutputCount.value);
            buffer.writeBoolean(addRangeOutputCount.chance != null);
            if (addRangeOutputCount.chance != null) {
                buffer.writeFloat(addRangeOutputCount.chance);
            }
        }

        @Override
        public AddRangeOutputCount fromNetwork(PacketBuffer buffer) {
            int value = buffer.readInt();
            Float chance = buffer.readBoolean() ? buffer.readFloat() : null;
            return new AddRangeOutputCount(value, chance);
        }

        @Override
        public CompoundNBT toNbt(AddRangeOutputCount addRangeOutputCount) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("value", addRangeOutputCount.value);
            if (addRangeOutputCount.chance != null) {
                nbt.putFloat("chance", addRangeOutputCount.chance);
            }
            return nbt;
        }

        @Override
        public AddRangeOutputCount fromNbt(CompoundNBT nbt) {
            int value = nbt.getInt("value");
            Float chance = nbt.contains("chance") ? nbt.getFloat("chance") : null;
            return new AddRangeOutputCount(value, chance);
        }
    }
}
