package lib.toma.animations.pipeline.event;

import lib.toma.animations.serialization.IAnimationEventSerializer;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class AnimationEventType<E extends IAnimationEvent> {

    private static final Map<ResourceLocation, AnimationEventType<?>> TYPES = new HashMap<>();

    public static final AnimationEventType<SoundAnimationEvent> SOUND = new AnimationEventType<>(new ResourceLocation("sound"), new SoundAnimationEvent.Serializer());
    public static final AnimationEventType<PlayAnimationEvent> ANIMATION = new AnimationEventType<>(new ResourceLocation("animation"), new PlayAnimationEvent.Serializer());

    private final ResourceLocation key;
    private final IAnimationEventSerializer<E> serializer;

    public AnimationEventType(ResourceLocation location, IAnimationEventSerializer<E> serializer) {
        this.key = location;
        this.serializer = serializer;
        TYPES.put(location, this);
    }

    public IAnimationEventSerializer<E> serializer() {
        return serializer;
    }

    public ResourceLocation getKey() {
        return key;
    }

    @SuppressWarnings("unchecked")
    public static <E extends IAnimationEvent> AnimationEventType<E> getType(ResourceLocation key) {
        return (AnimationEventType<E>) TYPES.get(key);
    }
}
