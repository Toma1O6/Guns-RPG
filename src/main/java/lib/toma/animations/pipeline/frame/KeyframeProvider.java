package lib.toma.animations.pipeline.frame;

import lib.toma.animations.AnimationCompatLayer;
import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.IKeyframeProvider;
import lib.toma.animations.pipeline.event.IAnimationEvent;
import lib.toma.animations.serialization.AnimationLoader;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class KeyframeProvider implements IKeyframeProvider {

    public static final IKeyframeProvider NO_FRAMES = new NoFrames();
    private final Map<AnimationStage, IKeyframe[]> frames;
    private final IAnimationEvent[] events;
    private final Supplier<byte[]> cacheCreator;

    public KeyframeProvider(Map<AnimationStage, IKeyframe[]> frames, IAnimationEvent[] events) {
        this.frames = frames;
        this.events = events;
        this.cacheCreator = this::createCache;
        compileFrames();
    }

    public static IKeyframeProvider noFrames(ResourceLocation location) {
        AnimationCompatLayer.logger.error(AnimationLoader.MARKER, "Missing animation definition for {} key, falling back to default implementation", location);
        return NO_FRAMES;
    }

    @Override
    public Map<AnimationStage, IKeyframe[]> getFrames() {
        return frames;
    }

    @Override
    public IAnimationEvent[] animationEventsSorted() {
        return events;
    }

    @Override
    public byte[] getCacheStorage() {
        return cacheCreator.get();
    }

    private byte[] createCache() {
        int maxindex = 0;
        for (AnimationStage stage : frames.keySet()) {
            int index = stage.getIndex();
            if (index > maxindex) {
                maxindex = index;
            }
        }
        return new byte[maxindex];
    }

    private void compileFrames() {
        for (IKeyframe[] frames : frames.values()) {
            for (int i = 1; i < frames.length; i++) {
                IKeyframe actual = frames[i];
                IKeyframe last = frames[i - 1];
                actual.baseOn(last);
            }
        }
    }

    private static class NoFrames implements IKeyframeProvider {

        private static final Map<AnimationStage, IKeyframe[]> MAP = new HashMap<>(0);
        private static final IAnimationEvent[] EVENTS = new IAnimationEvent[0];

        @Override
        public Map<AnimationStage, IKeyframe[]> getFrames() {
            return MAP;
        }

        @Override
        public IAnimationEvent[] animationEventsSorted() {
            return EVENTS;
        }

        @Override
        public byte[] getCacheStorage() {
            return new byte[0];
        }
    }
}
