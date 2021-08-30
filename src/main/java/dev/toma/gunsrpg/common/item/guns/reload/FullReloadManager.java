package dev.toma.gunsrpg.common.item.guns.reload;

import dev.toma.gunsrpg.api.common.IReloadManager;
import dev.toma.gunsrpg.api.common.IReloader;

public class FullReloadManager implements IReloadManager {

    @Override
    public boolean isCancelable() {
        return false;
    }

    @Override
    public IReloader createReloadHandler() {
        return new FullReloader();
    }
}
