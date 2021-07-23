package lib.toma.animations.pipeline.frame;

import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class PositionScaleKeyframe extends PositionKeyframe {

    private final Vector3f scale;

    protected PositionScaleKeyframe(Vector3d position, Vector3f scale, float endpoint) {
        super(position, endpoint);
        this.scale = scale;
    }

    @Override
    public Vector3f scaleTarget() {
        return scale;
    }
}
