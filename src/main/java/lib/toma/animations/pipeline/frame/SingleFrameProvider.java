package lib.toma.animations.pipeline.frame;

import com.google.gson.*;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.event.IAnimationEvent;
import lib.toma.animations.serialization.IKeyframeTypeSerializer;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SingleFrameProvider implements IKeyframeProvider {

    private final Map<AnimationStage, IKeyframe> frames;
    private final int cacheSize;

    public SingleFrameProvider(IFrameConstructor frameConstructor) {
        FrameBuilder builder = new FrameBuilder();
        frameConstructor.construct(builder);
        if (!builder.hasAddedFrame()) {
            throw new IllegalArgumentException("Cannot construct empty keyframe provider!");
        }
        frames = builder.frames;
        cacheSize = AnimationUtils.getBiggestFromMap(frames, Map::keySet, AnimationStage::getIndex);
    }

    /**
     * Constructor for JSON deserializer
     * @param frames Loaded frames
     * @throws JsonParseException Thrown when frame map is empty
     */
    private SingleFrameProvider(Map<AnimationStage, IKeyframe> frames) {
        this.frames = frames;
        this.cacheSize = AnimationUtils.getBiggestFromMap(frames, Map::keySet, AnimationStage::getIndex);
    }

    public static SingleFrameProvider fromExistingMap(Map<AnimationStage, IKeyframe> frames) {
        return new SingleFrameProvider(frames);
    }

    static SingleFrameProvider jsonLoaded(Map<AnimationStage, IKeyframe> frames) throws JsonParseException {
        if (frames.isEmpty())
            throw new JsonSyntaxException("Single frame provider must contain atleast one keyframe");
        return new SingleFrameProvider(frames);
    }

    @Override
    public IKeyframe getCurrentFrame(AnimationStage stage, float progress, byte frameIndex) {
        return AnimationUtils.safeRet(frames, stage, Keyframes.none());
    }

    @Override
    public IKeyframe getOldFrame(AnimationStage stage, byte frameIndex) {
        return getCurrentFrame(stage, 0, frameIndex);
    }

    @Override
    public boolean shouldAdvance(AnimationStage stage, float progress, byte frameIndex) {
        return false;
    }

    @Override
    public IAnimationEvent[] getEvents() {
        return IAnimationEvent.NO_EVENTS;
    }

    @Override
    public int getCacheSize() {
        return cacheSize;
    }

    @Override
    public FrameProviderType<?> getType() {
        return FrameProviderType.SINGLE;
    }

    @Override
    public Map<AnimationStage, IKeyframe[]> getFrameMap() {
        Map<AnimationStage, IKeyframe[]> map = AnimationUtils.createSortedMap();
        frames.forEach((k, v) -> map.put(k, new IKeyframe[] {v}));
        return map;
    }

    @FunctionalInterface
    public interface IFrameConstructor {
        void construct(FrameBuilder builder);
    }

    public static class FrameBuilder {
        private final Map<AnimationStage, IKeyframe> frames = new HashMap<>();
        private boolean modified;

        public FrameBuilder define(AnimationStage stage, IKeyframe iKeyframe) {
            if (frames.put(stage, iKeyframe) != null)
                throw new UnsupportedOperationException("Cannot add another frame definition for " + stage.getKey().toString() + " stage!");
            modified = true;
            return this;
        }

        private boolean hasAddedFrame() {
            return modified;
        }
    }

    public static class Serializer implements IKeyframeTypeSerializer<SingleFrameProvider> {

        @Override
        public void serialize(JsonObject data, SingleFrameProvider provider, JsonSerializationContext context) {
            for (Map.Entry<AnimationStage, IKeyframe> entry : provider.frames.entrySet()) {
                data.add(entry.getKey().toString(), context.serialize(entry.getValue(), IKeyframe.class));
            }
        }

        @Override
        public SingleFrameProvider deserialize(JsonObject source, JsonDeserializationContext context, IAnimationEvent[] events) throws JsonParseException {
            Map<AnimationStage, IKeyframe> map = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : source.entrySet()) {
                String stageKey = entry.getKey();
                JsonElement element = entry.getValue();
                AnimationStage stage = AnimationStage.byKey(new ResourceLocation(stageKey));
                if (stage == null)
                    throw new JsonSyntaxException("Unknown animation stage: " + stageKey);
                if (!element.isJsonObject())
                    throw new JsonSyntaxException("Not a Json object!");
                IKeyframe frame = context.deserialize(element, IKeyframe.class);
                map.put(stage, Objects.requireNonNull(frame));
            }
            return SingleFrameProvider.jsonLoaded(map);
        }
    }
}
