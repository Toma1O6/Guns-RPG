package lib.toma.animations.pipeline.frame;

import lib.toma.animations.AnimationUtils;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.util.Objects;

public class PositionKeyframe implements IKeyframe {

    private final Vector3d position;
    private final float endpoint;
    private Vector3d staticPos = Vector3d.ZERO;
    private Vector3f staticScale = AnimationUtils.DEFAULT_SCALE_VECTOR;
    private Quaternion staticRotation = Quaternion.ONE;

    protected PositionKeyframe(Vector3d position, float endpoint) {
        this.position = Objects.requireNonNull(position);
        this.endpoint = endpoint;
    }

    @Override
    public float endpoint() {
        return endpoint;
    }

    @Override
    public Vector3d positionTarget() {
        return position;
    }

    @Override
    public Vector3f scaleTarget() {
        return AnimationUtils.EMPTY_SCALE_VECTOR;
    }

    @Override
    public Quaternion rotationTarget() {
        return Quaternion.ONE;
    }

    @Override
    public Vector3d initialPosition() {
        return staticPos;
    }

    @Override
    public Vector3f initialScale() {
        return staticScale;
    }

    @Override
    public Quaternion initialRotation() {
        return staticRotation;
    }

    @Override
    public void baseOn(IKeyframe parent) {
        this.staticPos = Keyframes.getInitialPosition(parent);
        this.staticScale = Keyframes.getInitialScale(parent);
        this.staticRotation = Keyframes.getInitialRotation(parent);
    }
}
