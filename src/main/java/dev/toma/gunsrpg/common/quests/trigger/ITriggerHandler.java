package dev.toma.gunsrpg.common.quests.trigger;

import dev.toma.gunsrpg.util.properties.IPropertyReader;

@FunctionalInterface
public interface ITriggerHandler {

    ITriggerHandler NONE = (trigger, reader) -> {};

    void handleTriggerSuccess(Trigger trigger, IPropertyReader reader);
}
