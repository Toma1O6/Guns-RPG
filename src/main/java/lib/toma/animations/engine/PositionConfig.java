package lib.toma.animations.engine;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.util.math.vector.Vector3d;

public class PositionConfig implements IRenderConfig {

    protected Vector3d pos;

    public PositionConfig(Vector3d pos) {
        this.pos = pos;
    }

    @Override
    public void applyTo(MatrixStack stack) {
        stack.translate(pos.x, pos.y, pos.z);
    }
}
