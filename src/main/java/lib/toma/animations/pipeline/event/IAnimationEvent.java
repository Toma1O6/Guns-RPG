package lib.toma.animations.pipeline.event;

import lib.toma.animations.pipeline.IAnimation;
import net.minecraft.client.Minecraft;

public interface IAnimationEvent {

    IAnimationEvent[] NO_EVENTS = new IAnimationEvent[0];

    AnimationEventType<?> getType();

    float invokeAt();

    void dispatch(Minecraft client, IAnimation fromAnimation);
}
