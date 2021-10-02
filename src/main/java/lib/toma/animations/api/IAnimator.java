package lib.toma.animations.api;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.api.IRenderConfig;
import lib.toma.animations.api.IAnimationPipeline;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;

import java.util.function.Function;

/**
 * Interface used to alter animation rendering via {@link IRenderPipeline}.
 * This type is applied via {@link IRenderPipeline#setAnimator(IAnimator)} and controls
 * dispatching on vanilla animation stages such as {@link AnimationStage#HELD_ITEM} etc.
 *
 * @author Toma
 */
@FunctionalInterface
public interface IAnimator {

    /**
     * Should handle animation of vanilla {@link AnimationStage}s
     *
     * @param poseStack Current pose stack
     * @param buffer Render buffer
     * @param light Light value at player's location
     * @param swing Swing progress
     * @param equip Equip progress
     * @param selector Retrieves correct {@link IRenderConfig} for specific hand
     * @param pipeline {@link IAnimationPipeline} for processing animations
     * @param fpRenderer First person renderer for rendering items
     * @param player Client player
     * @param stack Held item
     * @param type Current transform type
     * @param mainHand If current hand is also main hand (right hand)
     */
    void animate(MatrixStack poseStack, IRenderTypeBuffer buffer, int light, float swing, float equip, Function<HandSide, IRenderConfig> selector,
                 IAnimationPipeline pipeline, FirstPersonRenderer fpRenderer, PlayerEntity player, ItemStack stack,
                 ItemCameraTransforms.TransformType type, boolean mainHand);
}
