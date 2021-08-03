package lib.toma.animations.pipeline.frame;

import lib.toma.animations.pipeline.AnimationStage;

import java.util.Map;

public interface ILoadableProvider {

    Map<AnimationStage, IKeyframe[]> getFramesForAnimator();
}
