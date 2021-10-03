package lib.toma.animations.engine.frame;

import lib.toma.animations.Keyframes;
import lib.toma.animations.api.IKeyframe;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;

public class Keyframe extends PositionKeyframe {

    private final Vector3d rotation;
    private Vector3d relativeRotation = Vector3d.ZERO;
    private Quaternion rotationQuat = Quaternion.ONE;

    protected Keyframe(Vector3d position, Vector3d rotation, float endpoint) {
        super(position, endpoint);
        this.rotation = rotation;
        calculateRelativeRotation();
    }

    public static IKeyframe of(Vector3d position, Vector3d rotation, float endpoint) {
        return new Keyframe(position, rotation, endpoint);
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
    public Quaternion getRotationQuaternion() {
        return rotationQuat;
    }

    @Override
    public void baseOn(IKeyframe parent) {
        super.baseOn(parent);
        calculateRelativeRotation();
    }

    private void calculateRelativeRotation() {
        this.relativeRotation = Keyframes.getRelativeRotation(rotation, this.initialRotation());
        this.rotationQuat = Keyframes.rotationVector2Quaternion(relativeRotation);
    }
}
