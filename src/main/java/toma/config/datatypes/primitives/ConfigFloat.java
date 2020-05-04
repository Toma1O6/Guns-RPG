package toma.config.datatypes.primitives;

import com.google.gson.JsonPrimitive;
import toma.config.Config;
import toma.config.datatypes.IConfigType;

import java.util.function.Consumer;

public class ConfigFloat extends ConfigNumber<Float> {

    public ConfigFloat(String name, String comment, float def, float min, float max, boolean slider, Consumer<IConfigType<Float>> assign) {
        super(name, comment, def, min, max, slider, JsonPrimitive::getAsFloat, assign);
    }

    @Override
    public float getSliderValue() {
        Range<Float> range = getNumberRange();
        return (value() - range.getMin()) / (range.getMax() - range.getMin());
    }

    @Override
    public Float getDragValue(float f) {
        Range<Float> range = getNumberRange();
        float v = range.getMin() + (range.getMax() - range.getMin()) * f;
        return v > range.getMax() ? range.getMax() : v < range.getMin() ? range.getMin() : v;
    }

    @Override
    public boolean filter(char character, String string) {
        if(character == '-') {
            return string.length() == 0;
        } else if(character == '.') {
            return string.length() > 0 && Character.isDigit(string.charAt(string.length() - 1)) && !string.contains(".");
        } else return Character.isDigit(character);
    }

    @Override
    public Float parse(String input) {
        float f = getNumberRange().getMin();
        try {
            f = Float.parseFloat(input);
        } catch (NumberFormatException e) {
            Config.log.error("Couldn't parse float value for input {}, using default: {}", input, f);
        }
        return Math.max(getNumberRange().getMin(), Math.min(getNumberRange().getMax(), f));
    }
}
