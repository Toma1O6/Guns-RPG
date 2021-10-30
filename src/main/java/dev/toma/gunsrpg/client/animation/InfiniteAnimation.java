package dev.toma.gunsrpg.client.animation;

import lib.toma.animations.api.Animation;
import lib.toma.animations.api.IKeyframeProvider;

public class InfiniteAnimation extends Animation {

    public InfiniteAnimation(IKeyframeProvider provider, int length) {
        super(provider, length);
    }

    @Override
    public boolean hasFinished() {
        return false;
    }
}
