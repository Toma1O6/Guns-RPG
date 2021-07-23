package lib.toma.animations.pipeline.frame;

import lib.toma.animations.serialization.IKeyframeSerializer;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public interface IKeyframe {

    float endpoint();

    Vector3d positionTarget();

    Vector3f scaleTarget();

    Quaternion rotationTarget();

    Vector3d initialPosition();

    Vector3f initialScale();

    Quaternion initialRotation();

    void baseOn(IKeyframe parent);

    IKeyframeSerializer serializers();
}
