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

public class MulRangeOutputCount implements AmmoBenchOutputCount {

    private static final Random RANDOM = new Random();
    private final float multiplier;
    @Nullable
    private final Float chance;

    public MulRangeOutputCount(float multiplier, @Nullable Float chance) {
        this.multiplier = multiplier;
        this.chance = chance;
    }

    @Override
    public int getCount(int count) {
        if (chance != null && RANDOM.nextFloat() >= chance) {
            return count;
        }
        return (int) (count * multiplier);
    }

    @Override
    public AmmoBenchOutputCountType<?> getType() {
        return AmmoBenchOutputCountType.MUL;
    }

    public static final class Serializer implements AmmoBenchOutputCountType.AmmoBenchOutputCountSerializer<MulRangeOutputCount> {

        @Override
        public MulRangeOutputCount parseJson(JsonElement element) throws JsonParseException {
            JsonObject object = JsonHelper.asJsonObject(element);
            float multiplier = JSONUtils.getAsFloat(object, "value");
            Float chance = JsonHelper.optionally(object, "chance", JSONUtils::getAsFloat).orElse(null);
            return new MulRangeOutputCount(multiplier, chance);
        }

        @Override
        public void toNetwork(MulRangeOutputCount mulRangeOutputCount, PacketBuffer buffer) {
            buffer.writeFloat(mulRangeOutputCount.multiplier);
            buffer.writeBoolean(mulRangeOutputCount.chance != null);
            if (mulRangeOutputCount.chance != null) {
                buffer.writeFloat(mulRangeOutputCount.chance);
            }
        }

        @Override
        public MulRangeOutputCount fromNetwork(PacketBuffer buffer) {
            float value = buffer.readFloat();
            Float chance = buffer.readBoolean() ? buffer.readFloat() : null;
            return new MulRangeOutputCount(value, chance);
        }

        @Override
        public CompoundNBT toNbt(MulRangeOutputCount mulRangeOutputCount) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putFloat("value", mulRangeOutputCount.multiplier);
            if (mulRangeOutputCount.chance != null) {
                nbt.putFloat("chance", mulRangeOutputCount.chance);
            }
            return nbt;
        }

        @Override
        public MulRangeOutputCount fromNbt(CompoundNBT nbt) {
            float value = nbt.getFloat("value");
            Float chance = nbt.contains("chance") ? nbt.getFloat("chance") : null;
            return new MulRangeOutputCount(value, chance);
        }
    }
}
