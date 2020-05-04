package toma.config.datatypes.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import toma.config.datatypes.IConfigType;

import java.util.List;
import java.util.function.Consumer;

public class ConfigBoolList extends ConfigList<Boolean> {

    public ConfigBoolList(String name, String comment, List<Boolean> value, boolean fixed, Consumer<IConfigType<List<Boolean>>> consumer) {
        super(name, comment, value, fixed, consumer, JsonArray::add, JsonElement::getAsBoolean, () -> false);
    }
}
