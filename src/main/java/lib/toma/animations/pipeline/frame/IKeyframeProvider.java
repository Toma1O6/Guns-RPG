package lib.toma.animations.pipeline.frame;

import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.event.IAnimationEvent;

import java.util.Map;

public interface IKeyframeProvider {

    FrameProviderType<?> getType();

    boolean shouldAdvance(AnimationStage stage, float progress, int frameIndex);

    IKeyframe getCurrentFrame(AnimationStage stage, float progress, int frameIndex);

    IKeyframe getOldFrame(AnimationStage stage, int frameIndex);

    IAnimationEvent[] getEvents();

    void initCache(Map<AnimationStage, Integer> cache);

    Map<AnimationStage, IKeyframe[]> getFrameMap();
}
