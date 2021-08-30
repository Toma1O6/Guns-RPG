package dev.toma.gunsrpg.api.common;

import dev.toma.gunsrpg.api.common.IReloader;

public interface IReloadManager {

    boolean isCancelable();

    IReloader createReloadHandler();
}
