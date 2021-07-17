package dev.toma.gunsrpg.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.common.entity.GrenadeEntity;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nullable;

public class RenderGrenade extends EntityRenderer<GrenadeEntity> {

    private final ItemRenderer renderer;

    public RenderGrenade(EntityRendererManager manager) {
        super(manager);
        this.renderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(GrenadeEntity grenade, float yaw, float renderTickTime, MatrixStack matrix, IRenderTypeBuffer buffer, int light) {
        matrix.pushPose();
        matrix.translate(0.0F, 0.2F, 0.0F);
        float rotation = MathHelper.lerp(renderTickTime, grenade.lastRotation, grenade.rotation);
        matrix.mulPose(Vector3f.XP.rotationDegrees(rotation));
        matrix.mulPose(Vector3f.YP.rotationDegrees(rotation / 2f));
        matrix.mulPose(Vector3f.ZP.rotationDegrees(rotation));
        ItemStack stack = grenade.renderStack.orElse(ItemStack.EMPTY);
        IBakedModel model = renderer.getModel(stack, grenade.level, null);
        renderer.render(stack, ItemCameraTransforms.TransformType.GROUND, true, matrix, buffer, light, OverlayTexture.NO_OVERLAY, model);
        matrix.popPose();
        super.render(grenade, yaw, renderTickTime, matrix, buffer, light);
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(GrenadeEntity entity) {
        return AtlasTexture.LOCATION_BLOCKS;
    }
}
