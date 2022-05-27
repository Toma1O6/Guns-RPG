package dev.toma.gunsrpg.common.quests.trigger;

import dev.toma.gunsrpg.util.properties.IPropertyReader;

@FunctionalInterface
public interface ITriggerResponder {

    TriggerResponseStatus handleTriggerEvent(Trigger trigger, IPropertyReader reader);
}
