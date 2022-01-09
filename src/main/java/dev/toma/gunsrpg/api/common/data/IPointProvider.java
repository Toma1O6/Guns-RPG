package dev.toma.gunsrpg.api.common.data;

public interface IPointProvider {

    int getPoints();

    void awardPoints(int amount);
}
