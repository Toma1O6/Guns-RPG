package lib.toma.animations.api.event;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;

public interface IAnimationEventSerializer<E extends IAnimationEvent> {

    JsonElement serialize(E event, JsonSerializationContext context);

    E deserialize(float target, JsonElement src, JsonDeserializationContext context) throws JsonParseException;
}
