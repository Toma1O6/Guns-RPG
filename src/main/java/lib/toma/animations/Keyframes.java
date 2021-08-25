package lib.toma.animations;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.api.IKeyframe;
import lib.toma.animations.engine.frame.EmptyKeyframe;
import lib.toma.animations.engine.frame.Keyframe;
import lib.toma.animations.engine.frame.PositionKeyframe;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Comparator;

public class Keyframes {

    private static final IKeyframe NULL_FRAME = wait(0.0F);

    public static void sortFrames(IKeyframe[] array) {
        QuickSort.sort(array, Comparator.comparingDouble(IKeyframe::endpoint));
    }

    public static Vector3d getInitialPosition(IKeyframe parent) {
        return parent.initialPosition().add(parent.positionTarget());
    }

    public static Quaternion getInitialRotation(IKeyframe parent) {
        Quaternion q1 = parent.initialRotation();
        Quaternion q2 = parent.rotationTarget();
        Quaternion q3 = q1.copy();
        q3.mul(q2);
        return q3;
    }

    public static void processFrame(IKeyframe keyframe, float percent, MatrixStack matrixStack) {
        Vector3d move1 = keyframe.initialPosition();
        Vector3d move2 = keyframe.positionTarget();
        Quaternion q1 = keyframe.initialRotation();
        Quaternion q2 = keyframe.rotationTarget();
        Quaternion q3 = q2.copy();
        q3.mul(percent);
        matrixStack.translate(move1.x + move2.x * percent, move1.y + move2.y * percent, move1.z + move2.z * percent);
        matrixStack.mulPose(q1);
        matrixStack.mulPose(q3);
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

    public static IKeyframe keyframe(Vector3d position, Quaternion rotation, float endpoint) {
        return Keyframe.of(position, rotation, endpoint);
    }
}
