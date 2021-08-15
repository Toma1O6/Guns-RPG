package lib.toma.animations.engine.serialization;

import com.google.gson.*;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.vector.Quaternion;

import java.lang.reflect.Type;

public class QuaternionSerializer implements JsonSerializer<Quaternion>, JsonDeserializer<Quaternion> {

    @Override
    public JsonElement serialize(Quaternion src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("i", src.i());
        object.addProperty("j", src.j());
        object.addProperty("k", src.k());
        object.addProperty("r", src.r());
        return object;
    }

    @Override
    public Quaternion deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonSyntaxException("Not a Json object!");
        JsonObject object = json.getAsJsonObject();
        return new Quaternion(
                JSONUtils.getAsFloat(object, "i"),
                JSONUtils.getAsFloat(object, "j"),
                JSONUtils.getAsFloat(object, "k"),
                JSONUtils.getAsFloat(object, "r")
        );
    }
}
