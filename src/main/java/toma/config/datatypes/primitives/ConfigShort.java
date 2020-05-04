package toma.config.datatypes.primitives;

import com.google.gson.JsonPrimitive;
import toma.config.Config;
import toma.config.datatypes.IConfigType;

import java.util.function.Consumer;

public class ConfigShort extends ConfigNumber<Short> {

    public ConfigShort(String name, String comment, short value, short min, short max, boolean asSlider, Consumer<IConfigType<Short>> assign) {
        super(name, comment, value, min, max, asSlider, JsonPrimitive::getAsShort, assign);
    }

    @Override
    public float getSliderValue() {
        Range<Short> range = getNumberRange();
        return (value() - range.getMin()) / (float) (range.getMax() - range.getMin());
    }

    @Override
    public Short getDragValue(float f) {
        Range<Short> range = getNumberRange();
        short v = (short) (range.getMin() + (range.getMax() - range.getMin()) * f);
        return v > range.getMax() ? range.getMax() : v < range.getMin() ? range.getMin() : v;
    }

    @Override
    public boolean filter(char character, String string) {
        if(character == '-') {
            return string.length() == 0;
        } else return Character.isDigit(character);
    }

    @Override
    public Short parse(String input) {
        short s = getNumberRange().getMin();
        try {
            s = Short.parseShort(input);
        } catch (NumberFormatException e) {
            Config.log.error("Couldn't parse short value for input {}, using default: {}", input, s);
        }
        return (short) Math.max(getNumberRange().getMin(), Math.min(getNumberRange().getMax(), s));
    }
}
