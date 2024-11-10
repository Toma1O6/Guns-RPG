package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.common.quests.quest.Quest;

import java.util.Optional;

@Deprecated
public interface IQuests {

    ITraderStandings getTraderStandings();

    Optional<Quest<?>> getActiveQuest();

    void assignQuest(Quest<?> quest);

    void clearActiveQuest();
}
