package dev.toma.gunsrpg.api.common.data;

public interface IJamInfo {

    boolean isUnjamming();

    void setUnjamming(boolean unjamming);

    void startUnjamming(int slot, int time);

    void tick();
}
