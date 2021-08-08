package lib.toma.animations.pipeline.frame;

import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.event.IAnimationEvent;

import java.util.Map;

public interface IKeyframeProvider {

    FrameProviderType<?> getType();

    boolean shouldAdvance(AnimationStage stage, float progress, byte frameIndex);

    IKeyframe getCurrentFrame(AnimationStage stage, float progress, byte frameIndex);

    IKeyframe getOldFrame(AnimationStage stage, byte frameIndex);

    IAnimationEvent[] getEvents();

    int getCacheSize();

    Map<AnimationStage, IKeyframe[]> getFrameMap();
}
