package lib.toma.animations.api;

/**
 * API for hand render settings
 */
public interface IHandRenderAPI extends IHandTransformer {

    /**
     * Checks if developer mode is active for enabling special hand rendering
     * @return If {@link lib.toma.animations.AnimationEngine} is running in developer mode
     */
    boolean isDevMode();
}
