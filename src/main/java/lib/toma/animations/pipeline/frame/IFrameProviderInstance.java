package lib.toma.animations.pipeline.frame;

import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.IAnimation;

public interface IFrameProviderInstance {

    boolean canAnimate(AnimationStage stage);

    IKeyframe getCurrentFrame(AnimationStage stage, float progress);

    IKeyframe getPreviousFrame(AnimationStage stage);

    void onAnimationProgressed(float progress, float progressOld, IAnimation source);

    static IFrameProviderInstance forFrameProvider(IKeyframeProvider provider) {
        return new FrameProviderInstance(provider);
    }
}
