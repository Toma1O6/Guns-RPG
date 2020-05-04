package toma.config.datatypes.list;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import toma.config.datatypes.IConfigType;

import java.util.List;
import java.util.function.Consumer;

public class ConfigByteList extends ConfigList<Byte> {

    public ConfigByteList(String name, String comment, List<Byte> value, boolean fixed, Consumer<IConfigType<List<Byte>>> consumer) {
        super(name, comment, value, fixed, consumer, JsonArray::add, JsonElement::getAsByte, () -> (byte) 0);
    }
}
