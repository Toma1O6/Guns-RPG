package lib.toma.animations.pipeline;

import net.minecraft.entity.player.PlayerEntity;

public interface IAnimationCreator<A extends IAnimation> {

    A create(PlayerEntity player);
}
