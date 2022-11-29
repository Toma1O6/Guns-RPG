package dev.toma.gunsrpg.config.quest;

import dev.toma.configuration.config.Configurable;

public final class AreaConfig {

    @Configurable
    @Configurable.Range(min = 0)
    @Configurable.Comment("Smallest possible distance of quest location on one axis")
    public int minDistance;

    @Configurable
    @Configurable.Range(min = 0)
    @Configurable.Comment("Biggest possible distance of quest location on one axis")
    public int maxDistance;

    @Configurable
    @Configurable.Range(min = 5, max = 128)
    @Configurable.Comment("Area radius in blocks")
    public int areaSize;

    public AreaConfig(int minDistance, int maxDistance, int areaSize) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.areaSize = areaSize;
    }
}
