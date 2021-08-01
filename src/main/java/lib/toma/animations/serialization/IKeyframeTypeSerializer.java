package lib.toma.animations.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import lib.toma.animations.pipeline.event.IAnimationEvent;
import lib.toma.animations.pipeline.frame.IKeyframeProvider;

public interface IKeyframeTypeSerializer<FP extends IKeyframeProvider> {

    void serialize(JsonObject data, FP provider, JsonSerializationContext context);

    FP deserialize(JsonObject source, JsonDeserializationContext context, IAnimationEvent[] events) throws JsonParseException;
}
