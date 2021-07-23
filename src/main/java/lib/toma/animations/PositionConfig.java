package lib.toma.animations;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.math.vector.Vector3d;

public class PositionConfig implements IRenderConfig {

    protected Vector3d pos;

    protected PositionConfig(Vector3d pos) {
        this.pos = pos;
    }

    @Override
    public void applyTo(MatrixStack stack) {
        stack.translate(pos.x, pos.y, pos.z);
    }
}
