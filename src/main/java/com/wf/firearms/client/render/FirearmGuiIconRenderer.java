package com.wf.firearms.client.render;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.client.model.FirearmGuiIconModels;
import com.wf.firearms.client.model.FirearmIconAssets;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

/**
 * 创造栏 / 地面 / 展示框：在 BEWLR 内用 {@link ItemRenderer#renderModelLists} 画 2D 图标。
 *
 * <p>Forge 1.20.1 在 {@code isCustomRenderer=true} 时始终进 BEWLR，{@code applyTransform} 无法改走原版平面链。
 * ItemRenderer 已 {@code translate(0.5,0.5,0.5)}，此处不要再 {@code applyTransform}（会把图标压到角落）。
 */
public final class FirearmGuiIconRenderer {
    private static final ResourceLocation MISSINGNO =
            ResourceLocation.fromNamespaceAndPath("minecraft", "missingno");

    private FirearmGuiIconRenderer() {}

    public static boolean render(
            ItemStack stack,
            String weaponKey,
            ItemDisplayContext ctx,
            PoseStack pose,
            MultiBufferSource buffer,
            int light,
            int overlay) {
        if (ctx != ItemDisplayContext.GUI
                && ctx != ItemDisplayContext.GROUND
                && ctx != ItemDisplayContext.FIXED) {
            return false;
        }

        Minecraft mc = Minecraft.getInstance();
        BakedModel iconModel = resolveIconModel(mc.getModelManager(), weaponKey);
        if (iconModel == null || hasMissingSprite(iconModel)) {
            GunsRpg.LOGGER.warn(
                    "GUI 2D icon missing for {} ({}), fallback 3D",
                    weaponKey,
                    FirearmIconAssets.textureId(weaponKey));
            return false;
        }

        if (ctx == ItemDisplayContext.GUI
                || ctx == ItemDisplayContext.GROUND
                || ctx == ItemDisplayContext.FIXED) {
            light = LightTexture.FULL_BRIGHT;
        }

        ItemRenderer itemRenderer = mc.getItemRenderer();
        boolean foil = stack.hasFoil();
        pose.pushPose();
        var passes = iconModel.getRenderPasses(stack, true);
        if (passes.isEmpty()) {
            passes = java.util.List.of(iconModel);
        }
        for (BakedModel pass : passes) {
            for (var renderType : pass.getRenderTypes(stack, true)) {
                VertexConsumer consumer = ItemRenderer.getFoilBuffer(buffer, renderType, false, foil);
                itemRenderer.renderModelLists(pass, stack, light, overlay, pose, consumer);
            }
        }
        pose.popPose();
        return true;
    }

    private static BakedModel resolveIconModel(ModelManager models, String weaponKey) {
        BakedModel cached = FirearmGuiIconModels.get(weaponKey);
        if (cached != null && !hasMissingSprite(cached)) {
            return cached;
        }

        ModelResourceLocation[] candidates = {
            FirearmIconAssets.inventoryModel(weaponKey),
            new ModelResourceLocation(FirearmIconAssets.modelId(weaponKey), ""),
            new ModelResourceLocation(FirearmIconAssets.modelId(weaponKey), "standalone")
        };
        for (ModelResourceLocation key : candidates) {
            BakedModel model = models.getModel(key);
            if (model != null && model != models.getMissingModel() && !hasMissingSprite(model)) {
                return model;
            }
        }
        return null;
    }

    public static boolean hasMissingSprite(BakedModel model) {
        TextureAtlasSprite sprite = model.getParticleIcon();
        if (sprite == null) {
            return true;
        }
        ResourceLocation name = sprite.contents().name();
        return MISSINGNO.equals(name);
    }
}
