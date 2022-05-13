package dev.toma.gunsrpg.common.quests;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.quests.condition.QuestConditionLoader;
import dev.toma.gunsrpg.common.quests.condition.list.QuestConditionListManager;
import dev.toma.gunsrpg.common.quests.quest.QuestManager;
import dev.toma.gunsrpg.common.quests.reward.QuestRewardManager;
import dev.toma.gunsrpg.util.ILogHandler;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public final class QuestSystem {

    private static final Marker MARKER = MarkerManager.getMarker("Quests");
    private final ILogHandler logger;
    private final QuestRewardManager rewardManager;
    private final QuestConditionLoader conditionLoader;
    private final QuestConditionListManager conditionListManager;
    private final QuestManager questManager;

    public QuestSystem() {
        this.logger = ILogHandler.wrapLoggerWithMarker(GunsRPG.log, MARKER);
        this.rewardManager = new QuestRewardManager(logger);
        this.conditionLoader = new QuestConditionLoader();
        this.conditionListManager = new QuestConditionListManager(logger, this.conditionLoader);
        this.questManager = new QuestManager(logger, conditionListManager, conditionLoader);
    }

    public QuestRewardManager getRewardManager() {
        return rewardManager;
    }

    public QuestConditionLoader getConditionLoader() {
        return conditionLoader;
    }

    public QuestConditionListManager getConditionListManager() {
        return conditionListManager;
    }

    public QuestManager getQuestManager() {
        return questManager;
    }

    public void initialize(AddReloadListenerEvent event) {
        logger.info("Starting up quest system");
        event.addListener(rewardManager);
        event.addListener(conditionListManager);
        event.addListener(questManager);
    }
}
