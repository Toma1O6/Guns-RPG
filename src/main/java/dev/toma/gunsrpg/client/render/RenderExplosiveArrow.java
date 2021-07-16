package dev.toma.gunsrpg.client.render;

import dev.toma.gunsrpg.common.entity.EntityExplosiveArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TippedArrowRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderExplosiveArrow extends ArrowRenderer<EntityExplosiveArrow> {

    public RenderExplosiveArrow(EntityRendererManager manager) {
        super(manager);
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(EntityExplosiveArrow entity) {
        return TippedArrowRenderer.NORMAL_ARROW_LOCATION;
    }
}
