package lib.toma.animations.api;

import lib.toma.animations.engine.frame.FrameProviderInstance;

public interface IFrameProviderInstance {

    boolean blocksStageAnimation(AnimationStage stage);

    IKeyframe getCurrentFrame(AnimationStage stage, float progress);

    IKeyframe getPreviousFrame(AnimationStage stage);

    void onAnimationProgressed(float progress, float progressOld, IAnimation source);

    static IFrameProviderInstance forFrameProvider(IKeyframeProvider provider) {
        return FrameProviderInstance.newInstance(provider);
    }
}
