package dev.toma.gunsrpg.api.common.skill;

import net.minecraft.util.text.ITextComponent;

public interface ISkillDisplay {

    ITextComponent getTitle();

    ITextComponent[] getDescription();

    ISkillRenderProperties getRenderProperties();
}
