package com.wf.firearms.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wf.firearms.client.FirearmRenderers;
import com.wf.firearms.item.FirearmItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class GunsRpgItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static GunsRpgItemRenderer instance;

    public GunsRpgItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet models) {
        super(dispatcher, models);
    }

    public static GunsRpgItemRenderer getInstance() {
        if (instance == null) {
            Minecraft mc = Minecraft.getInstance();
            instance = new GunsRpgItemRenderer(mc.getBlockEntityRenderDispatcher(), mc.getEntityModels());
            FirearmRenderers.init();
        }
        return instance;
    }

    private static boolean isHandContext(ItemDisplayContext ctx) {
        return ctx == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
                || ctx == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND
                || ctx == ItemDisplayContext.THIRD_PERSON_LEFT_HAND
                || ctx == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND
                || ctx == ItemDisplayContext.HEAD;
    }

    @Override
    public void renderByItem(
            ItemStack stack,
            ItemDisplayContext ctx,
            PoseStack pose,
            MultiBufferSource buffer,
            int light,
            int overlay) {
        if (!(stack.getItem() instanceof FirearmItem firearm)) {
            return;
        }

        if (!isHandContext(ctx)) {
            return;
        }

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        FirearmRenderers.getRenderer(firearm.getWeaponKey()).render(stack, ctx, pose, buffer, light, overlay);
    }
}
