package dev.toma.gunsrpg.common.debuffs;

import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;

public interface IStagedDebuff extends IDebuff {

    boolean canSpread();

    int getCurrentProgress();

    int ticksSinceAdded();

    int ticksSinceProgressed();

    int ticksSinceHealed();

    float getBlockingProgress(IAttributeProvider provider);
}
