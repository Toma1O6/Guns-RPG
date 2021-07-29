package lib.toma.animations.pipeline;

import lib.toma.animations.pipeline.event.IAnimationEvent;
import lib.toma.animations.pipeline.frame.IKeyframe;

import java.util.Map;

public interface IKeyframeProvider {

    Map<AnimationStage, IKeyframe[]> getFrames();

    IAnimationEvent[] animationEventsSorted();

    byte[] getCacheStorage();
}
