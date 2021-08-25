package lib.toma.animations.api;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * Interface used to alter item rendering via {@link IRenderPipeline}.
 * This type is applied via {@link IRenderPipeline#setItemRenderer(IItemRenderer)} and controls
 * item rendering and animating held item.
 *
 * @author Toma
 */
public interface IItemRenderer {

    /**
     * Used to render held items.
     * Default implementation uses provided FirstPersonRenderer for item rendering.
     * @param fpRenderer FirstPersonRenderer for item rendering which is done by calling {@link FirstPersonRenderer#renderItem(LivingEntity, ItemStack, ItemCameraTransforms.TransformType, boolean, MatrixStack, IRenderTypeBuffer, int)}
     * @param player Client player
     * @param stack Held item
     * @param type Current transform type
     * @param offHand If currently rendered hand is off hand (left hand)
     * @param poseStack Current pose stack
     * @param buffer Render buffer
     * @param light Light at player's location
     * @param swing Swing animation progress (can be set to 0 if {@link IAnimationEntry#disableVanillaAnimations()} returns true)
     * @param equip Equip animation progress (can be set to 0 if {@link IAnimationEntry#disableVanillaAnimations()} returns true)
     */
    void renderItem(FirstPersonRenderer fpRenderer, PlayerEntity player, ItemStack stack, ItemCameraTransforms.TransformType type, boolean offHand,
                    MatrixStack poseStack, IRenderTypeBuffer buffer, int light, float swing, float equip);
}
