package toma.config.datatypes.primitives;

import com.google.gson.JsonPrimitive;
import toma.config.Config;
import toma.config.datatypes.IConfigType;

import java.util.function.Consumer;

public class ConfigByte extends ConfigNumber<Byte> {

    public ConfigByte(String name, String comment, byte value, byte min, byte max, boolean slider, Consumer<IConfigType<Byte>> load) {
        super(name, comment, value, min, max, slider, JsonPrimitive::getAsByte, load);
    }

    @Override
    public float getSliderValue() {
        Range<Byte> range = getNumberRange();
        return (value() - range.getMin()) / (float) (range.getMax() - range.getMin());
    }

    @Override
    public Byte getDragValue(float f) {
        Range<Byte> range = getNumberRange();
        return wrap((byte)(range.getMin() + (range.getMax() - range.getMin()) * f), range.getMin(), range.getMax());
    }

    @Override
    public boolean filter(char character, String string) {
        if(character == '-') {
            return string.length() == 0;
        } else return Character.isDigit(character);
    }

    @Override
    public Byte parse(String input) {
        byte b = getNumberRange().getMin();
        try {
            b = Byte.parseByte(input);
        } catch (NumberFormatException e) {
            Config.log.error("Couldn't parse byte value for input {}, using default: {}", input, b);
        }
        return (byte) Math.max(getNumberRange().getMin(), Math.min(getNumberRange().getMax(), b));
    }

    private byte wrap(byte v, byte min, byte max) {
        return v > max ? max : v < min ? min : v;
    }
}
