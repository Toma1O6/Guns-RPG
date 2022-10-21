package dev.toma.gunsrpg.client.render;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ZombieNightmareModel;
import dev.toma.gunsrpg.common.entity.ZombieNightmareEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class ZombieNightmareRenderer extends BipedRenderer<ZombieNightmareEntity, ZombieNightmareModel> {

    private static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/entity/zombie_nightmare.png");

    public ZombieNightmareRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ZombieNightmareModel(), 0.5F);
        layers.clear();
        addLayer(new HeadLayer<>(this, 1.0F, 1.0F, 1.0F));
        addLayer(new BipedArmorLayer<>(this, new ZombieNightmareModel(0.5F, true), new ZombieNightmareModel(1.0F, true)));
        addLayer(new HeldItemLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(ZombieNightmareEntity entity) {
        return TEXTURE;
    }
}
