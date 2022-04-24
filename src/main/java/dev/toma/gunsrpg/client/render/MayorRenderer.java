package dev.toma.gunsrpg.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.common.entity.MayorEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.model.VillagerModel;
import net.minecraft.util.ResourceLocation;

public class MayorRenderer extends MobRenderer<MayorEntity, VillagerModel<MayorEntity>> {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/entity/mayor.png");

    public MayorRenderer(EntityRendererManager manager) {
        super(manager, new VillagerModel<>(0.0F), 0.5F);
        this.addLayer(new HeadLayer<>(this));
        this.addLayer(new CrossedArmsItemLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(MayorEntity entity) {
        return TEXTURE;
    }

    @Override
    protected void scale(MayorEntity entity, MatrixStack matrix, float f) {
        float scale = 0.9375F;
        matrix.scale(scale, scale, scale);
    }
}
