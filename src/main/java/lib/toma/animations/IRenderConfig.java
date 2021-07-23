package lib.toma.animations;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.vector.Vector3d;

public interface IRenderConfig {

    IRenderConfig EMPTY = matrix -> {};

    void applyTo(MatrixStack stack);

    static PositionConfig pos(Vector3d position) {
        return new PositionConfig(position);
    }

    static PositionConfig pos(double x, double y, double z) {
        return pos(new Vector3d(x, y, z));
    }

    static IRenderConfig empty() {
        return EMPTY;
    }
}
