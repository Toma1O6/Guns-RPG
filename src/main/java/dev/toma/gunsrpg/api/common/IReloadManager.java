package dev.toma.gunsrpg.api.common;

public interface IReloadManager {

    boolean isCancelable();

    IReloader createReloadHandler();
}
