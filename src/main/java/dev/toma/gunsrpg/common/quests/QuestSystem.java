package dev.toma.gunsrpg.common.quests;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.quests.reward.QuestRewardManager;
import dev.toma.gunsrpg.util.ILogHandler;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public final class QuestSystem {

    private static final Marker MARKER = MarkerManager.getMarker("Quests");
    private final ILogHandler logger;
    private final QuestRewardManager rewardManager;

    public QuestSystem() {
        this.logger = ILogHandler.wrapLoggerWithMarker(GunsRPG.log, MARKER);
        this.rewardManager = new QuestRewardManager(logger);
    }

    public QuestRewardManager getRewardManager() {
        return rewardManager;
    }

    public void initialize(AddReloadListenerEvent event) {
        logger.info("Starting up quest system");
        event.addListener(rewardManager);
        logger.info("Quest system ready");
    }
}
