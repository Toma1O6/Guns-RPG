package dev.toma.gunsrpg.client.render.skill;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.client.IHudSkillRenderer;
import dev.toma.gunsrpg.api.common.skill.ICooldown;
import dev.toma.gunsrpg.api.common.skill.ISkill;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.gunsrpg.util.RenderUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.math.vector.Matrix4f;

public class SkillCooldownRenderer<S extends ISkill & ICooldown> implements IHudSkillRenderer<S> {

    @Override
    public void renderExtraSkillData(S skill, MatrixStack stack, FontRenderer renderer, int x, int y, int width, int height, float alpha) {
        int cooldown = skill.getCooldown();
        if (cooldown > 0 && alpha > 0.1F) {
            String text = Interval.format(cooldown, f -> f.src(Interval.Unit.TICK).out(Interval.Unit.HOUR, Interval.Unit.MINUTE, Interval.Unit.SECOND).compact());
            int textWidth = renderer.width(text);
            float center = x + (width - textWidth) / 2.0F;
            int a = (int) (255 * alpha);
            Matrix4f pose = stack.last().pose();
            int backgroundAlpha = (int) (alpha * 0.4f * 255);
            RenderUtils.drawSolid(pose, (int) center - 2, y + height + 1, (int) center + textWidth + 2, y + height + 15, backgroundAlpha << 24);
            float progress = skill.getCooldown() / (float) skill.getMaxCooldown();
            RenderUtils.drawSolid(pose, (int) center - 2, y + height + 14, (int) center + (int) (progress * (textWidth + 2)), y + height + 15, 0xff77 << 8 | backgroundAlpha << 24);
            renderer.drawShadow(stack, text, center, y + height + 3, 0xFFFFFF | a << 24);
        }
    }
}
