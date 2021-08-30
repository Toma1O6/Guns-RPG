package dev.toma.gunsrpg.api.common.data;

public interface IAimInfo {

    void update();

    boolean startedAiming();

    boolean isAiming();

    void setAiming(boolean aiming);

    float getProgress();

    float getProgress(float deltaTime);
}
