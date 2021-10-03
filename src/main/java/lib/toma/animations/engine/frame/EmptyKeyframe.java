package lib.toma.animations.engine.frame;

import lib.toma.animations.Keyframes;
import lib.toma.animations.api.IKeyframe;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;

public class EmptyKeyframe implements IKeyframe {

    private final float endpoint;
    private Vector3d pos = Vector3d.ZERO;
    private Vector3d rot = Vector3d.ZERO;
    private Quaternion rotQuat = Quaternion.ONE;

    protected EmptyKeyframe(float endpoint) {
        this.endpoint = endpoint;
    }

    public static IKeyframe wait(float value) {
        return new EmptyKeyframe(value);
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
    public Quaternion getInitialRotationQuaternion() {
        return Quaternion.ONE;
    }

    @Override
    public Quaternion getRotationQuaternion() {
        return rotQuat;
    }

    @Override
    public void baseOn(IKeyframe parent) {
        pos = Keyframes.getInitialPosition(parent);
        rot = Keyframes.getInitialRotation(parent);
        rotQuat = Keyframes.rotationVector2Quaternion(rot);
    }
}
