package lib.toma.animations.api.event;

import lib.toma.animations.api.IAnimation;
import net.minecraft.client.Minecraft;

public interface IAnimationEvent {

    IAnimationEvent[] NO_EVENTS = new IAnimationEvent[0];

    AnimationEventType<?> getType();

    float invokeAt();

    void dispatch(Minecraft client, IAnimation fromAnimation);

    IAnimationEvent copyAt(float target);
}
