package dev.toma.gunsrpg.common.quests.trigger;

import dev.toma.gunsrpg.util.properties.PropertyKey;

public final class Triggers {

    public static final PropertyKey<TriggerType> TRIGGER = PropertyKey.newKey("trigger");

    public static final TriggerType TICK = new TriggerType();
    public static final TriggerType ENTITY_KILLED = new TriggerType();
    public static final TriggerType DAMAGE_TAKEN = new TriggerType();
    public static final TriggerType ITEM_HANDOVER = new TriggerType();

    private Triggers() {}
}
