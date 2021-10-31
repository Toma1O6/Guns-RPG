package lib.toma.animations.engine.frame;

import lib.toma.animations.Easing;
import lib.toma.animations.Keyframes;
import lib.toma.animations.api.IKeyframe;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Objects;

public class PositionKeyframe implements IKeyframe {

    private final Vector3d position;
    private final Easing easing;
    private final float endpoint;
    private Vector3d staticPos = Vector3d.ZERO;
    private Vector3d staticRotation = Vector3d.ZERO;
    private Quaternion staticRotationQuat = Quaternion.ONE;
    private Vector3d relativePos = Vector3d.ZERO;
    private Vector3d relativeRot = Vector3d.ZERO;
    private Quaternion relativeRotQuat = Quaternion.ONE;

    protected PositionKeyframe(Vector3d position, Easing easing, float endpoint) {
        this.position = Objects.requireNonNull(position);
        this.easing = easing;
        this.endpoint = endpoint;
        calculateRelativePos();
        calculateRelativeRot();
    }

    public static IKeyframe positioned(Vector3d position, Easing easing, float endpoint) {
        return new PositionKeyframe(position, easing, endpoint);
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
        return relativeRot;
    }

    @Override
    public Quaternion getInitialRotationQuaternion() {
        return staticRotationQuat;
    }

    @Override
    public Quaternion getRotationQuaternion() {
        return relativeRotQuat;
    }

    @Override
    public Easing getEasing() {
        return easing;
    }

    @Override
    public void baseOn(IKeyframe parent) {
        this.staticPos = Keyframes.getInitialPosition(parent);
        this.staticRotation = Keyframes.getInitialRotation(parent);
        this.staticRotationQuat = Keyframes.rotationVector2Quaternion(staticRotation);
        this.calculateRelativePos();
        this.calculateRelativeRot();
    }

    private void calculateRelativePos() {
        this.relativePos = Keyframes.getRelativePosition(position, staticPos);
    }

    protected void calculateRelativeRot() {
        Vector3d rotation = rotationTarget();
        if (rotation == null) return;
        this.relativeRot = Keyframes.getRelativeRotation(rotation, staticRotation);
        this.relativeRotQuat = Keyframes.rotationVector2Quaternion(relativeRot);
    }
}
