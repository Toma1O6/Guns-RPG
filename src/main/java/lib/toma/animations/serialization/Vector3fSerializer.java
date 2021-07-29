package lib.toma.animations.serialization;

import com.google.gson.*;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.vector.Vector3f;

import java.lang.reflect.Type;

public class Vector3fSerializer implements JsonSerializer<Vector3f>, JsonDeserializer<Vector3f> {

    @Override
    public JsonElement serialize(Vector3f src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("x", src.x());
        object.addProperty("y", src.y());
        object.addProperty("z", src.z());
        return object;
    }

    @Override
    public Vector3f deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonSyntaxException("Not a Json object!");
        JsonObject object = json.getAsJsonObject();
        return new Vector3f(
                JSONUtils.getAsFloat(object, "x"),
                JSONUtils.getAsFloat(object, "y"),
                JSONUtils.getAsFloat(object, "z")
        );
    }
}
