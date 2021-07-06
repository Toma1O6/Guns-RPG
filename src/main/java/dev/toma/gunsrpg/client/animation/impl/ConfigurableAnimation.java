package dev.toma.gunsrpg.client.animation.impl;

import dev.toma.gunsrpg.client.animation.IAnimation;
import dev.toma.gunsrpg.client.animation.MultiStepAnimation;
import dev.toma.gunsrpg.util.object.Pair;

import java.util.List;

public class ConfigurableAnimation extends MultiStepAnimation {

    private final ILoader loader;

    public ConfigurableAnimation(int time, ILoader loader) {
        super(time);
        this.loader = loader;
    }

    @Override
    public void createAnimationSteps() {
        loader.load(steps);
    }

    public interface ILoader {
        void load(List<Pair<Range, IAnimation>> animationSteps);
    }
}
