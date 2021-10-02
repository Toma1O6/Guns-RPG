package lib.toma.animations.engine.frame;

import lib.toma.animations.Keyframes;
import lib.toma.animations.api.IKeyframe;
import lib.toma.animations.engine.Vector4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;

public class Keyframe extends PositionKeyframe {

    private final Vector4f rotation;
    private Vector4f relativeRotation = Vector4f.ZERO;
    private Quaternion rotationQuat = Quaternion.ONE;

    protected Keyframe(Vector3d position, Vector4f rotation, float endpoint) {
        super(position, endpoint);
        this.rotation = rotation;
        calculateRelativeRotation();
    }

    public static IKeyframe of(Vector3d position, Vector4f rotation, float endpoint) {
        return new Keyframe(position, rotation, endpoint);
    }

    @Override
    public Vector4f rotationTarget() {
        return rotation;
    }

    @Override
    public Vector4f relativeRot() {
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
        this.rotationQuat = relativeRotation.toQuaternion();
    }
}
