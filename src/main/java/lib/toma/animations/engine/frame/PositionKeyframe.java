package lib.toma.animations.engine.frame;

import lib.toma.animations.Keyframes;
import lib.toma.animations.api.IKeyframe;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Objects;

public class PositionKeyframe implements IKeyframe {

    private final Vector3d position;
    private final float endpoint;
    private Vector3d staticPos = Vector3d.ZERO;
    private Vector3d staticRotation = Vector3d.ZERO;
    private Quaternion staticRotationQuat = Quaternion.ONE;
    private Vector3d relativePos = Vector3d.ZERO;

    protected PositionKeyframe(Vector3d position, float endpoint) {
        this.position = Objects.requireNonNull(position);
        this.endpoint = endpoint;
        calculateRelativePos();
    }

    public static IKeyframe positioned(Vector3d position, float endpoint) {
        return new PositionKeyframe(position, endpoint);
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
    public Vector3d rotationTarget() {
        return Vector3d.ZERO;
    }

    @Override
    public Vector3d initialPosition() {
        return staticPos;
    }

    @Override
    public Vector3d initialRotation() {
        return staticRotation;
    }

    @Override
    public Vector3d relativePos() {
        return relativePos;
    }

    @Override
    public Vector3d relativeRot() {
        return rotationTarget();
    }

    @Override
    public Quaternion getInitialRotationQuaternion() {
        return staticRotationQuat;
    }

    @Override
    public Quaternion getRotationQuaternion() {
        return Quaternion.ONE;
    }

    @Override
    public void baseOn(IKeyframe parent) {
        this.staticPos = Keyframes.getInitialPosition(parent);
        this.staticRotation = Keyframes.getInitialRotation(parent);
        this.staticRotationQuat = Keyframes.rotationVector2Quaternion(staticRotation);
        this.calculateRelativePos();
    }

    protected void calculateRelativePos() {
        this.relativePos = Keyframes.getRelativePosition(position, staticPos);
    }
}
