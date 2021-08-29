package dev.toma.gunsrpg.common.item.guns.reload;

public interface IReloadManager {

    boolean isCancelable();

    IReloader createReloadHandler();
}
