package toma.config.datatypes.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import toma.config.datatypes.IConfigType;

import java.util.List;
import java.util.function.Consumer;

public class ConfigFloatList extends ConfigList<Float> {

    public ConfigFloatList(String name, String comment, List<Float> value, boolean fixed, Consumer<IConfigType<List<Float>>> consumer) {
        super(name, comment, value, fixed, consumer, JsonArray::add, JsonElement::getAsFloat, () -> 0.0F);
    }
}
