package lib.toma.animations.api;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.engine.PositionConfig;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * Structure containing various transforms which can be applied to
 * any {@link MatrixStack}.
 *
 * @author Toma
 */
public interface IRenderConfig {

    /**
     * Empty render config, doesn't do anything. Avoids using {@code null} values in code.
     */
    IRenderConfig EMPTY = matrix -> {};

    /**
     * Applies transforms from this object to supplied pose stack
     * @param stack Pose stack
     */
    @OnlyIn(Dist.CLIENT)
    void applyTo(MatrixStack stack);

    /**
     * Creates position transform config
     * @param position Position transform
     * @return Position transform config
     */
    static IRenderConfig pos(Vector3d position) {
        return new PositionConfig(position);
    }

    /**
     * Creates position transform config.
     * Same as {@link IRenderConfig#pos(Vector3d)} but you don't have to manually create new position vector
     * @param x X position
     * @param y Y position
     * @param z Z position
     * @return Position transform config
     */
    static IRenderConfig pos(double x, double y, double z) {
        return pos(new Vector3d(x, y, z));
    }

    /**
     * Returns empty render config to avoid using null values in code.
     * @return Empty render config
     */
    static IRenderConfig empty() {
        return EMPTY;
    }
}
