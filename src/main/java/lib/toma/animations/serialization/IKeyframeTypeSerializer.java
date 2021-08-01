package lib.toma.animations.serialization;

import com.google.gson.*;
import lib.toma.animations.pipeline.event.IAnimationEvent;
import lib.toma.animations.pipeline.frame.IKeyframeProvider;

public interface IKeyframeTypeSerializer<FP extends IKeyframeProvider> {

    void serialize(JsonObject data, FP provider, JsonSerializationContext context);

    FP deserialize(JsonObject source, JsonDeserializationContext context, IAnimationEvent[] events) throws JsonParseException;
}
