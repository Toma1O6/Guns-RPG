package lib.toma.animations.api;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;

import java.util.function.Function;

/**
 * Interface which allows modification of {@link IRenderPipeline}
 *
 * @author Toma
 */
@FunctionalInterface
public interface IAnimateCallback {

    /**
     * Callback method
     *
     * @param poseStack Stack of all transformations
     * @param buffer RenderType buffer
     * @param light Current light value
     * @param swing Hand swing progress
     * @param equip Item equip progress
     * @param selector {@link IRenderConfig} selector for specific hand side
     * @param pipeline Animation pipeline
     * @param fpRenderer First person renderer, used for rendering held items
     * @param player Client player entity
     * @param stack Held ItemStack
     * @param type Current {@link net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType}
     * @param mainHand Whether currently rendered hand is main hand (right hand)
     */
    void call(MatrixStack poseStack, IRenderTypeBuffer buffer, int light, float swing, float equip, Function<HandSide, IRenderConfig> selector,
              IAnimationPipeline pipeline, FirstPersonRenderer fpRenderer, PlayerEntity player, ItemStack stack, ItemCameraTransforms.TransformType type,
              boolean mainHand);
}
