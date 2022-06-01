package dev.toma.gunsrpg.common.quests.trigger;

public final class Trigger {

    private static int indexPool;

    public static final Trigger TICK = create();
    public static final Trigger DAMAGE_TAKEN = create();
    public static final Trigger DAMAGE_GIVEN = create();
    public static final Trigger ENTITY_KILLED = create();
    public static final Trigger PLAYER_DIED = create();
    public static final Trigger ITEM_HANDOVER = create();

    private final int triggerIndex;

    private Trigger() {
        this.triggerIndex = indexPool++;
    }

    public static Trigger create() {
        return new Trigger();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trigger trigger = (Trigger) o;
        return triggerIndex == trigger.triggerIndex;
    }

    @Override
    public int hashCode() {
        return triggerIndex;
    }
}
