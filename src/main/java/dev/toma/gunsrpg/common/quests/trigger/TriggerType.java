package dev.toma.gunsrpg.common.quests.trigger;

import dev.toma.gunsrpg.util.properties.IPropertyHolder;

import java.util.HashSet;
import java.util.Set;

public final class TriggerType {

    private final Set<ITriggerListener> listeners = new HashSet<>();

    public void trigger(IPropertyHolder propertyHolder) {
        propertyHolder.setProperty(Triggers.TRIGGER, this);
        listeners.forEach(listener -> listener.onTrigger(propertyHolder));
    }

    public void addListener(ITriggerListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ITriggerListener listener) {
        listeners.remove(listener);
    }
}
