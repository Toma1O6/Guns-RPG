package dev.toma.gunsrpg.client.render;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ZombieGunnerModel;
import dev.toma.gunsrpg.client.render.layer.CustomHeldLayer;
import dev.toma.gunsrpg.common.entity.ZombieGunnerEntity;
import dev.toma.gunsrpg.config.ModConfig;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.util.ResourceLocation;

public class ZombieGunnerRenderer extends BipedRenderer<ZombieGunnerEntity, ZombieGunnerModel> {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/entity/zombie_gunner.png");

    public ZombieGunnerRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ZombieGunnerModel(), 0.5F);
        layers.clear();
        addLayer(new HeadLayer<>(this, 1.0F, 1.0F, 1.0F));
        addLayer(new BipedArmorLayer<>(this, new ZombieGunnerModel(0.5F, true), new ZombieGunnerModel(1.0F, true)));
        addLayer(new CustomHeldLayer<>(this, ModConfig.clientConfig.gunnerHeldItemRenderConfig));
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieGunnerEntity entity) {
        return TEXTURE;
    }
}
