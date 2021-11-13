package dev.toma.gunsrpg.common.item.guns.reload;

import dev.toma.gunsrpg.api.common.IReloader;

public class ClipReloadManager extends FullReloadManager {

    private final FullReloader.IAnimationProvider animationProvider;

    public ClipReloadManager(FullReloader.IAnimationProvider animationProvider) {
        this.animationProvider = animationProvider;
    }

    @Override
    public IReloader createReloadHandler() {
        return new FullReloader(animationProvider);
    }
}
