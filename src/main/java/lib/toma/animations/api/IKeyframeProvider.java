package lib.toma.animations.api;

import lib.toma.animations.Keyframes;
import lib.toma.animations.api.event.IAnimationEvent;
import lib.toma.animations.engine.frame.FrameProviderType;

import java.util.Map;

/**
 * IKeyframe provider handles that animation always gets the correct keyframes for its progress and stage
 *
 * @author Toma
 */
public interface IKeyframeProvider {

    /**
     * Gets type for serialization
     * @return Frame provider type
     */
    FrameProviderType<?> getType();

    /**
     * Checks whether animation should move to next frame given its progress
     * @param stage Currently rendered stage
     * @param progress Animation progress
     * @param frameIndex Current frame index
     * @return {@code True} if animation should move to next frame
     */
    boolean shouldAdvance(AnimationStage stage, float progress, int frameIndex);

    /**
     * Retrieves frame from storage given animation stage and frame index
     * @param stage Currently rendered stage
     * @param progress Animation progress
     * @param frameIndex Current frame index
     * @return Keyframe for processing. Implementations should always return {@code nonnull} value!
     */
    IKeyframe getCurrentFrame(AnimationStage stage, float progress, int frameIndex);

    /**
     * Retrieves previous keyframe given actual frame index
     * @param stage Currently rendered stage
     * @param frameIndex Current frame index
     * @return Previously processed {@link IKeyframe} or {@link Keyframes#none()} if no such keyframe exists
     */
    IKeyframe getOldFrame(AnimationStage stage, int frameIndex);

    /**
     * Array of animation events
     * @return Event array
     */
    IAnimationEvent[] getEvents();

    /**
     * Initializes frame index cache. Usually just inserts {@code 0} for every {@link AnimationStage} defined in this provider
     * @param cache Frame index cache for initialization
     */
    void initCache(Map<AnimationStage, Integer> cache);

    /**
     * Return map of all keyframes for each stage. Used in developer tool for loading displaying keyframes on timelines.
     * @return Mapping of all keyframes for specific animation stage
     */
    Map<AnimationStage, IKeyframe[]> getFrameMap();
}
