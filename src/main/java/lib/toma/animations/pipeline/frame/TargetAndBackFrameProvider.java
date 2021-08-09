package lib.toma.animations.pipeline.frame;

import com.google.gson.*;
import lib.toma.animations.AnimationUtils;
import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.event.IAnimationEvent;
import lib.toma.animations.serialization.IKeyframeTypeSerializer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.util.Map;

public class TargetAndBackFrameProvider implements IKeyframeProvider {

    private final AnimationStage targetStage;
    private final FramePair frames;

    public TargetAndBackFrameProvider(AnimationStage stage, Vector3d position, Vector3f rotation, float rotationDegrees) {
        this.targetStage = stage;
        this.frames = new FramePair(position, rotation, rotationDegrees);
    }

    public TargetAndBackFrameProvider(AnimationStage stage, IKeyframe mov, IKeyframe ret) {
        this.targetStage = stage;
        this.frames = new FramePair(mov, ret);
    }

    @Override
    public boolean shouldAdvance(AnimationStage stage, float progress, int frameIndex) {
        return frameIndex == 0 && progress >= 0.5F;
    }

    @Override
    public IKeyframe getCurrentFrame(AnimationStage stage, float progress, int frameIndex) {
        return frameIndex != 0 ? frames.returning() : frames.toTarget();
    }

    @Override
    public IKeyframe getOldFrame(AnimationStage stage, int frameIndex) {
        return frameIndex != 0 ? frames.toTarget() : Keyframes.none();
    }

    @Override
    public IAnimationEvent[] getEvents() {
        return IAnimationEvent.NO_EVENTS;
    }

    @Override
    public FrameProviderType<?> getType() {
        return FrameProviderType.TARGET_AND_BACK;
    }

    @Override
    public Map<AnimationStage, IKeyframe[]> getFrameMap() {
        Map<AnimationStage, IKeyframe[]> map = AnimationUtils.createSortedMap();
        IKeyframe[] array = new IKeyframe[2];
        array[0] = frames.toTarget();
        array[1] = frames.returning();
        map.put(targetStage, array);
        return map;
    }

    @Override
    public void initCache(Map<AnimationStage, Integer> cache) {
        cache.put(targetStage, 0);
    }

    private static class FramePair {

        private final IKeyframe mov;
        private final IKeyframe ret;

        public FramePair(Vector3d position, Vector3f rotation, float rotationScale) {
            this(
                    new Keyframe(position, new Quaternion(rotation, rotationScale, true), 0.5F),
                    new Keyframe(new Vector3d(-position.x, -position.y, -position.z), new Quaternion(rotation, -rotationScale, true), 1.0F)
            );
        }

        FramePair(IKeyframe mov, IKeyframe ret) {
            this.mov = mov;
            this.ret = ret;
            ret.baseOn(mov);
        }

        public IKeyframe toTarget() {
            return mov;
        }

        public IKeyframe returning() {
            return ret;
        }
    }

    public static final class Serializer implements IKeyframeTypeSerializer<TargetAndBackFrameProvider> {

        @Override
        public void serialize(JsonObject data, TargetAndBackFrameProvider provider, JsonSerializationContext context) {
            FramePair pair = provider.frames;
            data.addProperty("target", provider.targetStage.toString());
            data.add("mov", context.serialize(pair.toTarget(), IKeyframe.class));
            data.add("ret", context.serialize(pair.returning(), IKeyframe.class));
        }

        @Override
        public TargetAndBackFrameProvider deserialize(JsonObject source, JsonDeserializationContext context, IAnimationEvent[] events) throws JsonParseException {
            String stageKey = JSONUtils.getAsString(source, "target");
            AnimationStage stage = AnimationStage.byKey(new ResourceLocation(stageKey));
            if (stage == null) throw new JsonSyntaxException("Unkown animation stage: " + stageKey);
            IKeyframe mov = context.deserialize(JSONUtils.getAsJsonObject(source, "mov"), IKeyframe.class);
            IKeyframe ret = context.deserialize(JSONUtils.getAsJsonObject(source, "ret"), IKeyframe.class);
            return new TargetAndBackFrameProvider(stage, mov, ret);
        }
    }
}
