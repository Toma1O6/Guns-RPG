package dev.toma.gunsrpg.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.model.RocketAngelModel;
import dev.toma.gunsrpg.common.entity.RocketAngelEntity;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class RocketAngelRenderer extends BipedRenderer<RocketAngelEntity, RocketAngelModel> {

    private static final ResourceLocation VEX_TEXTURE = GunsRPG.makeResource("textures/entity/rocket_angel.png");

    public RocketAngelRenderer(EntityRendererManager manager) {
        super(manager, new RocketAngelModel(), 0.6F);
    }

    @Override
    protected int getBlockLightLevel(RocketAngelEntity p_225624_1_, BlockPos p_225624_2_) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(RocketAngelEntity p_110775_1_) {
        return VEX_TEXTURE;
    }

    @Override
    protected void scale(RocketAngelEntity p_225620_1_, MatrixStack matrix, float p_225620_3_) {
        matrix.translate(0.0F, 0.4F, 0.0F);
    }
}
