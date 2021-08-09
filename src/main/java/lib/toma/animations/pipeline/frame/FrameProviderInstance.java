package lib.toma.animations.pipeline.frame;

import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.IAnimation;
import lib.toma.animations.pipeline.event.IAnimationEvent;
import net.minecraft.client.Minecraft;

import java.util.HashMap;
import java.util.Map;

public final class FrameProviderInstance implements IFrameProviderInstance {

    private final IKeyframeProvider frameProvider;
    private final Map<AnimationStage, Integer> frameCache;
    private byte eventIndex;

    FrameProviderInstance(IKeyframeProvider frameProvider) {
        this.frameProvider = frameProvider;
        this.frameCache = new HashMap<>();
        this.frameProvider.initCache(frameCache);
    }

    public static FrameProviderInstance instance(IKeyframeProvider provider) {
        return new FrameProviderInstance(provider);
    }

    public boolean blocksStageAnimation(AnimationStage stage) {
        return !frameCache.containsKey(stage);
    }

    public IKeyframe getCurrentFrame(AnimationStage stage, float animationProgress) {
        int index = frameCache.get(stage);
        if (frameProvider.shouldAdvance(stage, animationProgress, index)) {
            frameCache.put(stage, ++index);
        }
        return frameProvider.getCurrentFrame(stage, animationProgress, index);
    }

    public IKeyframe getPreviousFrame(AnimationStage stage) {
        return frameProvider.getOldFrame(stage, frameCache.get(stage));
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
            event.dispatch(Minecraft.getInstance(), source);
            invokeEventsRecursive(events, source, progress, progressOld); // makes sure all events are called in case there are multiple events 'close' to each other
        }
    }
}
