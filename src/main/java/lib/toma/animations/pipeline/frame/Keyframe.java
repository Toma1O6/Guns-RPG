package lib.toma.animations.pipeline.frame;

import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class Keyframe extends PositionScaleKeyframe {

    private final Quaternion rotation;

    protected Keyframe(Vector3d position, Vector3f scale, Quaternion rotation, float endpoint) {
        super(position, scale, endpoint);
        this.rotation = rotation;
    }

    @Override
    public Quaternion rotationTarget() {
        return rotation;
    }
}
