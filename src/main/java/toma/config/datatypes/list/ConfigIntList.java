package toma.config.datatypes.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import toma.config.datatypes.IConfigType;

import java.util.List;
import java.util.function.Consumer;

public class ConfigIntList extends ConfigList<Integer> {

    public ConfigIntList(String name, String comment, List<Integer> value, boolean fixed, Consumer<IConfigType<List<Integer>>> consumer) {
        super(name, comment, value, fixed, consumer, JsonArray::add, JsonElement::getAsInt, () -> 0);
    }
}
