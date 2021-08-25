package lib.toma.animations.api;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.api.IRenderConfig;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.HandSide;

import java.util.function.Function;

/**
 * Interface used to alter hand rendering via {@link IRenderPipeline}.
 * This type is applied via {@link IRenderPipeline#setHandRenderer(IHandRenderer)} and controls
 * hand rendering.
 *
 * @author Toma
 */
public interface IHandRenderer {

    /**
     * Renders hand for specific hand side and applies correct {@link IRenderConfig}.
     *
     * @param stack Current pose stack
     * @param side Hand side being rendered
     * @param selector Retrieves correct {@link IRenderConfig} for provided {@link HandSide}
     * @param buffer Render buffer
     * @param light Light value at player's location
     */
    void renderHand(MatrixStack stack, HandSide side, Function<HandSide, IRenderConfig> selector, IRenderTypeBuffer buffer, int light);
}
