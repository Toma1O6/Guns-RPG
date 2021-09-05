package dev.toma.gunsrpg.common.quests;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.quests.condition.ConditionListManager;
import dev.toma.gunsrpg.common.quests.condition.QuestConditionManager;
import dev.toma.gunsrpg.util.ILogHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public final class QuestSystem {

    public static final Marker MARKER = MarkerManager.getMarker("Quests");
    private final ILogHandler loggingHandler;
    private final QuestConditionManager conditionManager;
    private final ConditionListManager conditionListManager;

    public QuestSystem() {
        loggingHandler = ILogHandler.wrapLoggerWithMarker(GunsRPG.log, MARKER);
        conditionManager = new QuestConditionManager(loggingHandler);
        conditionListManager = new ConditionListManager(loggingHandler, conditionManager);

        MinecraftForge.EVENT_BUS.addListener(this::registerData);
    }

    private void registerData(AddReloadListenerEvent event) {
        event.addListener(conditionManager);
        event.addListener(conditionListManager);
    }
}
