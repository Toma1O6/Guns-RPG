package dev.toma.gunsrpg.api.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.skill.ISkill;
import net.minecraft.client.gui.FontRenderer;

public interface ISkillRenderer<S extends ISkill> {

    void renderExtraSkillData(S skill, MatrixStack stack, FontRenderer renderer, int x, int y, int width, int height, float alpha);
}
