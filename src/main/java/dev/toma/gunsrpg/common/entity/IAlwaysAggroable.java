package dev.toma.gunsrpg.common.entity;

public interface IAlwaysAggroable {

    void setForcedAggro(boolean status);

    boolean isAggroForced();
}
