package lib.toma.animations.api;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.Keyframes;
import net.minecraft.client.renderer.IRenderTypeBuffer;

/**
 * Tickable animation implementation ready to be used by
 * all types of {@link IKeyframeProvider}s.
 */
public class Animation extends TickableAnimation {

    private final IFrameProviderInstance provider;

    public Animation(IKeyframeProvider provider, int length) {
        this(IFrameProviderInstance.forFrameProvider(provider), length);
    }

    public Animation(IFrameProviderInstance provider, int length) {
        super(length);
        this.provider = provider;
    }

    @Override
    public void animate(AnimationStage stage, MatrixStack matrix, IRenderTypeBuffer typeBuffer, int light, int overlay) {
        if (provider.blocksStageAnimation(stage)) return;
        preAnimate(stage, matrix);
        float progressInterpolated = getInterpolatedProgress();
        IKeyframe currentFrame = provider.getCurrentFrame(stage, progressInterpolated);
        IKeyframe lastFrame = provider.getPreviousFrame(stage);
        float activeEndpoint = currentFrame.endpoint();
        float prevEndpoint = lastFrame.endpoint();
        float min = activeEndpoint - prevEndpoint;
        float stageProgress = min == 0.0F ? 1.0F : (progressInterpolated - prevEndpoint) / (activeEndpoint - prevEndpoint);
        Keyframes.processFrame(currentFrame, stageProgress, matrix);
        onAnimated(stage, matrix, typeBuffer, light, overlay);
    }

    @Override
    public void nextFrame(float actualProgress, float previousProgress) {
        provider.onAnimationProgressed(actualProgress, previousProgress, this);
    }

    public void onAnimated(AnimationStage stage, MatrixStack stack, IRenderTypeBuffer typeBuffer, int light, int overlay) {

    }

    public void preAnimate(AnimationStage stage, MatrixStack stack) {

    }
}
