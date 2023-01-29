package dev.toma.gunsrpg.common.debuffs.sources;

import dev.toma.gunsrpg.common.debuffs.IDebuffContext;

public interface DebuffSource {

    float getChance(IDebuffContext context);

    DebuffSourceType<?> getType();
}
