package lib.toma.animations.pipeline.frame;

import lib.toma.animations.serialization.IKeyframeTypeSerializer;

import java.util.HashMap;
import java.util.Map;

public final class FrameProviderType<FP extends IKeyframeProvider> {

    public static final FrameProviderType<NoFramesProvider> EMPTY = new FrameProviderType<>("empty", new NoFramesProvider.Serializer());
    public static final FrameProviderType<SingleFrameProvider> SINGLE = new FrameProviderType<>("single_frame", new SingleFrameProvider.Serializer());
    public static final FrameProviderType<TargetAndBackFrameProvider> TARGET_AND_BACK = new FrameProviderType<>("target_back", new TargetAndBackFrameProvider.Serializer());
    public static final FrameProviderType<KeyframeProvider> KEYFRAME_PROVIDER_TYPE = new FrameProviderType<>("default", new KeyframeProvider.Serializer()).allowEvents();

    private static final Map<String, FrameProviderType<?>> TYPE_REGISTRY = new HashMap<>();

    private final String typeKey;
    private final IKeyframeTypeSerializer<FP> serializer;
    private boolean supportEvents = false;

    public FrameProviderType(String typeKey, IKeyframeTypeSerializer<FP> serializer) {
        this.typeKey = typeKey;
        this.serializer = serializer;
        if (TYPE_REGISTRY.put(typeKey, this) != null) {
            throw new UnsupportedOperationException("Duplicate frame provider type key: " + typeKey);
        }
    }

    public String getKey() {
        return typeKey;
    }

    public IKeyframeTypeSerializer<FP> serializer() {
        return serializer;
    }

    public FrameProviderType<FP> allowEvents() {
        this.supportEvents = true;
        return this;
    }

    public boolean areEventsSupported() {
        return supportEvents;
    }

    @SuppressWarnings("unchecked")
    public static <T extends IKeyframeProvider> FrameProviderType<T> getType(String key) {
        return (FrameProviderType<T>) TYPE_REGISTRY.get(key);
    }
}
