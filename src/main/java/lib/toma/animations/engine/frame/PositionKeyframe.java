package lib.toma.animations.engine.frame;

import lib.toma.animations.Easing;
import lib.toma.animations.Keyframes;
import lib.toma.animations.RotationContext;
import lib.toma.animations.api.IKeyframe;
import net.minecraft.util.math.vector.Vector3d;

import java.util.Objects;

public class PositionKeyframe implements IKeyframe {

    private final Vector3d position;
    private final Easing easing;
    private final float endpoint;
    private Vector3d staticPos = Vector3d.ZERO;
    private Vector3d staticRotation = Vector3d.ZERO;
    private RotationContext staticRotationContext = RotationContext.EMPTY;
    private Vector3d relativePos = Vector3d.ZERO;

    protected PositionKeyframe(Vector3d position, Easing easing, float endpoint) {
        this.position = Objects.requireNonNull(position);
        this.easing = easing;
        this.endpoint = endpoint;
        calculateRelativePos();
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
        return rotationTarget();
    }

    @Override
    public RotationContext getInitialRotationContext() {
        return staticRotationContext;
    }

    @Override
    public RotationContext getRelativeRotationContext() {
        return RotationContext.EMPTY;
    }

    @Override
    public Easing getEasing() {
        return easing;
    }

    @Override
    public void baseOn(IKeyframe parent) {
        this.staticPos = Keyframes.getInitialPosition(parent);
        this.staticRotation = Keyframes.getInitialRotation(parent);
        this.staticRotationContext = Keyframes.rotationVector2Context(staticRotation, true);
        this.calculateRelativePos();
    }

    protected void calculateRelativePos() {
        this.relativePos = Keyframes.getRelativePosition(position, staticPos);
    }
}
