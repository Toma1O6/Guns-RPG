package lib.toma.animations.api;

import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;

public interface IKeyframe {

    float endpoint();

    Vector3d positionTarget();

    Quaternion rotationTarget();

    Vector3d initialPosition();

    Quaternion initialRotation();

    void baseOn(IKeyframe parent);
}
