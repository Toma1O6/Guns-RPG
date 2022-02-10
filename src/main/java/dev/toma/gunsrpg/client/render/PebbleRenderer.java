package dev.toma.gunsrpg.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.common.entity.projectile.Pebble;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class PebbleRenderer extends EntityRenderer<Pebble> {

    private final ItemRenderer renderer;

    public PebbleRenderer(EntityRendererManager manager) {
        super(manager);
        shadowRadius = 0.0f;
        shadowStrength = 0.0f;
        renderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public ResourceLocation getTextureLocation(Pebble p_110775_1_) {
        return AtlasTexture.LOCATION_BLOCKS;
    }

    @Override
    public void render(Pebble pebble, float p_225623_2_, float p_225623_3_, MatrixStack poseStack, IRenderTypeBuffer typeBuffer, int light) {
        ItemStack stack = pebble.getAmmoSource();
        if (stack.isEmpty()) return;
        poseStack.pushPose();
        IBakedModel model = renderer.getModel(stack, pebble.level, null);
        float spin = (pebble.tickCount * 5.0F) % 360;
        poseStack.mulPose(Vector3f.XP.rotationDegrees(spin));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(spin));
        renderer.render(stack, ItemCameraTransforms.TransformType.GROUND, false, poseStack, typeBuffer, light, OverlayTexture.NO_OVERLAY, model);
        poseStack.popPose();
    }
}
