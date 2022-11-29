package dev.toma.gunsrpg.integration.questing;

import dev.toma.gunsrpg.integration.questing.loader.QuestRewardLoader;
import net.minecraftforge.event.AddReloadListenerEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public final class QuestDataManager {

    public static final Marker MARKER = MarkerManager.getMarker("QuestManager");

    public final QuestRewardLoader rewardLoader = new QuestRewardLoader();

    public void registerDatapackLoaders(AddReloadListenerEvent event) {
        event.addListener(this.rewardLoader);
    }
}
