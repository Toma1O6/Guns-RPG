package dev.toma.gunsrpg.client.animation.builder;

import dev.toma.gunsrpg.client.animation.MultiStepAnimation;

public class BuiltAnimation extends MultiStepAnimation {

    public BuiltAnimation(int time) {
        super(time);
        init();
    }

    @Override
    public void createAnimationSteps() {
        float f1 = 0.0F;
        for(BuilderAnimationStep step : BuilderData.steps) {
            float f2 = f1 + step.length;
            addStep(f1, f2, step.resetState());
            f1 = f2;
        }
    }
}
