package dev.toma.gunsrpg.config.world;

import net.minecraftforge.common.config.Config;

public class SimpleOreGenConfig {

    @Config.Name("Spawns")
    @Config.Comment("Amount of spawn attempts per chunk")
    @Config.RequiresMcRestart
    public int spawns;

    @Config.Name("Min height")
    @Config.Comment("Defines minimal height where ore can spawn")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 1, max = 255)
    public int minHeight;

    @Config.Name("Max height")
    @Config.Comment("Defines maximum height where ore can spawn")
    @Config.RequiresMcRestart
    @Config.RangeInt(min = 1, max = 255)
    public int maxHeight;

    public SimpleOreGenConfig(int spawns, int minHeight, int maxHeight) {
        this.spawns = spawns;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }
}
