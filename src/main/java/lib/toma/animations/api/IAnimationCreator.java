package lib.toma.animations.api;

import lib.toma.animations.api.IAnimation;
import net.minecraft.entity.player.PlayerEntity;

public interface IAnimationCreator<A extends IAnimation> {

    A create(PlayerEntity player);
}
