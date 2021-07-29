package lib.toma.animations.pipeline.frame;

import lib.toma.animations.AnimationUtils;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class EmptyKeyframe implements IKeyframe {

    private final float endpoint;
    private Vector3d pos = Vector3d.ZERO;
    private Vector3f scale = AnimationUtils.DEFAULT_SCALE_VECTOR;
    private Quaternion rot = Quaternion.ONE;

    protected EmptyKeyframe(float endpoint) {
        this.endpoint = endpoint;
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
    public Vector3f scaleTarget() {
        return AnimationUtils.EMPTY_SCALE_VECTOR;
    }

    @Override
    public Quaternion rotationTarget() {
        return Quaternion.ONE;
    }

    @Override
    public Vector3d initialPosition() {
        return pos;
    }

    @Override
    public Vector3f initialScale() {
        return scale;
    }

    @Override
    public Quaternion initialRotation() {
        return rot;
    }

    @Override
    public void baseOn(IKeyframe parent) {
        pos = Keyframes.getEntryPosition(parent);
        scale = Keyframes.getEntryScale(parent);
        rot = Keyframes.getEntryRotation(parent);
    }
}
