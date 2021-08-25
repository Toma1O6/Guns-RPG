package lib.toma.animations.api;

/**
 * Marks all items which implement this API as animation ready - it will be rendered using
 * custom render pipeline.
 *
 * @author Toma
 */
public interface IAnimationEntry extends IHandTransformer {

    /**
     * Allows you to disable vanilla animations such as equip animation and swing animation.
     * @return Whether vanilla animations should be disabled for this implementation.
     */
    boolean disableVanillaAnimations();
}
