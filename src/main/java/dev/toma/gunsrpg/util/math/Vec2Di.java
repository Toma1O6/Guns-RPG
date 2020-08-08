package dev.toma.gunsrpg.util.math;

import net.minecraftforge.common.config.Config;

public class Vec2Di {

    @Config.Name("X")
    @Config.Comment("X coordinate of 2D vector")
    public int x;

    @Config.Name("Y")
    @Config.Comment("Y coordinate of 2D vector")
    public int y;

    public Vec2Di(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
