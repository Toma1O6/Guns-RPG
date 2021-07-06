package dev.toma.gunsrpg.client.render;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.ModelZombieGunner;
import dev.toma.gunsrpg.common.entity.ZombieGunnerEntity;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;

public class RenderZombieGunner extends RenderBiped<ZombieGunnerEntity> {

    private static final ResourceLocation TEXTURES = GunsRPG.makeResource("textures/entity/zombie_gunner.png");

    public RenderZombieGunner(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelZombieGunner(), 0.5F);
        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this) {
            protected void initArmor() {
                this.modelLeggings = new ModelZombieGunner(0.5F, true);
                this.modelArmor = new ModelZombieGunner(1.0F, true);
            }
        };
        this.addLayer(layerbipedarmor);
    }

    protected ResourceLocation getEntityTexture(ZombieGunnerEntity entity) {
        return TEXTURES;
    }
}
