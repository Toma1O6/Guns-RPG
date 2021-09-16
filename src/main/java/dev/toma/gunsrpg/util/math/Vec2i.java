package dev.toma.gunsrpg.util.math;

public class Vec2i implements IVec2i {

    private final int x, y;

    public Vec2i(int x, int y) {
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
