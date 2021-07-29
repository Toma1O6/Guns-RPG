package lib.toma.animations.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import lib.toma.animations.pipeline.event.IAnimationEvent;

public interface IAnimationEventSerializer<E extends IAnimationEvent> {

    JsonElement serialize(E event, JsonSerializationContext context);

    E deserialize(float target, JsonElement src, JsonDeserializationContext context) throws JsonParseException;
}
