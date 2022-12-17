package dev.toma.gunsrpg.resource.crate;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.toma.gunsrpg.resource.util.functions.IFunction;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

public class ConstantCount implements ICountFunction {

    public static final Codec<ConstantCount> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.optionalFieldOf("value", 1).forGetter(t -> t.value)
    ).apply(instance, ConstantCount::new));
    private final int value;

    private ConstantCount(int value) {
        this.value = value;
    }

    public static ConstantCount constant(int value) {
        return new ConstantCount(value);
    }

    @Override
    public CountFunctionType<?> getFunctionType() {
        return CountFunctionRegistry.CONST;
    }

    @Override
    public int getCount() {
        return value;
    }

    public static class Adapter implements ICountFunctionAdapter<ConstantCount> {

        @Override
        public ConstantCount deserialize(JsonObject data, IFunction range) {
            int value = JSONUtils.getAsInt(data, "value", 1);
            if (!range.canApplyFor(value)) {
                throw new JsonSyntaxException("Value is out of bounds.");
            }
            return constant(value);
        }

        @Override
        public void encode(ConstantCount function, PacketBuffer buffer) {
            buffer.writeInt(function.value);
        }

        @Override
        public ConstantCount decode(PacketBuffer buffer) {
            return new ConstantCount(buffer.readInt());
        }
    }
}
