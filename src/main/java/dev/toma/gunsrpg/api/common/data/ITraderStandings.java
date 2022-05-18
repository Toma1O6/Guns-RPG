package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.common.quests.quest.Quest;

import java.util.UUID;

public interface ITraderStandings {

    ITraderStatus getStatusWithTrader(UUID traderId);

    void questFinished(UUID traderId, Quest<?> quest);

    void questFailed(UUID traderId, Quest<?> quest);
}
