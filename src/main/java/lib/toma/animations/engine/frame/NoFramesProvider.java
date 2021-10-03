package lib.toma.animations.engine.frame;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import lib.toma.animations.Keyframes;
import lib.toma.animations.api.AnimationStage;
import lib.toma.animations.api.IKeyframe;
import lib.toma.animations.api.IKeyframeProvider;
import lib.toma.animations.api.IKeyframeTypeSerializer;
import lib.toma.animations.api.event.IAnimationEvent;

import java.util.Collections;
import java.util.Map;

public final class NoFramesProvider implements IKeyframeProvider {

    private static final NoFramesProvider NO_FRAMES = new NoFramesProvider();

    private NoFramesProvider() {}

    public static IKeyframeProvider empty() {
        return NO_FRAMES;
    }

    @Override
    public Map<AnimationStage, IKeyframe[]> getFrameMap() {
        return Collections.emptyMap();
    }

    @Override
    public FrameProviderType<?> getType() {
        return FrameProviderType.EMPTY;
    }

    @Override
    public boolean shouldAdvance(AnimationStage stage, float progress, int frameIndex) {
        return false;
    }

    @Override
    public IKeyframe getCurrentFrame(AnimationStage stage, float progress, int frameIndex) {
        return Keyframes.none();
    }

    @Override
    public IKeyframe getOldFrame(AnimationStage stage, int frameIndex) {
        return Keyframes.none();
    }

    @Override
    public IAnimationEvent[] getEvents() {
        return IAnimationEvent.NO_EVENTS;
    }

    @Override
    public void initCache(Map<AnimationStage, Integer> cache) {

    }

    public static class Serializer implements IKeyframeTypeSerializer<NoFramesProvider> {

        @Override
        public void serialize(JsonObject data, NoFramesProvider provider, JsonSerializationContext context) {}

        @Override
        public NoFramesProvider deserialize(JsonObject source, JsonDeserializationContext context, IAnimationEvent[] events) throws JsonParseException {
            return NO_FRAMES;
        }
    }
}
