package dev.toma.gunsrpg.resource.ammobench;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

public class MulRangeOutputCount implements AmmoBenchOutputCount {

    private final float multiplier;

    public MulRangeOutputCount(float multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public int getCount(int count) {
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
            return new MulRangeOutputCount(multiplier);
        }

        @Override
        public void toNetwork(MulRangeOutputCount mulRangeOutputCount, PacketBuffer buffer) {
            buffer.writeFloat(mulRangeOutputCount.multiplier);
        }

        @Override
        public MulRangeOutputCount fromNetwork(PacketBuffer buffer) {
            return new MulRangeOutputCount(buffer.readFloat());
        }

        @Override
        public CompoundNBT toNbt(MulRangeOutputCount mulRangeOutputCount) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putFloat("value", mulRangeOutputCount.multiplier);
            return nbt;
        }

        @Override
        public MulRangeOutputCount fromNbt(CompoundNBT nbt) {
            return new MulRangeOutputCount(nbt.getFloat("value"));
        }
    }
}
