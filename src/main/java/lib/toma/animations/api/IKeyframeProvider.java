package lib.toma.animations.api;

import lib.toma.animations.api.event.IAnimationEvent;
import lib.toma.animations.engine.frame.FrameProviderType;

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
