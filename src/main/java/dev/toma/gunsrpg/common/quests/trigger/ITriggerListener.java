package dev.toma.gunsrpg.common.quests.trigger;

import dev.toma.gunsrpg.util.properties.IPropertyReader;

public interface ITriggerListener {

    void onTrigger(IPropertyReader reader);
}
