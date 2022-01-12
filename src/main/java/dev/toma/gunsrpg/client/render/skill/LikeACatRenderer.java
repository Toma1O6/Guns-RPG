package dev.toma.gunsrpg.client.render.skill;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.client.ISkillRenderer;
import dev.toma.gunsrpg.common.skills.LikeACatSkill;
import dev.toma.gunsrpg.util.RenderUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.math.vector.Matrix4f;

public class LikeACatRenderer implements ISkillRenderer<LikeACatSkill> {

    @Override
    public void renderExtraSkillData(LikeACatSkill skill, MatrixStack stack, FontRenderer renderer, int x, int y, int width, int height, float alpha) {
        float effect = skill.getEffectPct();
        float cooldn = skill.getCooldownPct();
        Matrix4f pose = stack.last().pose();
        boolean drawing = renderBackground(pose, x, y, width, height, effect, cooldn);
        if (!drawing)
            return;
        if (effect > 0) {
            renderEffect(pose, x, y, width, height, 1.0F - effect, 0xFF00BB00);
        } else if (cooldn > 0) {
            renderEffect(pose, x, y, width, height, cooldn, 0xFFFF8228);
        }
    }

    private boolean renderBackground(Matrix4f pose, int x, int y, int width, int height, float effect, float cooldown) {
        if (effect == 0 && cooldown == 0)
            return false;
        RenderUtils.drawSolid(pose, x + width + 1, y, x + width + 5, y + height, 0xFF << 24);
        return true;
    }

    private void renderEffect(Matrix4f pose, int x, int y, int width, int height, float pct, int color) {
        int size = height - 2;
        int added = (int) (size * pct);
        int top = y + 1 + size - added;
        int bottom = top + (int) (size * pct);
        RenderUtils.drawSolid(pose, x + width + 2, top, x + width + 4, bottom, color);
    }
}
