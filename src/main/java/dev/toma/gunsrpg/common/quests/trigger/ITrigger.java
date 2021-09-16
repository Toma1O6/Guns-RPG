package dev.toma.gunsrpg.common.quests.trigger;

import com.google.gson.JsonObject;
import dev.toma.gunsrpg.common.quests.tracking.IProgressionTracker;

public interface ITrigger<CTX> {

    TriggerType<CTX> getType();

    boolean isValid(CTX context);

    IProgressionTracker<CTX> createTracker(JsonObject taskObj);
}
