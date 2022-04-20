package dev.toma.gunsrpg.common.quests;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.quests.condition.QuestConditionManager;
import dev.toma.gunsrpg.common.quests.condition.list.QuestConditionListManager;
import dev.toma.gunsrpg.common.quests.reward.QuestRewardManager;
import dev.toma.gunsrpg.util.ILogHandler;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public final class QuestSystem {

    private static final Marker MARKER = MarkerManager.getMarker("Quests");
    private final ILogHandler logger;
    private final QuestRewardManager rewardManager;
    private final QuestConditionManager conditionManager;
    private final QuestConditionListManager conditionListManager;

    public QuestSystem() {
        this.logger = ILogHandler.wrapLoggerWithMarker(GunsRPG.log, MARKER);
        this.rewardManager = new QuestRewardManager(logger);
        this.conditionManager = new QuestConditionManager(logger);
        this.conditionListManager = new QuestConditionListManager(logger, this.conditionManager);
    }

    public QuestRewardManager getRewardManager() {
        return rewardManager;
    }

    public QuestConditionManager getConditionManager() {
        return conditionManager;
    }

    public QuestConditionListManager getConditionListManager() {
        return conditionListManager;
    }

    public void initialize(AddReloadListenerEvent event) {
        logger.info("Starting up quest system");
        event.addListener(rewardManager);
        event.addListener(conditionManager);
        event.addListener(conditionListManager);
    }
}
