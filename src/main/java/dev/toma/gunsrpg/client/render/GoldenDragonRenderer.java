package dev.toma.gunsrpg.client.render;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.client.renderer.entity.EnderDragonRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.util.ResourceLocation;

public class GoldenDragonRenderer extends EnderDragonRenderer {

    public static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/entity/golden_dragon.png");

    public GoldenDragonRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public ResourceLocation getTextureLocation(EnderDragonEntity entity) {
        return TEXTURE;
    }
}
