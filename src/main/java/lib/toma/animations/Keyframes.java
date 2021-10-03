package lib.toma.animations;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.api.IKeyframe;
import lib.toma.animations.engine.frame.EmptyKeyframe;
import lib.toma.animations.engine.frame.Keyframe;
import lib.toma.animations.engine.frame.PositionKeyframe;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.util.Comparator;

public class Keyframes {

    private static final IKeyframe NULL_FRAME = wait(0.0F);

    public static void sortFrames(IKeyframe[] array) {
        QuickSort.sort(array, Comparator.comparingDouble(IKeyframe::endpoint));
    }

    public static Vector3d getInitialPosition(IKeyframe parent) {
        return parent.initialPosition().add(parent.relativePos());
    }

    public static Vector3d getInitialRotation(IKeyframe parent) {
        Vector3d v1 = parent.initialRotation();
        Vector3d v2 = parent.relativeRot();
        return v1.add(v2);
    }

    public static Vector3d getRelativePosition(Vector3d target, Vector3d initial) {
        return target.subtract(initial);
    }

    public static Vector3d getRelativeRotation(Vector3d target, Vector3d initial) {
        return target.subtract(initial);
    }

    public static void processFrame(IKeyframe keyframe, float percent, MatrixStack matrixStack) {
        Vector3d move1 = keyframe.initialPosition();
        Vector3d move2 = keyframe.relativePos();
        Quaternion q1 = keyframe.getInitialRotationQuaternion();
        Quaternion q2 = keyframe.getRotationQuaternion();
        Quaternion q3 = q2.copy();
        q3.mul(percent);
        matrixStack.translate(move1.x + move2.x * percent, move1.y + move2.y * percent, move1.z + move2.z * percent);
        matrixStack.mulPose(q1);
        matrixStack.mulPose(q3);
    }

    public static Quaternion rotationVector2Quaternion(Vector3d rotation) {
        if (rotation.equals(Vector3d.ZERO))
            return Quaternion.ONE;
        float r = (float) max(rotation);
        float x = (float) (rotation.x / r);
        float y = (float) (rotation.y / r);
        float z = (float) (rotation.z / r);
        return new Quaternion(new Vector3f(x, y, z), r, true);
    }

    public static IKeyframe none() {
        return NULL_FRAME;
    }

    public static IKeyframe wait(float endpoint) {
        return EmptyKeyframe.wait(endpoint);
    }

    public static IKeyframe position(Vector3d position, float endpoint) {
        return PositionKeyframe.positioned(position, endpoint);
    }

    public static IKeyframe keyframe(Vector3d position, Vector3d rotation, float endpoint) {
        return Keyframe.of(position, rotation, endpoint);
    }

    private static double max(Vector3d vector3d) {
        double x = Math.abs(vector3d.x);
        double y = Math.abs(vector3d.y);
        double z = Math.abs(vector3d.z);
        return Math.max(x, Math.max(y, z));
    }
}
