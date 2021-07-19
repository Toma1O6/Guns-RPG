package dev.toma.gunsrpg.client.render;

import dev.toma.gunsrpg.common.entity.ExplosiveArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TippedArrowRenderer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class ExplosiveArrowRenderer extends ArrowRenderer<ExplosiveArrowEntity> {

    public ExplosiveArrowRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(ExplosiveArrowEntity entity) {
        return TippedArrowRenderer.NORMAL_ARROW_LOCATION;
    }
}
