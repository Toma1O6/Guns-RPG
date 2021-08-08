package lib.toma.animations.pipeline.frame;

import com.google.gson.*;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.event.IAnimationEvent;
import lib.toma.animations.serialization.AnimationLoader;
import lib.toma.animations.serialization.IKeyframeTypeSerializer;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class KeyframeProvider implements IKeyframeProvider {

    private final Map<AnimationStage, IKeyframe[]> frames;
    private final IAnimationEvent[] events;
    private final int maxStageIndex;

    public KeyframeProvider(Map<AnimationStage, IKeyframe[]> frames, IAnimationEvent[] events) {
        this.frames = frames;
        this.events = events;
        this.maxStageIndex = AnimationUtils.getBiggestFromMap(frames, Map::keySet, AnimationStage::getIndex);
        compileFrames();
    }

    public static IKeyframeProvider noFrames(ResourceLocation location) {
        AnimationEngine.logger.error(AnimationLoader.MARKER, "Missing animation definition for {} key, falling back to default implementation", location);
        return NoFramesProvider.empty();
    }

    @Override
    public boolean shouldAdvance(AnimationStage stage, float progress, byte frameIndex) {
        IKeyframe[] keyframes = frames.get(stage);
        if (keyframes == null || frameIndex >= keyframes.length - 1) return false;
        IKeyframe frame = keyframes[frameIndex];
        return frame.endpoint() <= progress;
    }

    @Override
    public IKeyframe getCurrentFrame(AnimationStage stage, float progress, byte frameIndex) {
        return frames.get(stage)[frameIndex];
    }

    @Override
    public IKeyframe getOldFrame(AnimationStage stage, byte frameIndex) {
        return frameIndex == 0 ? Keyframes.none() : frames.get(stage)[frameIndex - 1];
    }

    @Override
    public IAnimationEvent[] getEvents() {
        return events;
    }

    @Override
    public int getCacheSize() {
        return maxStageIndex;
    }

    @Override
    public FrameProviderType<?> getType() {
        return FrameProviderType.KEYFRAME_PROVIDER_TYPE;
    }

    @Override
    public Map<AnimationStage, IKeyframe[]> getFrameMap() {
        return frames;
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

    public static class Serializer implements IKeyframeTypeSerializer<KeyframeProvider> {

        @Override
        public void serialize(JsonObject data, KeyframeProvider provider, JsonSerializationContext context) {
            Map<AnimationStage, IKeyframe[]> frames = provider.frames;
            for (Map.Entry<AnimationStage, IKeyframe[]> entry : frames.entrySet()) {
                String key = entry.getKey().toString();
                JsonArray array = new JsonArray();
                for (IKeyframe frame : entry.getValue()) {
                    array.add(context.serialize(frame, IKeyframe.class));
                }
                data.add(key, array);
            }
        }

        @Override
        public KeyframeProvider deserialize(JsonObject source, JsonDeserializationContext context, IAnimationEvent[] events) throws JsonParseException {
            Map<AnimationStage, IKeyframe[]> map = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : source.entrySet()) {
                ResourceLocation key = new ResourceLocation(entry.getKey());
                AnimationStage stage = AnimationStage.byKey(key);
                if (stage == null)
                    throw new JsonSyntaxException("Unknown animation stage: " + key);
                JsonElement element = entry.getValue();
                if (!element.isJsonArray())
                    throw new JsonSyntaxException("Not a Json array!");
                JsonArray array = element.getAsJsonArray();
                IKeyframe[] keyframes = new IKeyframe[array.size()];
                for (int i = 0; i < array.size(); i++) {
                    IKeyframe iKeyframe = context.deserialize(array.get(i), IKeyframe.class);
                    keyframes[i] = iKeyframe;
                }
                Keyframes.sortFrames(keyframes);
                map.put(stage, keyframes);
            }
            return new KeyframeProvider(map, events);
        }
    }
}
