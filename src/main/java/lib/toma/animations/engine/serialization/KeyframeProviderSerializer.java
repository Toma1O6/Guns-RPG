package lib.toma.animations.engine.serialization;

import com.google.gson.*;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import lib.toma.animations.QuickSort;
import lib.toma.animations.api.IKeyframeProvider;
import lib.toma.animations.api.IKeyframeTypeSerializer;
import lib.toma.animations.api.event.AnimationEventType;
import lib.toma.animations.api.event.IAnimationEvent;
import lib.toma.animations.api.event.IAnimationEventSerializer;
import lib.toma.animations.api.lifecycle.Registries;
import lib.toma.animations.engine.frame.FrameProviderType;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;
import java.util.Comparator;

public class KeyframeProviderSerializer implements JsonSerializer<IKeyframeProvider>, JsonDeserializer<IKeyframeProvider> {

    @Override
    public JsonElement serialize(IKeyframeProvider src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        FrameProviderType<?> type = src.getType();
        IAnimationEvent[] events = src.getEvents();
        object.addProperty("type", type.getKey());
        if (type.areEventsSupported() && events.length > 0) {
            object.add("events", serializeEvents(events, context));
        }
        object.add("data", serializeType(type, src, context));
        return object;
    }

    @Override
    public IKeyframeProvider deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject())
            throw new JsonSyntaxException("Not a Json object!");
        JsonObject object = json.getAsJsonObject();
        String typeKey = JSONUtils.getAsString(object, "type");
        FrameProviderType<?> type = FrameProviderType.getType(typeKey);
        if (type == null) throw new JsonSyntaxException("Unknown frame provider type: " + typeKey);
        IKeyframeTypeSerializer<?> serializer = type.serializer();
        IAnimationEvent[] events = IAnimationEvent.NO_EVENTS;
        if (type.areEventsSupported() && object.has("events")) {
            events = deserializeEvents(JSONUtils.getAsJsonArray(object, "events"), context);
        }
        return serializer.deserialize(JSONUtils.getAsJsonObject(object, "data"), context, events);
    }

    @SuppressWarnings("unchecked")
    private <FP extends IKeyframeProvider> JsonElement serializeType(FrameProviderType<FP> type, IKeyframeProvider source, JsonSerializationContext context) {
        IKeyframeTypeSerializer<FP> serializer = type.serializer();
        JsonObject object = new JsonObject();
        serializer.serialize(object, (FP) source, context);
        return object;
    }

    private <E extends IAnimationEvent> IAnimationEvent[] deserializeEvents(JsonArray src, JsonDeserializationContext context) throws JsonParseException {
        IAnimationEvent[] events = new IAnimationEvent[src.size()];
        for (int i = 0; i < src.size(); i++) {
            JsonElement element = src.get(i);
            if (!element.isJsonObject())
                throw new JsonSyntaxException("Not a Json object!");
            JsonObject object = element.getAsJsonObject();
            ResourceLocation typeKey = new ResourceLocation(JSONUtils.getAsString(object, "type"));
            AnimationEventType<E> type = (AnimationEventType<E>) Registries.EVENTS.getElement(typeKey);
            if (type == null)
                throw new JsonSyntaxException("Unknown event type: " + typeKey);
            IAnimationEventSerializer<E> serializer = type.serializer();
            float target = JSONUtils.getAsFloat(object, "target");
            E event = serializer.deserialize(target, JsonHelper.get(object, "data"), context);
            events[i] = event;
        }
        QuickSort.sort(events, Comparator.comparingDouble(IAnimationEvent::invokeAt));
        return events;
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
