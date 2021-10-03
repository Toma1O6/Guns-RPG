package lib.toma.animations.engine.frame;

import lib.toma.animations.AnimationUtils;
import lib.toma.animations.Easing;
import lib.toma.animations.Keyframes;
import lib.toma.animations.api.IKeyframe;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;

public class MutableKeyframe implements IKeyframe {

    public float endpoint;
    public Easing easing = AnimationUtils.DEFAULT_EASING;
    public Vector3d position = Vector3d.ZERO;
    public Vector3d rotation = Vector3d.ZERO;
    private Vector3d pos0 = Vector3d.ZERO;
    private Vector3d rot0 = Vector3d.ZERO;
    private Vector3d relPos = Vector3d.ZERO;
    private Vector3d relRot = Vector3d.ZERO;
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
    public Vector3d rotationTarget() {
        return rotation;
    }

    @Override
    public Vector3d relativeRot() {
        return relRot;
    }

    public void setRotation(Vector3d rotation) {
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
    public Vector3d initialRotation() {
        return rot0;
    }

    public void setRot0(Vector3d vector3d) {
        this.rot0 = vector3d;
        updateRelativeRot();
    }

    @Override
    public void baseOn(IKeyframe parent) {
        pos0 = Keyframes.getInitialPosition(parent);
        rot0 = Keyframes.getInitialRotation(parent);
        rot0Quat = Keyframes.rotationVector2Quaternion(rot0);
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

    @Override
    public Easing getEasing() {
        return easing;
    }

    public void setEasing(Easing easing) {
        this.easing = easing;
    }

    private void updateRelativePos() {
        relPos = Keyframes.getRelativePosition(position, pos0);
    }

    private void updateRelativeRot() {
        relRot = Keyframes.getRelativeRotation(rotation, rot0);
        relRotQuat = Keyframes.rotationVector2Quaternion(relRot);
    }
}
