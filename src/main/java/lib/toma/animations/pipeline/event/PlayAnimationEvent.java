package lib.toma.animations.pipeline.event;

import com.google.common.base.Preconditions;
import com.google.gson.*;
import lib.toma.animations.AnimationEngine;
import lib.toma.animations.pipeline.AnimationType;
import lib.toma.animations.pipeline.IAnimation;
import lib.toma.animations.pipeline.IAnimationPipeline;
import lib.toma.animations.serialization.IAnimationEventSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class PlayAnimationEvent extends AbstractAnimationEvent {

    private final AnimationType<?> type;
    private final int playDelay;

    public PlayAnimationEvent(float target, AnimationType<?> type) {
        this(target, type, 0);
    }

    public PlayAnimationEvent(float target, AnimationType<?> type, int playDelay) {
        this(AnimationEventType.ANIMATION, target, type, playDelay);
    }

    protected PlayAnimationEvent(AnimationEventType<? extends PlayAnimationEvent> eventType, float target, AnimationType<?> type, int playDelay) {
        super(eventType, target);
        this.type = type;
        this.playDelay = playDelay;
        Preconditions.checkState(type.hasCreator(), "This type doesn't have custom animation creator!");
    }

    @Override
    public void dispatch(Minecraft client, IAnimation fromAnimation) {
        IAnimationPipeline pipeline = AnimationEngine.get().pipeline();
        if (playDelay == 0)
            pipeline.insert(type);
        else
            pipeline.scheduleInsert(type, playDelay);
    }

    public static final class Serializer implements IAnimationEventSerializer<PlayAnimationEvent> {

        @Override
        public JsonElement serialize(PlayAnimationEvent event, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("key", event.type.getName().toString());
            if (event.playDelay > 0)
                object.addProperty("delay", event.playDelay);
            return object;
        }

        @Override
        public PlayAnimationEvent deserialize(float target, JsonElement src, JsonDeserializationContext context) {
            if (!src.isJsonObject())
                throw new JsonSyntaxException("Not a Json object!");
            JsonObject object = src.getAsJsonObject();
            ResourceLocation location = new ResourceLocation(JSONUtils.getAsString(object, "key"));
            AnimationType<?> type = AnimationType.getTypeFromID(location);
            if (type == null)
                throw new JsonSyntaxException("Unknown animation type: " + location);
            int delay = JSONUtils.getAsInt(object, "delay", 0);
            return new PlayAnimationEvent(target, type, delay);
        }
    }
}
