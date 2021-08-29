package dev.toma.gunsrpg.client.animation;

import lib.toma.animations.api.Animation;
import lib.toma.animations.api.IKeyframeProvider;

public class ReloadAnimation extends Animation implements IModifiableProgress {

    public ReloadAnimation(IKeyframeProvider provider, int reloadTicks) {
        super(provider, reloadTicks);
    }
}
