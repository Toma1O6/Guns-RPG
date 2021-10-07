package lib.toma.animations;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Quite ugly workaround for multi-axis rotations. There were some issues with encoding rotations directly to rotation matrix,
 * so now it's done separatedly by calling {@link com.mojang.blaze3d.matrix.MatrixStack#mulPose(Quaternion)} for each axis.
 */
public final class RotationContext {

    public static final RotationContext EMPTY = new RotationContext();

    private final IRotationSpecification[] specifications;

    public RotationContext(IRotationSpecification... specifications) {
        this.specifications = specifications;
    }

    public static RotationContext forXYZ(float x, float y, float z, boolean isStatic) {
        List<IRotationSpecification> specificationList = new ArrayList<>();
        if (x != 0.0F)
            defineSpecification(x, f -> new Quaternion(f, 0.0F, 0.0F, true), specificationList, isStatic);
        if (y != 0.0F)
            defineSpecification(y, f -> new Quaternion(0.0F, f, 0.0F, true), specificationList, isStatic);
        if (z != 0.0F)
            defineSpecification(z, f -> new Quaternion(0.0F, 0.0F, f, true), specificationList, isStatic);
        return new RotationContext(specificationList.toArray(new IRotationSpecification[0]));
    }

    public static RotationContext forXYZ(double x, double y, double z, boolean isStatic) {
        return forXYZ((float) x, (float) y, (float) z, isStatic);
    }

    public static RotationContext forXYZ(Vector3d vector3d, boolean isStatic) {
        return forXYZ(vector3d.x, vector3d.y, vector3d.z, isStatic);
    }

    public void apply(MatrixStack matrix, float easedProgress) {
        for (IRotationSpecification specification : specifications)
            matrix.mulPose(specification.getQuaternion(easedProgress));
    }

    private static void defineSpecification(float in, Function<Float, Quaternion> rotationMatrixFunction, List<IRotationSpecification> list, boolean isStatic) {
        Quaternion quaternion = rotationMatrixFunction.apply(in);
        IRotationSpecification specification = isStatic ? new StaticRotation(quaternion) : new AnimatedRotation(quaternion);
        list.add(specification);
    }

    @FunctionalInterface
    private interface IRotationSpecification {
        Quaternion getQuaternion(float easedProgress);
    }

    private static class StaticRotation implements IRotationSpecification {

        final Quaternion quaternion;

        public StaticRotation(Quaternion quaternion) {
            this.quaternion = quaternion;
        }

        @Override
        public Quaternion getQuaternion(float easedProgress) {
            return quaternion;
        }
    }

    private static class AnimatedRotation extends StaticRotation {

        public AnimatedRotation(Quaternion quaternion) {
            super(quaternion);
        }

        @Override
        public Quaternion getQuaternion(float easedProgress) {
            Quaternion q1 = super.getQuaternion(easedProgress).copy();
            q1.mul(easedProgress);
            return q1;
        }
    }
}
