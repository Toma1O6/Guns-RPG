package lib.toma.animations.api;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import lib.toma.animations.api.event.IAnimationEvent;
import lib.toma.animations.api.IKeyframeProvider;

/**
 * Handles serialization of {@link IKeyframeProvider}
 * @param <FP> {@link IKeyframeProvider} type
 *
 * @author Toma
 */
public interface IKeyframeTypeSerializer<FP extends IKeyframeProvider> {

    /**
     * Serializes internal data into JsonObject structure.
     * @param data Empty JsonObject for data
     * @param provider Keyframe provider instance
     * @param context Serialization context which allows you to serialize complex structures
     */
    void serialize(JsonObject data, FP provider, JsonSerializationContext context);

    /**
     * Deserializes data from JsonObject source into new keyframe provider instance.
     * @param source Data source
     * @param context Deserialization context for deserializing complex data structures
     * @param events Array of parsed events
     * @return New instance with data which have been deserialized from json
     * @throws JsonParseException When corrupt/incorrect data are found
     */
    FP deserialize(JsonObject source, JsonDeserializationContext context, IAnimationEvent[] events) throws JsonParseException;
}
