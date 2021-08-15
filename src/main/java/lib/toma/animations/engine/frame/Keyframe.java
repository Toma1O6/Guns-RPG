package lib.toma.animations.engine.frame;

import lib.toma.animations.api.IKeyframe;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;

public class Keyframe extends PositionKeyframe {

    private final Quaternion rotation;

    protected Keyframe(Vector3d position, Quaternion rotation, float endpoint) {
        super(position, endpoint);
        this.rotation = rotation;
    }

    public static IKeyframe of(Vector3d position, Quaternion rotation, float endpoint) {
        return new Keyframe(position, rotation, endpoint);
    }

    @Override
    public Quaternion rotationTarget() {
        return rotation;
    }
}
