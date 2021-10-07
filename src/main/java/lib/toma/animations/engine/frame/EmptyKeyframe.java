package lib.toma.animations.engine.frame;

import lib.toma.animations.AnimationUtils;
import lib.toma.animations.Easing;
import lib.toma.animations.Keyframes;
import lib.toma.animations.RotationContext;
import lib.toma.animations.api.IKeyframe;
import net.minecraft.util.math.vector.Vector3d;

public class EmptyKeyframe implements IKeyframe {

    private final float endpoint;
    private final Easing easing;
    private Vector3d pos = Vector3d.ZERO;
    private Vector3d rot = Vector3d.ZERO;
    private RotationContext context = RotationContext.EMPTY;

    protected EmptyKeyframe(float endpoint, Easing easing) {
        this.endpoint = endpoint;
        this.easing = easing;
    }

    public static IKeyframe wait(float value) {
        return wait(value, AnimationUtils.DEFAULT_EASING);
    }

    public static IKeyframe wait(float value, Easing easing) {
        return new EmptyKeyframe(value, easing);
    }

    @Override
    public float endpoint() {
        return endpoint;
    }

    @Override
    public Vector3d positionTarget() {
        return Vector3d.ZERO;
    }

    @Override
    public Vector3d rotationTarget() {
        return Vector3d.ZERO;
    }

    @Override
    public Vector3d initialPosition() {
        return pos;
    }

    @Override
    public Vector3d initialRotation() {
        return rot;
    }

    @Override
    public Vector3d relativePos() {
        return positionTarget();
    }

    @Override
    public Vector3d relativeRot() {
        return rotationTarget();
    }

    @Override
    public RotationContext getInitialRotationContext() {
        return context;
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
        pos = Keyframes.getInitialPosition(parent);
        rot = Keyframes.getInitialRotation(parent);
        context = Keyframes.rotationVector2Context(rot, true);
    }
}
