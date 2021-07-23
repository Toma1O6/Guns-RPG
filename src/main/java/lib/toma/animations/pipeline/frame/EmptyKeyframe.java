package lib.toma.animations.pipeline.frame;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.serialization.IKeyframeSerializer;
import lib.toma.animations.serialization.KeyframeSerializers;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.lang.reflect.Type;

public class EmptyKeyframe implements IKeyframe {

    private final float endpoint;
    private Vector3d pos = Vector3d.ZERO;
    private Vector3f scale = AnimationUtils.DEFAULT_SCALE_VECTOR;
    private Quaternion rot = Quaternion.ONE;

    protected EmptyKeyframe(float endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public float endpoint() {
        return endpoint;
    }

    @Override
    public Vector3d positionTarget() {
        return Vector3d.ZERO;
    }

    @Override
    public Vector3f scaleTarget() {
        return AnimationUtils.DEFAULT_SCALE_VECTOR;
    }

    @Override
    public Quaternion rotationTarget() {
        return Quaternion.ONE;
    }

    @Override
    public Vector3d initialPosition() {
        return pos;
    }

    @Override
    public Vector3f initialScale() {
        return scale;
    }

    @Override
    public Quaternion initialRotation() {
        return rot;
    }

    @Override
    public void baseOn(IKeyframe parent) {
        pos = Keyframes.getEntryPosition(parent);
        scale = Keyframes.getEntryScale(parent);
        rot = Keyframes.getEntryRotation(parent);
    }

    @Override
    public IKeyframeSerializer serializers() {
        return KeyframeSerializers.EMPTY_FRAME_SERIALIZER;
    }

    public static final class EmptyKeyframeSerializer implements IKeyframeSerializer {

        @Override
        public JsonElement serialize(IKeyframe src, Type typeOfSrc, JsonSerializationContext context) {
            return null;
        }

        @Override
        public IKeyframe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return null;
        }
    }
}
