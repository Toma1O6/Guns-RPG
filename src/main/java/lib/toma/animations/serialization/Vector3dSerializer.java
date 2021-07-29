package lib.toma.animations.serialization;

import com.google.gson.*;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.vector.Vector3d;

import java.lang.reflect.Type;

public class Vector3dSerializer implements JsonSerializer<Vector3d>, JsonDeserializer<Vector3d> {

    @Override
    public JsonElement serialize(Vector3d src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("x", src.x);
        object.addProperty("y", src.y);
        object.addProperty("z", src.z);
        return object;
    }

    @Override
    public Vector3d deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonSyntaxException("Not a Json object!");
        JsonObject object = json.getAsJsonObject();
        return new Vector3d(
                JSONUtils.getAsFloat(object, "x"),
                JSONUtils.getAsFloat(object, "y"),
                JSONUtils.getAsFloat(object, "z")
        );
    }
}
