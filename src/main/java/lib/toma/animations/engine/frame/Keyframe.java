package lib.toma.animations.engine.frame;

import lib.toma.animations.Easing;
import lib.toma.animations.Keyframes;
import lib.toma.animations.RotationContext;
import lib.toma.animations.api.IKeyframe;
import net.minecraft.util.math.vector.Vector3d;

public class Keyframe extends PositionKeyframe {

    private final Vector3d rotation;
    private Vector3d relativeRotation = Vector3d.ZERO;
    private RotationContext rotationContext = RotationContext.EMPTY;

    protected Keyframe(Vector3d position, Vector3d rotation, Easing easing, float endpoint) {
        super(position, easing, endpoint);
        this.rotation = rotation;
        calculateRelativeRotation();
    }

    public static IKeyframe of(Vector3d position, Vector3d rotation, Easing easing, float endpoint) {
        return new Keyframe(position, rotation, easing, endpoint);
    }

    @Override
    public Vector3d rotationTarget() {
        return rotation;
    }

    @Override
    public Vector3d relativeRot() {
        return relativeRotation;
    }

    @Override
    public RotationContext getRelativeRotationContext() {
        return rotationContext;
    }

    @Override
    public void baseOn(IKeyframe parent) {
        super.baseOn(parent);
        calculateRelativeRotation();
    }

    private void calculateRelativeRotation() {
        this.relativeRotation = Keyframes.getRelativeRotation(rotation, this.initialRotation());
        this.rotationContext = Keyframes.rotationVector2Context(relativeRotation);
    }
}
