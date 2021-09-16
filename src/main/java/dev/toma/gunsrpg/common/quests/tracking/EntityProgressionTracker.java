package dev.toma.gunsrpg.common.quests.tracking;

import net.minecraft.entity.Entity;

public class EntityProgressionTracker implements IProgressionTracker<Entity> {

    private final int target;
    private int killed;

    public EntityProgressionTracker(int target) {
        this.target = target;
    }

    @Override
    public void advance(Entity context) {
        ++killed;
    }

    @Override
    public boolean isComplete() {
        return killed >= target;
    }
}
