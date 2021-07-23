package lib.toma.animations.pipeline;

import net.minecraft.client.entity.player.ClientPlayerEntity;

public interface IAnimationCreator<A extends IAnimation> {

    A create(ClientPlayerEntity player);
}
