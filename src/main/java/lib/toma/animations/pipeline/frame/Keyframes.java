package lib.toma.animations.pipeline.frame;

import lib.toma.animations.AnimationUtils;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import org.apache.commons.lang3.tuple.Pair;

public class Keyframes {

    private static final IKeyframe NULL_FRAME = new EmptyKeyframe(0.0F);

    public static Vector3d getEntryPosition(IKeyframe parent) {
        return parent.initialPosition().add(parent.positionTarget());
    }

    public static Vector3f getEntryScale(IKeyframe parent) {
        Vector3f vec3f = parent.initialScale().copy();
        vec3f.add(parent.scaleTarget());
        return vec3f;
    }

    public static Quaternion getEntryRotation(IKeyframe parent) {
        Pair<Float, Vector3f> initialRot = AnimationUtils.getVectorWithRotation(parent.initialRotation());
        Pair<Float, Vector3f> targetRot = AnimationUtils.getVectorWithRotation(parent.rotationTarget());
        float newRot = initialRot.getLeft() + targetRot.getLeft();
        Vector3f total = initialRot.getRight();
        total.add(targetRot.getRight());
        return new Quaternion(total, newRot, true);
    }

    public static Quaternion mul(Quaternion target, float value) {
        float i = target.i();
        float j = target.j();
        float k = target.k();
        float r = target.r();
        return new Quaternion(i * value, j * value, k * value, r * value);
    }

    public static IKeyframe none() {
        return NULL_FRAME;
    }

    public static IKeyframe await(float endpoint) {
        return new EmptyKeyframe(endpoint);
    }

    public static IKeyframe position(Vector3d position, float endpoint) {
        return new PositionKeyframe(position, endpoint);
    }

    public static IKeyframe positionScale(Vector3d position, Vector3f scale, float endpoint) {
        return new PositionScaleKeyframe(position, scale, endpoint);
    }

    public static IKeyframe positionRotate(Vector3d position, Quaternion rotation, float endpoint) {
        return new PositionRotateKeyframe(position, rotation, endpoint);
    }

    public static IKeyframe keyframe(Vector3d position, Vector3f scale, Quaternion rotation, float endpoint) {
        return new Keyframe(position, scale, rotation, endpoint);
    }
}
