package dev.toma.gunsrpg.api.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;

public interface IKillDataRenderConfig {

    boolean shouldRender(PlayerEntity player);

    boolean isNestedDataRenderer();

    void render(KillDataRenderType renderType, MatrixStack poseStack, FontRenderer font, int screenWidth, int screenHeight, int additionalInfoSize);
}
