package toma.config.datatypes.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import toma.config.datatypes.IConfigType;

import java.util.List;
import java.util.function.Consumer;

public class ConfigDoubleList extends ConfigList<Double> {

    public ConfigDoubleList(String name, String comment, List<Double> value, boolean fixed, Consumer<IConfigType<List<Double>>> consumer) {
        super(name, comment, value, fixed, consumer, JsonArray::add, JsonElement::getAsDouble, () -> 0.0D);
    }
}
