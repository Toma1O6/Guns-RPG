package lib.toma.animations.api;

import lib.toma.animations.Keyframes;
import lib.toma.animations.engine.frame.FrameProviderInstance;

/**
 * Object used as data storage for running animations.
 * Contains data about currently processed frames for each stage, fired events etc.
 *
 * @author Toma
 */
public interface IFrameProviderInstance {

    /**
     * Decides whether provided {@link AnimationStage} can be processed
     * @param stage Stage to process
     * @return if provided stage can be processed by this object
     */
    boolean blocksStageAnimation(AnimationStage stage);

    /**
     * Get current {@link IKeyframe} for processing
     * @param stage Processing stage
     * @param progress Animation progress
     * @return Current keyframe for processing, should <b>never</b> return {@code null}!
     */
    IKeyframe getCurrentFrame(AnimationStage stage, float progress);

    /**
     * Get previously processed keyframe, used for calculating partial animation progress
     * @param stage Processed stage
     * @return Previously processed keyframe or {@link Keyframes#none()} when no such frame exists
     */
    IKeyframe getPreviousFrame(AnimationStage stage);

    /**
     * Called after every processed frame pass, used to dispatch animation events.
     * @param progress Current animation progress
     * @param progressOld Previous animation progress
     * @param source Animation source
     */
    void onAnimationProgressed(float progress, float progressOld, IAnimation source);

    /**
     * Creates new {@link IFrameProviderInstance} for specific {@link IKeyframeProvider}
     * @param provider Keyframe provider source
     * @return new keyframe provider instance
     */
    static IFrameProviderInstance forFrameProvider(IKeyframeProvider provider) {
        return FrameProviderInstance.newInstance(provider);
    }
}
