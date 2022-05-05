package dev.toma.gunsrpg.common.quests.quest;

import java.util.UUID;

public interface IQuest<D> {

    UUID getQuestInitiator();

    D getActiveData();

    void onAssigned();

    void onCompleted();

    void onFailed();
}
