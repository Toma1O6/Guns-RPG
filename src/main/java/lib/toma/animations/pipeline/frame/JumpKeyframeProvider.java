package lib.toma.animations.pipeline.frame;

import com.google.common.collect.ImmutableMap;
import lib.toma.animations.pipeline.AnimationStage;
import lib.toma.animations.pipeline.IKeyframeProvider;
import lib.toma.animations.pipeline.event.IAnimationEvent;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.util.Map;

public class JumpKeyframeProvider implements IKeyframeProvider {

    private final Map<AnimationStage, IKeyframe[]> frames;

    public JumpKeyframeProvider(AnimationStage stage, Vector3d position, Vector3f rotation, float rotationDegrees) {
        IKeyframe f1 = new PositionRotateKeyframe(position, new Quaternion(rotation, rotationDegrees, true), 0.5F);
        IKeyframe f2 = new PositionRotateKeyframe(new Vector3d(-position.x, -position.y, -position.z), new Quaternion(rotation, -rotationDegrees, true), 1.0F);
        frames = new ImmutableMap.Builder<AnimationStage, IKeyframe[]>().put(stage, new IKeyframe[] {f1, f2}).build();
    }

    @Override
    public Map<AnimationStage, IKeyframe[]> getFrames() {
        return frames;
    }

    @Override
    public IAnimationEvent[] animationEventsSorted() {
        return IAnimationEvent.NO_EVENTS;
    }

    @Override
    public byte[] getCacheStorage() {
        return new byte[2];
    }
}
