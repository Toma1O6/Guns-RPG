package lib.toma.animations;

import lib.toma.animations.screen.HandRenderScreen;
import lib.toma.animations.screen.animator.AnimatorScreen;

public interface IUtilScreenFactory {

    HandRenderScreen getHandConfigScreen();

    AnimatorScreen getAnimatorScreen();
}
