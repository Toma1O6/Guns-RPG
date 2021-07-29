package lib.toma.animations.pipeline.event;

import lib.toma.animations.pipeline.IAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public interface IAnimationEvent {

    AnimationEventType<?> getType();

    float invokeAt();

    void dispatchEvent(Minecraft client, IAnimation fromAnimation);
}
