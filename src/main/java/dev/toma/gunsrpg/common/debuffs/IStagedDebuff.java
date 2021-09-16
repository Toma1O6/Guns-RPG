package dev.toma.gunsrpg.common.debuffs;

public interface IStagedDebuff extends IDebuff {

    boolean canSpread();

    int getCurrentProgress();

    int ticksSinceAdded();

    int ticksSinceProgressed();

    int ticksSinceHealed();
}
