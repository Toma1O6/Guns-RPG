package lib.toma.animations.pipeline.frame;

import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.IAnimation;
import lib.toma.animations.pipeline.event.IAnimationEvent;
import net.minecraft.client.Minecraft;

public final class FrameProviderInstance implements IFrameProviderInstance {

    private final IKeyframeProvider frameProvider;
    private final byte[] frameIndexCache;
    private byte eventIndex;

    FrameProviderInstance(IKeyframeProvider frameProvider) {
        this.frameProvider = frameProvider;
        this.frameIndexCache = new byte[frameProvider.getCacheSize()];
    }

    public boolean canAnimate(AnimationStage stage) {
        return stage.getIndex() < frameIndexCache.length;
    }

    public IKeyframe getCurrentFrame(AnimationStage stage, float animationProgress) {
        byte index = frameIndexCache[stage.getIndex()];
        if (frameProvider.shouldAdvance(stage, animationProgress, index)) {
            index = ++frameIndexCache[stage.getIndex()];
        }
        return frameProvider.getCurrentFrame(stage, animationProgress, index);
    }

    public IKeyframe getPreviousFrame(AnimationStage stage) {
        return frameProvider.getOldFrame(stage, frameIndexCache[stage.getIndex()]);
    }

    public void onAnimationProgressed(float progress, float progressOld, IAnimation source) {
        IAnimationEvent[] events = frameProvider.getEvents();
        invokeEventsRecursive(events, source, progress, progressOld);
    }

    private void invokeEventsRecursive(IAnimationEvent[] events, IAnimation source, float progress, float progressOld) {
        if (eventIndex >= events.length) return;
        IAnimationEvent event = events[eventIndex];
        float eventDispatchTarget = event.invokeAt();
        if (progress >= eventDispatchTarget && progressOld < eventDispatchTarget) {
            ++eventIndex;
            event.dispatchEvent(Minecraft.getInstance(), source);
            invokeEventsRecursive(events, source, progress, progressOld); // makes sure all events are called in case there are multiple events 'close' to each other
        }
    }
}
