package toma.config.datatypes.primitives;

import com.google.gson.JsonPrimitive;
import toma.config.Config;
import toma.config.datatypes.IConfigType;

import java.util.function.Consumer;

public class ConfigInteger extends ConfigNumber<Integer> {

    public ConfigInteger(String name, String comment, int defaultValue, int min, int max, boolean asSlider, Consumer<IConfigType<Integer>> assign) {
        super(name, comment, defaultValue, min, max, asSlider, JsonPrimitive::getAsInt, assign);
    }

    @Override
    public float getSliderValue() {
        Range<Integer> range = getNumberRange();
        return (value() - range.getMin()) / (float) (range.getMax() - range.getMin());
    }

    @Override
    public Integer getDragValue(float f) {
        Range<Integer> range = getNumberRange();
        int v = range.getMin() + (int)((range.getMax() - range.getMin()) * f);
        return v > range.getMax() ? range.getMax() : v < range.getMin() ? range.getMin() : v;
    }

    @Override
    public boolean filter(char character, String string) {
        if(character == '-') {
            return string.length() == 0;
        } else return Character.isDigit(character);
    }

    @Override
    public Integer parse(String input) {
        int i = getNumberRange().getMin();
        try {
            i = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            Config.log.error("Couldn't parse integer value for input {}, using default: {}", input, i);
        }
        return Math.max(getNumberRange().getMin(), Math.min(getNumberRange().getMax(), i));
    }
}
