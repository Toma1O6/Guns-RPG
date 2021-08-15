package lib.toma.animations;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IRenderConfig {

    IRenderConfig EMPTY = matrix -> {};

    @OnlyIn(Dist.CLIENT)
    void applyTo(MatrixStack stack);

    static IRenderConfig pos(Vector3d position) {
        return new PositionConfig(position);
    }

    static IRenderConfig pos(double x, double y, double z) {
        return pos(new Vector3d(x, y, z));
    }

    static IRenderConfig empty() {
        return EMPTY;
    }
}
