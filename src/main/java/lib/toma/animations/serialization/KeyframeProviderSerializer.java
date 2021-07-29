package lib.toma.animations.serialization;

import com.google.gson.*;
import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.IKeyframeProvider;
import lib.toma.animations.pipeline.event.AnimationEventType;
import lib.toma.animations.pipeline.event.IAnimationEvent;
import lib.toma.animations.pipeline.frame.IKeyframe;
import lib.toma.animations.pipeline.frame.KeyframeProvider;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class KeyframeProviderSerializer implements JsonSerializer<IKeyframeProvider>, JsonDeserializer<IKeyframeProvider> {

    @Override
    public JsonElement serialize(IKeyframeProvider src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        Map<AnimationStage, IKeyframe[]> frames = src.getFrames();
        IAnimationEvent[] events = src.animationEventsSorted();
        object.add("frames", serializeFrames(frames, context));
        if (events.length != 0) {
            object.add("events", serializeEvents(events, context));
        }
        return object;
    }

    @Override
    public IKeyframeProvider deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonSyntaxException("Not a Json object!");
        JsonObject object = json.getAsJsonObject();
        if (!object.has("frames"))
            throw new JsonSyntaxException("Missing keyframe definition");
        Map<AnimationStage, IKeyframe[]> frames = deserializeFrames(object.getAsJsonObject("frames"), context);
        IAnimationEvent[] events = object.has("events") ? deserializeEvents(object.getAsJsonArray("events"), context) : IAnimationEvent.NO_EVENTS;
        return new KeyframeProvider(frames, events);
    }

    private Map<AnimationStage, IKeyframe[]> deserializeFrames(JsonObject src, JsonDeserializationContext context) throws JsonParseException {
        Map<AnimationStage, IKeyframe[]> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : src.entrySet()) {
            AnimationStage stage = AnimationStage.byKey(new ResourceLocation(entry.getKey()));
            if (stage == null)
                throw new JsonParseException("Unknown animation stage: " + entry.getKey());
            JsonElement element = entry.getValue();
            if (!element.isJsonArray())
                throw new JsonSyntaxException("Not a Json array!");
            JsonArray array = element.getAsJsonArray();
            IKeyframe[] frames = new IKeyframe[array.size()];
            for (int i = 0; i < array.size(); i++) {
                frames[i] = context.deserialize(array.get(i), IKeyframe.class);
            }
            map.put(stage, frames);
        }
        return map;
    }

    private <E extends IAnimationEvent> IAnimationEvent[] deserializeEvents(JsonArray src, JsonDeserializationContext context) throws JsonParseException {
        IAnimationEvent[] events = new IAnimationEvent[src.size()];
        for (int i = 0; i < src.size(); i++) {
            JsonElement element = src.get(i);
            if (!element.isJsonObject())
                throw new JsonSyntaxException("Not a Json object!");
            JsonObject object = element.getAsJsonObject();
            ResourceLocation typeKey = new ResourceLocation(JSONUtils.getAsString(object, "type"));
            AnimationEventType<E> type = AnimationEventType.getType(typeKey);
            if (type == null)
                throw new JsonSyntaxException("Unknown event type: " + typeKey);
            IAnimationEventSerializer<E> serializer = type.serializer();
            float target = JSONUtils.getAsFloat(object, "target");
            E event = serializer.deserialize(target, JSONUtils.getAsJsonObject(object, "data"), context);
            events[i] = event;
        }
        return events;
    }

    private JsonObject serializeFrames(Map<AnimationStage, IKeyframe[]> map, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        for (Map.Entry<AnimationStage, IKeyframe[]> entry : map.entrySet()) {
            AnimationStage stage = entry.getKey();
            IKeyframe[] frames = entry.getValue();
            JsonArray array = new JsonArray();
            for (IKeyframe frame : frames) {
                array.add(context.serialize(frame, IKeyframe.class));
            }
            object.add(stage.getKey().toString(), array);
        }
        return object;
    }

    @SuppressWarnings("unchecked")
    private <E extends IAnimationEvent> JsonArray serializeEvents(IAnimationEvent[] events, JsonSerializationContext context) {
        JsonArray array = new JsonArray();
        for (IAnimationEvent iAnimationEvent : events) {
            JsonObject object = new JsonObject();
            E event = (E) iAnimationEvent;
            AnimationEventType<E> type = (AnimationEventType<E>) event.getType();
            IAnimationEventSerializer<E> serializer = type.serializer();
            object.addProperty("type", type.getKey().toString());
            object.addProperty("target", event.invokeAt());
            object.add("data", serializer.serialize(event, context));
            array.add(object);
        }
        return array;
    }
}
