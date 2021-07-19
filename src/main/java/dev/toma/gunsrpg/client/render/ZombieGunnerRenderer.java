package dev.toma.gunsrpg.client.render;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ZombieGunnerModel;
import dev.toma.gunsrpg.common.entity.ZombieGunnerEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;

public class ZombieGunnerRenderer extends BipedRenderer<ZombieGunnerEntity, ZombieGunnerModel> {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/entity/zombie_gunner.png");

    public ZombieGunnerRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ZombieGunnerModel(), 0.5F);
        addLayer(new BipedArmorLayer<>(this, new ZombieGunnerModel(0.5F, true), new ZombieGunnerModel(1.0F, true)));
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieGunnerEntity entity) {
        return TEXTURE;
    }
}
