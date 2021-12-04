package lib.toma.animations.engine.frame;

import lib.toma.animations.AnimationUtils;
import lib.toma.animations.IEasing;
import lib.toma.animations.Keyframes;
import lib.toma.animations.api.IKeyframe;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;

public class EmptyKeyframe implements IKeyframe {

    private final float endpoint;
    private final IEasing easing;
    private Vector3d pos = Vector3d.ZERO;
    private Vector3d rot = Vector3d.ZERO;
    private Vector3d relativePos = Vector3d.ZERO;
    private Vector3d relativeRot = Vector3d.ZERO;
    private Quaternion rotQuat = Quaternion.ONE;
    private Quaternion relativeRotQuat = Quaternion.ONE;

    protected EmptyKeyframe(float endpoint, IEasing easing) {
        this.endpoint = endpoint;
        this.easing = easing;
    }

    public static IKeyframe wait(float value) {
        return wait(value, AnimationUtils.DEFAULT_EASE_FUNC);
    }

    public static IKeyframe wait(float value, IEasing easing) {
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
        return relativePos;
    }

    @Override
    public Vector3d relativeRot() {
        return relativeRot;
    }

    @Override
    public Quaternion getInitialRotationQuaternion() {
        return rotQuat;
    }

    @Override
    public Quaternion getRotationQuaternion() {
        return relativeRotQuat;
    }

    @Override
    public IEasing getEasing() {
        return easing;
    }

    @Override
    public void baseOn(IKeyframe parent) {
        pos = Keyframes.getInitialPosition(parent);
        rot = Keyframes.getInitialRotation(parent);
        relativePos = Vector3d.ZERO.subtract(pos);
        relativeRot = Vector3d.ZERO.subtract(rot);
        rotQuat = Keyframes.rotationVector2Quaternion(rot);
        relativeRotQuat = Keyframes.rotationVector2Quaternion(relativeRot);
    }
}
