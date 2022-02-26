package dev.toma.gunsrpg.client.render.debuff;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.debuffs.IDebuff;

public interface IDebuffRenderer<D extends IDebuff> {

    void drawOnScreen(D debuff, IAttributeProvider attributes, MatrixStack poseStack, int left, int top, int width, int height, float partialTicks);
}
