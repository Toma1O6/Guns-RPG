package lib.toma.animations.engine.frame;

import lib.toma.animations.Keyframes;
import lib.toma.animations.api.IKeyframe;
import lib.toma.animations.engine.Vector4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;

public class MutableKeyframe implements IKeyframe {

    public float endpoint;
    public Vector3d position = Vector3d.ZERO;
    public Vector4f rotation = Vector4f.ZERO;
    private Vector3d pos0 = Vector3d.ZERO;
    private Vector4f rot0 = Vector4f.ZERO;
    private Vector3d relPos = Vector3d.ZERO;
    private Vector4f relRot = Vector4f.ZERO;
    private Quaternion rot0Quat = Quaternion.ONE;
    private Quaternion relRotQuat = Quaternion.ONE;

    public static MutableKeyframe fullCopyOf(IKeyframe frame) {
        MutableKeyframe mkf = copyOf(frame);
        mkf.setPos0(frame.initialPosition());
        mkf.setRot0(frame.initialRotation());
        mkf.updateRelativePos();
        mkf.updateRelativeRot();
        return mkf;
    }

    public static MutableKeyframe copyOf(IKeyframe keyframe) {
        MutableKeyframe mkf = new MutableKeyframe();
        mkf.setEndpoint(keyframe.endpoint());
        mkf.setPosition(keyframe.positionTarget());
        mkf.setRotation(keyframe.rotationTarget());
        return mkf;
    }

    @Override
    public float endpoint() {
        return endpoint;
    }

    public void setEndpoint(float endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public Vector3d positionTarget() {
        return position;
    }

    @Override
    public Vector3d relativePos() {
        return relPos;
    }

    public void setPosition(Vector3d position) {
        this.position = position;
        updateRelativePos();
    }

    @Override
    public Vector4f rotationTarget() {
        return rotation;
    }

    @Override
    public Vector4f relativeRot() {
        return relRot;
    }

    public void setRotation(Vector4f rotation) {
        this.rotation = rotation;
        updateRelativeRot();
    }

    @Override
    public Vector3d initialPosition() {
        return pos0;
    }

    public void setPos0(Vector3d pos0) {
        this.pos0 = pos0;
        updateRelativePos();
    }

    @Override
    public Vector4f initialRotation() {
        return rot0;
    }

    public void setRot0(Vector4f vector4f) {
        this.rot0 = vector4f;
        updateRelativeRot();
    }

    @Override
    public void baseOn(IKeyframe parent) {
        pos0 = Keyframes.getInitialPosition(parent);
        rot0 = Keyframes.getInitialRotation(parent);
        rot0Quat = rot0.toQuaternion();
        updateRelativePos();
        updateRelativeRot();
    }

    @Override
    public Quaternion getInitialRotationQuaternion() {
        return rot0Quat;
    }

    @Override
    public Quaternion getRotationQuaternion() {
        return relRotQuat;
    }

    private void updateRelativePos() {
        relPos = Keyframes.getRelativePosition(position, pos0);
    }

    private void updateRelativeRot() {
        relRot = Keyframes.getRelativeRotation(rotation, rot0);
        relRotQuat = relRot.toQuaternion();
    }
}
