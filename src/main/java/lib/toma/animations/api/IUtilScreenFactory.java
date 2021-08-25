package lib.toma.animations.api;

import lib.toma.animations.engine.screen.HandRenderScreen;
import lib.toma.animations.engine.screen.animator.AnimatorScreen;

/**
 * Interface handling creating of dev tool screens.
 *
 * @author Toma
 */
public interface IUtilScreenFactory {

    /**
     * @return new HandRenderScreen instance
     */
    HandRenderScreen getHandConfigScreen();

    /**
     * @return new AnimatorScreen instance
     */
    AnimatorScreen getAnimatorScreen();
}
