package dev.toma.gunsrpg.config.world;

import dev.toma.configuration.config.Configurable;
import dev.toma.gunsrpg.api.common.IGeneratorConfig;

public class SimpleOreGenConfig implements IGeneratorConfig {

    @Configurable
    @Configurable.Range(min = 0, max = 128)
    @Configurable.Comment("Generation attempts per chunk")
    public int spawns;

    @Configurable
    @Configurable.Range(min = 1, max = 255)
    @Configurable.Comment("Minimum generation height")
    public int minHeight;

    @Configurable
    @Configurable.Range(min = 1, max = 255)
    @Configurable.Comment("Maximum generation height")
    public int maxHeight;

    public SimpleOreGenConfig(int spawnAttempts, int minGenHeight, int maxGenHeight) {
        spawns = spawnAttempts;
        minHeight = minGenHeight;
        maxHeight = maxGenHeight;
    }

    public int getSpawnAttempts() {
        return spawns;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }
}
