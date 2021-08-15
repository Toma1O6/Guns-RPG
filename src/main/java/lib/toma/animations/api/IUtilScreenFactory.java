package lib.toma.animations.api;

import lib.toma.animations.engine.screen.HandRenderScreen;
import lib.toma.animations.engine.screen.animator.AnimatorScreen;

public interface IUtilScreenFactory {

    HandRenderScreen getHandConfigScreen();

    AnimatorScreen getAnimatorScreen();
}
