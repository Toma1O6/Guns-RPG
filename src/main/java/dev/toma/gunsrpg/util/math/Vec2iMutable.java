package dev.toma.gunsrpg.util.math;

public class Vec2iMutable implements IVec2i {

    private int x;
    private int y;

    public Vec2iMutable(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void grow(int n) {
        growX(n);
        growY(n);
    }

    public void growX(int n) {
        this.x += n;
    }

    public void growY(int n) {
        this.y += n;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
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
