package lib.toma.animations.engine;

import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

public final class Vector4f {

    public static final Vector4f ZERO = new Vector4f(0.0F, 0.0F, 0.0F, 0.0F);

    private final float rotation;
    private final float x;
    private final float y;
    private final float z;

    public Vector4f(float rotation, float x, float y, float z) {
        this.rotation = rotation;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector4f add(Vector4f vector4f) {
        return new Vector4f(rotation + vector4f.rotation, x + vector4f.x, y + vector4f.y, z + vector4f.z);
    }

    public Vector4f subtract(Vector4f vector4f) {
        return new Vector4f(rotation - vector4f.rotation, x - vector4f.x, y - vector4f.y, z - vector4f.z);
    }

    public Vector4f inverseRotation() {
        return new Vector4f(-rotation, x, y, z);
    }

    public Quaternion toQuaternion() {
        return new Quaternion(new Vector3f(x, y, z), rotation, true);
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float z() {
        return z;
    }

    public float rotation() {
        return rotation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector4f vector4f = (Vector4f) o;
        if (Float.compare(vector4f.rotation, rotation) != 0) return false;
        if (Float.compare(vector4f.x, x) != 0) return false;
        if (Float.compare(vector4f.y, y) != 0) return false;
        return Float.compare(vector4f.z, z) == 0;
    }

    @Override
    public int hashCode() {
        int result = (rotation != 0.0f ? Float.floatToIntBits(rotation) : 0);
        result = 31 * result + (x != 0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != 0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != 0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Vector4f{" +
                "rotation=" + rotation +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
