package com.wf.firearms.client.render.entity;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.entity.BulletProjectile;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/** 子弹实体客户端渲染（无可见模型，仅避免 EntityRenderDispatcher NPE）。 */
public class BulletEntityRenderer extends EntityRenderer<BulletProjectile> {
    private static final ResourceLocation TEX =
            ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "textures/item/wooden_9mm.png");

    public BulletEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(BulletProjectile entity) {
        return TEX;
    }
}
