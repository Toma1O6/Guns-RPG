package lib.toma.animations.pipeline.frame;

import lib.toma.animations.pipeline.AnimationStage;

import java.util.Map;

/**
 * This interface allows user customization of keyframe providers
 * via the Animator tool
 */
public interface ICustomizableProvider {

    /**
     * Implementations should collect all keyframes and put them into new map object.
     * @return AnimationStage to keyframe array mappings
     */
    Map<AnimationStage, IKeyframe[]> getFramesForAnimator();

    /**
     * Inserts specified keyframe into frame provider
     * @param stage Targetted animation stage
     * @param iKeyframe Keyframe to insert
     */
    void insertKeyframe(AnimationStage stage, IKeyframe iKeyframe);

    /**
     * Deletes specified keyframe from frame provider. Keyframes can be compared by reference
     * @param stage Targetted animation stage
     * @param iKeyframe Keyframe reference to delete
     */
    void deleteKeyframe(AnimationStage stage, IKeyframe iKeyframe);
}
