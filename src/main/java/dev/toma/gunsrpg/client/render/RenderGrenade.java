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
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.Nullable;

public class RenderGrenade extends EntityRenderer<GrenadeEntity> {

    private static final Vector3f ROTATION = new Vector3f(1.0F, 0.5F, 1.0F);
    private final ItemRenderer renderer;

    public RenderGrenade(EntityRendererManager manager) {
        super(manager);
        this.renderer = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void render(GrenadeEntity grenade, float yaw, float renderTickTime, MatrixStack matrix, IRenderTypeBuffer buffer, int light) {
        matrix.pushPose();
        matrix.scale(0.6F, 0.6F, 0.6F);
        matrix.translate(-0.5F, 0.0F, -0.5F);
        float rotation = MathHelper.lerp(renderTickTime, grenade.lastRotation, grenade.rotation);
        matrix.mulPose(new Quaternion(ROTATION, rotation, true));
        ItemStack stack = grenade.renderStack.orElse(ItemStack.EMPTY);
        IBakedModel model = renderer.getModel(stack, grenade.level, null);
        renderer.render(stack, ItemCameraTransforms.TransformType.GROUND, false, matrix, buffer, light, OverlayTexture.NO_OVERLAY, model);
        matrix.popPose();
        super.render(grenade, yaw, renderTickTime, matrix, buffer, light);
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(GrenadeEntity entity) {
        return AtlasTexture.LOCATION_BLOCKS;
    }
}
