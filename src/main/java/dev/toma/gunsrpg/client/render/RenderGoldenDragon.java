package dev.toma.gunsrpg.client.render;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.ResourceLocation;

public class RenderGoldenDragon extends RenderDragon {

    public static final ResourceLocation TEXTURE = GunsRPG.makeResource("textures/entity/golden_dragon.png");

    public RenderGoldenDragon(RenderManager manager) {
        super(manager);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityDragon entity) {
        return TEXTURE;
    }
}
