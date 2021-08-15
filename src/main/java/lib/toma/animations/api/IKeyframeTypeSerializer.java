package lib.toma.animations.api;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import lib.toma.animations.api.event.IAnimationEvent;
import lib.toma.animations.api.IKeyframeProvider;

public interface IKeyframeTypeSerializer<FP extends IKeyframeProvider> {

    void serialize(JsonObject data, FP provider, JsonSerializationContext context);

    FP deserialize(JsonObject source, JsonDeserializationContext context, IAnimationEvent[] events) throws JsonParseException;
}
