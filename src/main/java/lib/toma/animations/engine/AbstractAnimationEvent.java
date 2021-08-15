package lib.toma.animations.engine;

import com.google.common.base.Preconditions;
import lib.toma.animations.api.event.AnimationEventType;
import lib.toma.animations.api.event.IAnimationEvent;

public abstract class AbstractAnimationEvent implements IAnimationEvent {

    private final AnimationEventType<?> type;
    private final float target;

    public AbstractAnimationEvent(AnimationEventType<?> type, float target) {
        Preconditions.checkState(target >= 0 && target <= 1.0F, "Target value must be in range of <0 ; 1>! Got " + target);
        this.type = type;
        this.target = target;
    }

    @Override
    public AnimationEventType<?> getType() {
        return type;
    }

    @Override
    public float invokeAt() {
        return target;
    }
}
