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

    private static final ResourceLocation[] VEX_TEXTURE = {
            GunsRPG.makeResource("textures/entity/rocket_angel_0.png"),
            GunsRPG.makeResource("textures/entity/rocket_angel_1.png"),
            GunsRPG.makeResource("textures/entity/rocket_angel_2.png"),
            GunsRPG.makeResource("textures/entity/rocket_angel_3.png"),
            GunsRPG.makeResource("textures/entity/rocket_angel_4.png")
    };

    public RocketAngelRenderer(EntityRendererManager manager) {
        super(manager, new RocketAngelModel(), 0.6F);
    }

    @Override
    protected int getBlockLightLevel(RocketAngelEntity entity, BlockPos pos) {
        return 15;
    }

    @Override
    public ResourceLocation getTextureLocation(RocketAngelEntity entity) {
        return VEX_TEXTURE[entity.getTextureIndex()];
    }

    @Override
    protected void scale(RocketAngelEntity p_225620_1_, MatrixStack matrix, float p_225620_3_) {
        matrix.translate(0.0F, 0.4F, 0.0F);
    }
}
