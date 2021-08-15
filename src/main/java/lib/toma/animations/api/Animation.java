package lib.toma.animations.api;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.Keyframes;

public class Animation extends TickableAnimation {

    private final IFrameProviderInstance provider;

    public Animation(IKeyframeProvider provider, int length) {
        super(length);
        this.provider = IFrameProviderInstance.forFrameProvider(provider);
    }

    public Animation(IFrameProviderInstance provider, int length) {
        super(length);
        this.provider = provider;
    }

    @Override
    public void animate(AnimationStage stage, MatrixStack matrix) {
        if (provider.blocksStageAnimation(stage)) return;
        float progressInterpolated = getIntepolatedProgress();
        IKeyframe currentFrame = provider.getCurrentFrame(stage, progressInterpolated);
        IKeyframe lastFrame = provider.getPreviousFrame(stage);
        float activeEndpoint = currentFrame.endpoint();
        float prevEndpoint = lastFrame.endpoint();
        float min = activeEndpoint - prevEndpoint;
        float stageProgress = min == 0.0F ? 1.0F : (progressInterpolated - prevEndpoint) / (activeEndpoint - prevEndpoint);
        Keyframes.processFrame(currentFrame, stageProgress, matrix);
    }

    @Override
    public void nextFrame(float actualProgress, float previousProgress) {
        provider.onAnimationProgressed(actualProgress, previousProgress, this);
    }
}
