package dev.toma.gunsrpg.config.world;

import dev.toma.configuration.api.IConfigWriter;
import dev.toma.configuration.api.IObjectSpec;
import dev.toma.configuration.api.type.IntType;
import dev.toma.configuration.api.type.ObjectType;
import dev.toma.gunsrpg.api.common.IGeneratorConfig;

public class SimpleOreGenConfig extends ObjectType implements IGeneratorConfig {

    private final IntType spawns;
    private final IntType minHeight;
    private final IntType maxHeight;

    public SimpleOreGenConfig(IObjectSpec spec, int spawnAttempts, int minGenHeight, int maxGenHeight) {
        super(spec);
        IConfigWriter writer = spec.getWriter();
        spawns = writer.writeBoundedInt("Spawns", spawnAttempts, 0, 128, "Amount of spawn attempts per chunk");
        minHeight = writer.writeBoundedInt("Min height", minGenHeight, 1, 255, "Defines lowest height at which ore can spawn");
        maxHeight = writer.writeBoundedInt("Max height", maxGenHeight, 1, 255, "Defines highest height at which ore can spawn");
    }

    public int getSpawnAttempts() {
        return spawns.get();
    }

    public int getMinHeight() {
        return minHeight.get();
    }

    public int getMaxHeight() {
        return maxHeight.get();
    }
}
