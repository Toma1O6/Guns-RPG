package lib.toma.animations;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.api.IKeyframe;
import lib.toma.animations.engine.frame.EmptyKeyframe;
import lib.toma.animations.engine.frame.Keyframe;
import lib.toma.animations.engine.frame.PositionKeyframe;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Comparator;

public class Keyframes {

    private static final IKeyframe NULL_FRAME = wait(0.0F, AnimationUtils.DEFAULT_EASING);

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
        float easedProgresss = keyframe.getEasing().ease(percent);
        Vector3d move1 = keyframe.initialPosition();
        Vector3d move2 = keyframe.relativePos();
        RotationContext ctx1 = keyframe.getInitialRotationContext();
        RotationContext ctx2 = keyframe.getRelativeRotationContext();
        matrixStack.translate(move1.x + move2.x * easedProgresss, move1.y + move2.y * easedProgresss, move1.z + move2.z * easedProgresss);
        ctx1.apply(matrixStack, easedProgresss);
        ctx2.apply(matrixStack, easedProgresss);
    }

    public static RotationContext rotationVector2Context(Vector3d rotation) {
        return rotationVector2Context(rotation, false);
    }

    public static RotationContext rotationVector2Context(Vector3d rotation, boolean isInitial) {
        if (rotation.equals(Vector3d.ZERO))
            return RotationContext.EMPTY;
        return RotationContext.forXYZ(rotation, isInitial);
    }

    public static IKeyframe none() {
        return NULL_FRAME;
    }

    public static IKeyframe wait(float endpoint, Easing easing) {
        return EmptyKeyframe.wait(endpoint, easing);
    }

    public static IKeyframe position(Vector3d position, Easing easing, float endpoint) {
        return PositionKeyframe.positioned(position, easing, endpoint);
    }

    public static IKeyframe keyframe(Vector3d position, Vector3d rotation, Easing easing, float endpoint) {
        return Keyframe.of(position, rotation, easing, endpoint);
    }
}
