package dev.toma.gunsrpg.api.common.skill;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.IOverlayRender;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ICooldown extends ITickableSkill, IOverlayRender {

    int getCooldown();

    void setOnCooldown();

    int getMaxCooldown();

    void onUse(PlayerEntity player);

    @OnlyIn(Dist.CLIENT)
    @Override
    default void drawOnTop(MatrixStack stack, int x, int y, int width, int heigth) {
        float f = getCooldown() / (float) getMaxCooldown();
        if (f == 0) return;
        stack.pushPose();
        stack.translate(0, 0, 1);
        Matrix4f pose = stack.last().pose();
        ModUtils.renderColor(pose, x, y + heigth, x + width, y + heigth + 3, 0.0F, 0.0F, 0.0F, 1.0F);
        ModUtils.renderColor(pose, x + 1, y + heigth + 1, x + 1 + (int) ((width - 1) * f), y + heigth + 2, 1.0F, 0.2F, 0.0F, 1.0F);
        String cooldown = ModUtils.formatTicksToTime(this.getCooldown());
        FontRenderer renderer = Minecraft.getInstance().font;
        renderer.draw(stack, cooldown, x + (width - renderer.width(cooldown)) / 2f, y + heigth + 4, 0xffffff);
        stack.popPose();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    default void renderInHUD(MatrixStack stack, ISkill skill, int renderIndex, int left, int top) {
        if (skill.canApply(Minecraft.getInstance().player)) {
            int x = left + renderIndex * 20;
            Matrix4f pose = stack.last().pose();
            ModUtils.renderColor(pose, x, top, x + 20, top + 20, 0.0F, 0.0F, 0.0F, 0.4F);
            //ModUtils.renderTexture(pose, x + 2, top + 2, x + 18, top + 18, skill.getType().icon);
        }
    }
}
