package dev.toma.gunsrpg.common.quests.tracking;

public interface IProgressionTracker<CTX> {

    void advance(CTX context);

    boolean isComplete();
}
