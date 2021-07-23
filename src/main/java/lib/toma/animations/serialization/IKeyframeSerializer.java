package lib.toma.animations.serialization;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import lib.toma.animations.pipeline.frame.IKeyframe;

public interface IKeyframeSerializer extends JsonSerializer<IKeyframe>, JsonDeserializer<IKeyframe> {
}
