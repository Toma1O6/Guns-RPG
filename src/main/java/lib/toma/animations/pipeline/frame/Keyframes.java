package lib.toma.animations.pipeline.frame;

import com.mojang.blaze3d.matrix.MatrixStack;
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

    public static void processFrame(IKeyframe keyframe, float percent, MatrixStack matrixStack) {
        Vector3d move1 = keyframe.initialPosition();
        Vector3d move2 = keyframe.positionTarget();
        Vector3f scale1 = keyframe.initialScale();
        Vector3f scale2 = keyframe.scaleTarget();
        Quaternion rot1 = keyframe.initialRotation();
        Quaternion rot2 = keyframe.rotationTarget();
        matrixStack.translate(move1.x + move2.x * percent, move1.y + move2.y * percent, move1.z + move2.z * percent);
        matrixStack.scale(scale1.x() + scale2.x() * percent, scale1.y() + scale2.y() * percent, scale1.z() + scale2.z() * percent);
        matrixStack.mulPose(mul(rot1, rot2, percent));
    }

    protected static Quaternion mul(Quaternion q1, Quaternion q2, float f) {
        Pair<Float, Vector3f> p1 = AnimationUtils.getVectorWithRotation(q1);
        Pair<Float, Vector3f> p2 = AnimationUtils.getVectorWithRotation(q2);
        float d1 = p1.getLeft();
        float d2 = p2.getLeft();
        Vector3f v1 = p1.getRight();
        Vector3f v2 = p2.getRight();
        float x = v2.x() * f;
        float y = v2.y() * f;
        float z = v2.z() * f;
        Vector3f v3 = new Vector3f(v1.x() + x, v1.y() + y, v1.z() + z);
        return new Quaternion(v3, d1 + d2 * f, true);
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
