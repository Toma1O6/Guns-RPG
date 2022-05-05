package dev.toma.gunsrpg.common.quests.trigger;

import dev.toma.gunsrpg.util.properties.IPropertyReader;

public interface ITriggerListener {

    TriggerResponseStatus handleTriggerEvent(Trigger trigger, IPropertyReader reader);
}
