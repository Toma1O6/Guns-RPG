package lib.toma.animations.api;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.api.IRenderConfig;
import lib.toma.animations.api.IAnimationPipeline;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.HandSide;

import java.util.function.Function;

/**
 * Interface used to alter animation rendering via {@link IRenderPipeline}.
 * This type is applied via {@link IRenderPipeline#setHandAnimator(IHandAnimator)} and controls
 * rendering and animation of right and left hand. For hand rendering use {@link IHandRenderer#renderHand(MatrixStack, HandSide, Function, IRenderTypeBuffer, int)}
 * which can be retrieved from {@link IRenderPipeline#getHandRenderer()}
 *
 * @author Toma
 */
public interface IHandAnimator {

    /**
     * Handles rendering and animation of right and left hand. Hands should be rendered using
     * {@link IHandRenderer#renderHand(MatrixStack, HandSide, Function, IRenderTypeBuffer, int)} which can be retrieved
     * from {@link IRenderPipeline#getHandRenderer()}
     *
     * @param poseStack Current pose stack
     * @param buffer Render buffer
     * @param light Light value at player's location
     * @param equipProgress Equip animation progress
     * @param selector Retrieves correct {@link IRenderConfig} for specific hand side
     * @param pipeline {@link IAnimationPipeline} for animation processing
     */
    void animateHands(MatrixStack poseStack, IRenderTypeBuffer buffer, int light, float equipProgress,
                      Function<HandSide, IRenderConfig> selector, IAnimationPipeline pipeline);
}
