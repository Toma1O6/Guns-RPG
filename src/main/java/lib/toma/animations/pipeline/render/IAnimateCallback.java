package lib.toma.animations.pipeline.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import lib.toma.animations.IRenderConfig;
import lib.toma.animations.pipeline.IAnimationPipeline;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;

import java.util.function.Function;

public interface IAnimateCallback {

    void call(MatrixStack poseStack, IRenderTypeBuffer buffer, int light, float swing, float equip, Function<HandSide, IRenderConfig> selector,
              IAnimationPipeline pipeline, FirstPersonRenderer fpRenderer, PlayerEntity player, ItemStack stack, ItemCameraTransforms.TransformType type,
              boolean mainHand);
}
