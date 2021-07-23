package lib.toma.animations.serialization;

import lib.toma.animations.pipeline.frame.EmptyKeyframe;

public class KeyframeSerializers {
    public static final IKeyframeSerializer EMPTY_FRAME_SERIALIZER = new EmptyKeyframe.EmptyKeyframeSerializer();
}
