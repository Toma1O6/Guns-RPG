package lib.toma.animations.engine.serialization;

import com.google.gson.*;
import lib.toma.animations.api.IKeyframe;
import lib.toma.animations.Keyframes;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;

import java.lang.reflect.Type;

public class KeyframeSerializer implements JsonSerializer<IKeyframe>, JsonDeserializer<IKeyframe> {

    @Override
    public JsonElement serialize(IKeyframe src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        float endpoint = src.endpoint();
        Vector3d pos = src.positionTarget();
        Quaternion rotation = src.rotationTarget();
        object.addProperty("e", endpoint);
        if (!pos.equals(Vector3d.ZERO)) {
            object.add("pos", context.serialize(pos, Vector3d.class));
        }
        if (!rotation.equals(Quaternion.ONE)) {
            object.add("rot", context.serialize(rotation, Quaternion.class));
        }
        return object;
    }

    @Override
    public IKeyframe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonSyntaxException("Not a Json object!");
        JsonObject object = json.getAsJsonObject();
        float endpoint = JSONUtils.getAsFloat(object, "e");
        boolean positioned = object.has("pos");
        boolean rotated = object.has("rot");
        if (!positioned && !rotated) {
            return endpoint == 0.0F ? Keyframes.none() : Keyframes.wait(endpoint);
        }
        if (rotated) {
            Vector3d pos = positioned ? context.deserialize(object.get("pos"), Vector3d.class) : Vector3d.ZERO;
            Quaternion rot = context.deserialize(object.get("rot"), Quaternion.class);
            return Keyframes.keyframe(pos, rot, endpoint);
        } else {
            Vector3d pos = context.deserialize(object.get("pos"), Vector3d.class);
            return Keyframes.position(pos, endpoint);
        }
    }
}
