package toma.config.datatypes.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import toma.config.datatypes.IConfigType;

import java.util.List;
import java.util.function.Consumer;

public class ConfigLongList extends ConfigList<Long> {

    public ConfigLongList(String name, String comment, List<Long> value, boolean fixed, Consumer<IConfigType<List<Long>>> consumer) {
        super(name, comment, value, fixed, consumer, JsonArray::add, JsonElement::getAsLong, () -> 0L);
    }
}
