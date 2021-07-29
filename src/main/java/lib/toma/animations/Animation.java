package lib.toma.animations;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.IKeyframeProvider;
import lib.toma.animations.pipeline.TickableAnimation;
import lib.toma.animations.pipeline.event.IAnimationEvent;
import lib.toma.animations.pipeline.frame.IKeyframe;
import lib.toma.animations.pipeline.frame.Keyframes;
import net.minecraft.client.Minecraft;

public class Animation extends TickableAnimation {

    private final IKeyframeProvider provider;
    private final byte[] indexCache;
    private byte eventIndex;

    public Animation(IKeyframeProvider provider, int length) {
        super(length);
        this.provider = provider;
        this.indexCache = provider.getCacheStorage();
    }

    @Override
    public void animate(AnimationStage stage, MatrixStack matrix) {
        IKeyframe[] frames = provider.getFrames().get(stage);
        if (frames == null) return;
        float progressInterpolated = getIntepolatedProgress();
        byte frameIndex = getIndexFromCache(stage);
        IKeyframe animatingFrame = frames[frameIndex];
        float activeEndpoint = animatingFrame.endpoint();
        IKeyframe lastFrame;
        if (this.tryAdvance(stage, frames, progressInterpolated, animatingFrame, frameIndex)) {
            animatingFrame = frames[++frameIndex];
            lastFrame = frames[frameIndex - 1];
            activeEndpoint = animatingFrame.endpoint();
        } else {
            lastFrame = frameIndex == 0 ? Keyframes.none() : frames[frameIndex - 1];
        }
        float prevEndpoint = lastFrame.endpoint();
        float stageProgress = (progressInterpolated - prevEndpoint) / (activeEndpoint - prevEndpoint);
        Keyframes.processFrame(animatingFrame, stageProgress, matrix);
    }

    @Override
    public void nextFrame(float actualProgress, float previousProgress) {
        IAnimationEvent[] events = provider.animationEventsSorted();
        invokeEvent(events, actualProgress, previousProgress);
    }

    protected byte getIndexFromCache(AnimationStage stage) {
        return indexCache[stage.getIndex()];
    }

    private boolean tryAdvance(AnimationStage stage, IKeyframe[] frames, float progress, IKeyframe keyframe, byte index) {
        if (index >= frames.length || progress < keyframe.endpoint()) return false; // abort
        indexCache[stage.getIndex()] = (byte) (index + 1);
        return true;
    }

    private void invokeEvent(IAnimationEvent[] events, float actualProgress, float previousProgress) {
        if (eventIndex >= events.length) return;
        IAnimationEvent event = events[eventIndex];
        float target = event.invokeAt();
        if (actualProgress >= target && previousProgress < target) {
            event.dispatchEvent(Minecraft.getInstance(), this);
            ++eventIndex;
            invokeEvent(events, actualProgress, previousProgress);
        }
    }
}
