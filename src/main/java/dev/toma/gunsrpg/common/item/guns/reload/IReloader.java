package dev.toma.gunsrpg.common.item.guns.reload;

public interface IReloader {

    void startReload();

    void stopReload();

    boolean canStopReload();

    void tick();


}
