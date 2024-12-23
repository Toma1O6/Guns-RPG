package dev.toma.gunsrpg.api.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.api.common.skill.ICooldown;
import dev.toma.gunsrpg.api.common.skill.ISkill;
import dev.toma.gunsrpg.common.skills.core.DisplayData;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.RenderUtils;
import net.minecraft.util.math.vector.Matrix4f;

public interface IHudSkillRenderer<S extends ISkill & ICooldown> extends ISkillRenderer<S> {

    default void renderOnHUD(MatrixStack stack, S skill, int x, int y, int width, int height) {
        Matrix4f pose = stack.last().pose();
        SkillType<?> type = skill.getType();
        DisplayData displayData = type.getDisplayData();
        RenderUtils.drawSolid(pose, x, y, x + width, y + height, 0x77 << 24);
        displayData.renderAt(stack, x + 2, y + 2);
    }
}
