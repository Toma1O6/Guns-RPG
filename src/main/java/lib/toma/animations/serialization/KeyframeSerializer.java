package lib.toma.animations.serialization;

import com.google.gson.*;
import lib.toma.animations.pipeline.frame.IKeyframe;
import lib.toma.animations.pipeline.frame.Keyframes;
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
            boolean rotated = !rotation.equals(Quaternion.ONE);
            if (rotated)
                object.add("rot", context.serialize(rotation, Quaternion.class));
        }
        return object;
    }

    @Override
    public IKeyframe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonSyntaxException("Not a json object!");
        JsonObject object = json.getAsJsonObject();
        float endpoint = JSONUtils.getAsFloat(object, "e");
        if (!object.has("pos")) {
            return endpoint == 0 ? Keyframes.none() : Keyframes.await(endpoint);
        } else {
            Vector3d pos = context.deserialize(object.get("pos"), Vector3d.class);
            Quaternion rot = object.has("rot") ? context.deserialize(object.get("rot"), Quaternion.class) : null;
            if (rot != null) {
                return Keyframes.keyframe(pos, rot, endpoint);
            } else {
                return Keyframes.position(pos, endpoint);
            }
        }
    }
}
