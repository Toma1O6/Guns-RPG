package lib.toma.animations.engine.serialization;

import com.google.gson.*;
import lib.toma.animations.engine.Vector4f;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;

public class Vector4fSerializer implements JsonSerializer<Vector4f>, JsonDeserializer<Vector4f> {

    @Override
    public JsonElement serialize(Vector4f src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("r", src.rotation());
        object.addProperty("x", src.x());
        object.addProperty("y", src.y());
        object.addProperty("z", src.z());
        return object;
    }

    @Override
    public Vector4f deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonSyntaxException("Not a Json object!");
        JsonObject object = json.getAsJsonObject();
        return new Vector4f(
                JSONUtils.getAsFloat(object, "r"),
                JSONUtils.getAsFloat(object, "x"),
                JSONUtils.getAsFloat(object, "y"),
                JSONUtils.getAsFloat(object, "z")
        );
    }
}
