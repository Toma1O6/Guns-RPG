package toma.config.datatypes.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import toma.config.datatypes.IConfigType;

import java.util.List;
import java.util.function.Consumer;

public class ConfigShortList extends ConfigList<Short> {

    public ConfigShortList(String name, String comment, List<Short> value, boolean fixed, Consumer<IConfigType<List<Short>>> consumer) {
        super(name, comment, value, fixed, consumer, JsonArray::add, JsonElement::getAsShort, () -> (short) 0);
    }
}
