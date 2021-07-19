package dev.toma.gunsrpg.client.render;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ModelZombieGunner;
import dev.toma.gunsrpg.common.entity.ZombieGunnerEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.util.ResourceLocation;

public class RenderZombieGunner extends BipedRenderer<ZombieGunnerEntity, ModelZombieGunner> {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/entity/zombie_gunner.png");

    public RenderZombieGunner(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ModelZombieGunner(), 0.5F);
        addLayer(new BipedArmorLayer<>(this, new ModelZombieGunner(0.5F, true), new ModelZombieGunner(1.0F, true)));
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieGunnerEntity entity) {
        return TEXTURE;
    }
}
