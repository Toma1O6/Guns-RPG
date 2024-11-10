package dev.toma.gunsrpg.api.client;

import dev.toma.gunsrpg.api.common.data.IQuestingData;

public interface ScreenDataEventListener {

    void onQuestingDataReceived(IQuestingData questingData);
}
