package dev.toma.gunsrpg.common.item.guns.reload;

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
