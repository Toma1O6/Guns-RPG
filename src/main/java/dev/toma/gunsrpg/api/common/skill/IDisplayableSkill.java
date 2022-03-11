package dev.toma.gunsrpg.api.common.skill;

import net.minecraft.util.ResourceLocation;

@Deprecated
public interface IDisplayableSkill extends ISkill {

    ResourceLocation getDisplayIcon();
}
