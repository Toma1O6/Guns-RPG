package lib.toma.animations.api;

import lib.toma.animations.IEasing;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;

/**
 * Keyframes are used to store position and rotation transforms for each animation step.
 * Each keyframe has assigned its endpoint which determines when keyframe should be used.
 * If keyframe is played after another keyframe, it will be assigned to it via {@link IKeyframe#baseOn(IKeyframe)} method,
 * which should be used to compute initial position and rotation values in this keyframe.
 *
 * @author Toma
 */
public interface IKeyframe {

    /**
     * Used to mark end position of this keyframe in animation progress.
     * Initial position is defined by parent's keyframe endpoint or 0 when this is
     * first keyframe in list.
     * @return Endpoint of this keyframe
     */
    float endpoint();

    /**
     * Returns position target of this keyframe. Position is additive, which means that
     * it will be added to initial position.
     * @return Position target. Animation position after this keyframe is going to be {@code Initial position + Target position}
     */
    Vector3d positionTarget();

    /**
     * Returns rotation target of this keyframe. Rotation is additive, which means that
     * it will be added to initial rotation.
     * @return Rotation target. Animation rotation after this keyframe is going to be {@code Initial rotation + Target rotation}
     */
    Vector3d rotationTarget();

    /**
     * @return Initial position of this keyframe. Should be calculated in {@link IKeyframe#baseOn(IKeyframe)} method via
     * {@link lib.toma.animations.Keyframes#getInitialPosition(IKeyframe)}
     */
    Vector3d initialPosition();

    /**
     * @return Initial rotation of this keyframe. Should be calculated in {@link IKeyframe#baseOn(IKeyframe)} method via
     * {@link lib.toma.animations.Keyframes#getInitialRotation(IKeyframe)}
     */
    Vector3d initialRotation();

    /**
     * @return Relative position for animation
     */
    Vector3d relativePos();

    /**
     * @return Relative rotation for animation
     */
    Vector3d relativeRot();

    /**
     * @return Initial quaternion for rotations
     */
    Quaternion getInitialRotationQuaternion();

    /**
     * @return Quaternion for rotations
     */
    Quaternion getRotationQuaternion();

    /**
     * @return Easing function which should be used on this keyframe
     */
    IEasing getEasing();

    /**
     * Used to calculate initial position and rotation
     * @param parent Parent keyframe
     */
    void baseOn(IKeyframe parent);
}
