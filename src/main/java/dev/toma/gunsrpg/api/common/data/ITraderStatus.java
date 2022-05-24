package dev.toma.gunsrpg.api.common.data;

public interface ITraderStatus {

    float getReputation();

    void addReputation(float reputation);

    void onTraderAttacked();
}
