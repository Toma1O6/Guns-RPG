package lib.toma.animations.engine.frame;

import lib.toma.animations.Keyframes;
import lib.toma.animations.api.IKeyframe;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;

public class Keyframe extends PositionKeyframe {

    private final Quaternion rotation;
    private Quaternion relativeRotation = Quaternion.ONE.copy();

    protected Keyframe(Vector3d position, Quaternion rotation, float endpoint) {
        super(position, endpoint);
        this.rotation = rotation;
        calculateRelativeRotation();
    }

    public static IKeyframe of(Vector3d position, Quaternion rotation, float endpoint) {
        return new Keyframe(position, rotation, endpoint);
    }

    @Override
    public Quaternion rotationTarget() {
        return rotation;
    }

    @Override
    public Quaternion relativeRot() {
        return relativeRotation;
    }

    @Override
    public void baseOn(IKeyframe parent) {
        super.baseOn(parent);
        calculateRelativeRotation();
    }

    private void calculateRelativeRotation() {
        this.relativeRotation = Keyframes.getRelativeRotation(rotation, this.initialRotation());
    }
}
