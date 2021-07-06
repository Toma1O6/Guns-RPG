package dev.toma.gunsrpg.client.render;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ModelExplosiveSkeleton;
import dev.toma.gunsrpg.common.entity.ExplosiveSkeletonEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;

public class RenderExplosiveSkeleton extends RenderBiped<ExplosiveSkeletonEntity> {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/entity/explosive_skeleton.png");

    public RenderExplosiveSkeleton(RenderManager manager) {
        super(manager, new ModelExplosiveSkeleton(), 0.5F);
        addLayer(new LayerHeldItem(this));
        addLayer(new LayerBipedArmor(this) {
            @Override
            protected void initArmor() {
                this.modelLeggings = new ModelExplosiveSkeleton(0.5F, true);
                this.modelArmor = new ModelExplosiveSkeleton(1.0F, true);
            }
        });
    }

    @Override
    public void transformHeldFull3DItemLayer()
    {
        GlStateManager.translate(0.09375F, 0.1875F, 0.0F);
    }

    @Override
    protected ResourceLocation getEntityTexture(ExplosiveSkeletonEntity entity) {
        return TEXTURE;
    }
}
