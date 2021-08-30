package dev.toma.gunsrpg.client.animation;

import dev.toma.gunsrpg.api.client.IModifiableProgress;
import lib.toma.animations.api.Animation;
import lib.toma.animations.api.IKeyframeProvider;

public class ReloadAnimation extends Animation implements IModifiableProgress {

    public ReloadAnimation(IKeyframeProvider provider, int reloadTicks) {
        super(provider, reloadTicks);
    }
}
