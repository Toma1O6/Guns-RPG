package lib.toma.animations.engine.serialization;

import com.google.gson.*;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.EasingRegistry;
import lib.toma.animations.IEasing;
import lib.toma.animations.Keyframes;
import lib.toma.animations.api.IKeyframe;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.vector.Vector3d;

import java.lang.reflect.Type;

public class KeyframeSerializer implements JsonSerializer<IKeyframe>, JsonDeserializer<IKeyframe> {

    @Override
    public JsonElement serialize(IKeyframe src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        float endpoint = src.endpoint();
        Vector3d pos = src.positionTarget();
        Vector3d rotation = src.rotationTarget();
        object.addProperty("e", endpoint);
        object.addProperty("ease", src.getEasing().getEasingId());
        if (!pos.equals(Vector3d.ZERO)) {
            object.add("pos", context.serialize(pos, Vector3d.class));
        }
        if (!rotation.equals(Vector3d.ZERO)) {
            object.add("rot", context.serialize(rotation, Vector3d.class));
        }
        return object;
    }

    @Override
    public IKeyframe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonSyntaxException("Not a Json object!");
        JsonObject object = json.getAsJsonObject();
        float endpoint = JSONUtils.getAsFloat(object, "e");
        EasingRegistry registry = EasingRegistry.getRegistry();
        byte easingId = JSONUtils.getAsByte(object, "ease", AnimationUtils.DEFAULT_EASE_FUNC.getEasingId());
        IEasing easing = registry.getEasing(easingId);
        boolean positioned = object.has("pos");
        boolean rotated = object.has("rot");
        if (!positioned && !rotated) {
            return endpoint == 0.0F ? Keyframes.none() : Keyframes.wait(endpoint, easing);
        }
        if (rotated) {
            Vector3d pos = positioned ? context.deserialize(object.get("pos"), Vector3d.class) : Vector3d.ZERO;
            Vector3d rot = context.deserialize(object.get("rot"), Vector3d.class);
            return Keyframes.keyframe(pos, rot, easing, endpoint);
        } else {
            Vector3d pos = context.deserialize(object.get("pos"), Vector3d.class);
            return Keyframes.position(pos, easing, endpoint);
        }
    }
}
