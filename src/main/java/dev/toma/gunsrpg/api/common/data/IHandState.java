package dev.toma.gunsrpg.api.common.data;

public interface IHandState {

    void setHandsBusy();

    void freeHands();

    boolean areHandsBusy();
}
