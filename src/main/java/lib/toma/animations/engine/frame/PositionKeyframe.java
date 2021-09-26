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
    private Quaternion staticRotation = Quaternion.ONE;
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
    public Quaternion rotationTarget() {
        return Quaternion.ONE;
    }

    @Override
    public Vector3d initialPosition() {
        return staticPos;
    }

    @Override
    public Quaternion initialRotation() {
        return staticRotation;
    }

    @Override
    public Vector3d relativePos() {
        return relativePos;
    }

    @Override
    public Quaternion relativeRot() {
        return rotationTarget();
    }

    @Override
    public void baseOn(IKeyframe parent) {
        this.staticPos = Keyframes.getInitialPosition(parent);
        this.staticRotation = Keyframes.getInitialRotation(parent);
        this.calculateRelativePos();
    }

    protected void calculateRelativePos() {
        this.relativePos = Keyframes.getRelativePosition(position, staticPos);
    }
}
