package dev.toma.gunsrpg.client.render;

import dev.toma.gunsrpg.common.entity.EntityExplosiveArrow;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RenderExplosiveArrow extends RenderArrow<EntityExplosiveArrow> {

    public static final ResourceLocation RES_ARROW = new ResourceLocation("textures/entity/projectiles/arrow.png");

    public RenderExplosiveArrow(RenderManager manager) {
        super(manager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityExplosiveArrow entity) {
        return RES_ARROW;
    }
}
