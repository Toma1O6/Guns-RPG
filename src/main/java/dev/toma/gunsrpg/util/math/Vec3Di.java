package dev.toma.gunsrpg.util.math;

public class Vec3Di extends Vec2Di {

    public final int z;

    public Vec3Di(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }

    public int getZ() {
        return z;
    }
}
