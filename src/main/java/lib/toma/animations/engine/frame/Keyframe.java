package lib.toma.animations.engine.frame;

import lib.toma.animations.IEasing;
import lib.toma.animations.api.IKeyframe;
import net.minecraft.util.math.vector.Vector3d;

public class Keyframe extends PositionKeyframe {

    private Vector3d rotation;

    protected Keyframe(Vector3d position, Vector3d rotation, IEasing easing, float endpoint) {
        super(position, easing, endpoint);
        this.rotation = rotation;
        calculateRelativeRot();
    }

    public static IKeyframe of(Vector3d position, Vector3d rotation, IEasing easing, float endpoint) {
        return new Keyframe(position, rotation, easing, endpoint);
    }

    @Override
    public Vector3d rotationTarget() {
        return rotation;
    }
}
