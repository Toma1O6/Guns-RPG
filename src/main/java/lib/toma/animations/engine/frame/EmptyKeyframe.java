package lib.toma.animations.engine.frame;

import lib.toma.animations.Keyframes;
import lib.toma.animations.api.IKeyframe;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;

public class EmptyKeyframe implements IKeyframe {

    private final float endpoint;
    private Vector3d pos = Vector3d.ZERO;
    private Quaternion rot = Quaternion.ONE;

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
    public Quaternion rotationTarget() {
        return Quaternion.ONE;
    }

    @Override
    public Vector3d initialPosition() {
        return pos;
    }

    @Override
    public Quaternion initialRotation() {
        return rot;
    }

    @Override
    public void baseOn(IKeyframe parent) {
        pos = Keyframes.getInitialPosition(parent);
        rot = Keyframes.getInitialRotation(parent);
    }
}
