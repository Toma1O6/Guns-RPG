package dev.toma.gunsrpg.util.math;

import dev.toma.configuration.config.Configurable;

public class ConfigurableVec2i implements IVec2i {

    @Configurable
    @Configurable.Comment("X coordinate of vector")
    public int x;

    @Configurable
    @Configurable.Comment("Y coordinate of vector")
    public int y;

    public ConfigurableVec2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return y;
    }
}
