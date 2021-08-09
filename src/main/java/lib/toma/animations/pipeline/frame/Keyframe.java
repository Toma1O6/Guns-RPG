package lib.toma.animations.pipeline.frame;

import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;

public class Keyframe extends PositionKeyframe {

    private final Quaternion rotation;

    protected Keyframe(Vector3d position, Quaternion rotation, float endpoint) {
        super(position, endpoint);
        this.rotation = rotation;
    }

    @Override
    public Quaternion rotationTarget() {
        return rotation;
    }
}
