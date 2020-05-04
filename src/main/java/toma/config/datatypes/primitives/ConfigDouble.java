package toma.config.datatypes.primitives;

import com.google.gson.JsonPrimitive;
import toma.config.Config;
import toma.config.datatypes.IConfigType;

import java.util.function.Consumer;

public class ConfigDouble extends ConfigNumber<Double> {

    public ConfigDouble(String name, String comment, double def, double min, double max, boolean slider, Consumer<IConfigType<Double>> assign) {
        super(name, comment, def, min, max, slider, JsonPrimitive::getAsDouble, assign);
    }

    @Override
    public float getSliderValue() {
        Range<Double> range = getNumberRange();
        return (float) ((value() - range.getMin()) / (range.getMax() - range.getMin()));
    }

    @Override
    public Double getDragValue(float f) {
        Range<Double> range = getNumberRange();
        double v = range.getMin() + (range.getMax() - range.getMin()) * f;
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
    public Double parse(String input) {
        double d = getNumberRange().getMin();
        try {
            d = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            Config.log.error("Couldn't parse double value for input {}, using default: {}", input, d);
        }
        return Math.max(getNumberRange().getMin(), Math.min(getNumberRange().getMax(), d));
    }
}
