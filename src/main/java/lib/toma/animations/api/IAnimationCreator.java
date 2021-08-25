package lib.toma.animations.api;

import net.minecraft.entity.player.PlayerEntity;

/**
 * Interface responsible for creating new instances of animations. Used by {@link AnimationType}s
 * for automatically creating animations.
 *
 * @param <A> Animation type
 * @author Toma
 */
@FunctionalInterface
public interface IAnimationCreator<A extends IAnimation> {

    /**
     * Creates new Animation instance
     * @param player Client player
     * @return New animation.
     */
    A create(PlayerEntity player);
}
