package lib.toma.animations.pipeline.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IItemRenderer {
    void renderItem(FirstPersonRenderer fpRenderer, PlayerEntity player, ItemStack stack, ItemCameraTransforms.TransformType type, boolean offHand,
                    MatrixStack poseStack, IRenderTypeBuffer buffer, int light, float swing, float equip);
}
