package toma.config.datatypes.primitives;

import com.google.gson.JsonPrimitive;
import toma.config.Config;
import toma.config.datatypes.IConfigType;

import java.util.function.Consumer;

public class ConfigLong extends ConfigNumber<Long> {

    public ConfigLong(String name, String comment, long value, long min, long max, boolean slider, Consumer<IConfigType<Long>> assign) {
        super(name, comment, value, min, max, slider, JsonPrimitive::getAsLong, assign);
    }

    @Override
    public float getSliderValue() {
        Range<Long> range = getNumberRange();
        return (value() - range.getMin()) / (float) (range.getMax() - range.getMin());
    }

    @Override
    public Long getDragValue(float f) {
        Range<Long> range = getNumberRange();
        long v = range.getMin() + (long)((range.getMax() - range.getMin()) * f);
        return v > range.getMax() ? range.getMax() : v < range.getMin() ? range.getMin() : v;
    }

    @Override
    public boolean filter(char character, String string) {
        if(character == '-') {
            return string.length() == 0;
        } else return Character.isDigit(character);
    }

    @Override
    public Long parse(String input) {
        long l = getNumberRange().getMin();
        try {
            l = Long.parseLong(input);
        } catch (NumberFormatException e) {
            Config.log.error("Couldn't parse long value for input {}, using default: {}", input, l);
        }
        return Math.max(getNumberRange().getMin(), Math.min(getNumberRange().getMax(), l));
    }
}
