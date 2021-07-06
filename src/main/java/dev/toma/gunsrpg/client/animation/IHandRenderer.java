package dev.toma.gunsrpg.client.animation;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.HandSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IHandRenderer {

    @OnlyIn(Dist.CLIENT)
    void transformRightArm(MatrixStack stack);

    @OnlyIn(Dist.CLIENT)
    void transformLeftArm(MatrixStack stack);

    default boolean shouldRenderForSide(HandSide side) {
        return true;
    }
}
