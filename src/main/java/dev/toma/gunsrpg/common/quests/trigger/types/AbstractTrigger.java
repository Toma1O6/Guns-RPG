package dev.toma.gunsrpg.common.quests.trigger.types;

import dev.toma.gunsrpg.common.quests.trigger.ITrigger;
import dev.toma.gunsrpg.common.quests.trigger.TriggerType;

public abstract class AbstractTrigger<CTX> implements ITrigger<CTX> {

    private final TriggerType<CTX> ownerType;

    public AbstractTrigger(TriggerType<CTX> ownerType) {
        this.ownerType = ownerType;
    }

    @Override
    public TriggerType<CTX> getType() {
        return ownerType;
    }
}
