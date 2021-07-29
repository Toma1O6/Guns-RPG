package lib.toma.animations.pipeline.frame;

import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.IKeyframeProvider;
import lib.toma.animations.pipeline.event.IAnimationEvent;

import java.util.HashMap;
import java.util.Map;

public class SingleFrameProvider implements IKeyframeProvider {

    private final Map<AnimationStage, IKeyframe[]> frames;

    public SingleFrameProvider(IFrameConstructor frameConstructor) {
        FrameBuilder builder = new FrameBuilder();
        frameConstructor.construct(builder);
        if (!builder.hasAddedFrame()) {
            throw new IllegalArgumentException("Cannot construct empty keyframe provider!");
        }
        frames = builder.frames;
    }

    @Override
    public Map<AnimationStage, IKeyframe[]> getFrames() {
        return frames;
    }

    @Override
    public IAnimationEvent[] animationEventsSorted() {
        return IAnimationEvent.NO_EVENTS;
    }

    @Override
    public byte[] getCacheStorage() {
        return new byte[1];
    }

    @FunctionalInterface
    public interface IFrameConstructor {
        void construct(FrameBuilder builder);
    }

    public static class FrameBuilder {
        private final Map<AnimationStage, IKeyframe[]> frames = new HashMap<>();
        private boolean modified;

        public FrameBuilder define(AnimationStage stage, IKeyframe iKeyframe) {
            if (frames.put(stage, new IKeyframe[] {iKeyframe}) != null)
                throw new UnsupportedOperationException("Cannot add another frame definition for " + stage.getKey().toString() + " stage!");
            modified = true;
            return this;
        }

        private boolean hasAddedFrame() {
            return modified;
        }
    }
}
