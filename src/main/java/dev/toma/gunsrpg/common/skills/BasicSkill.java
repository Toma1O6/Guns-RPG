package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.EntityPlayer;

public class BasicSkill extends AbstractSkill {

    public BasicSkill(SkillType<?> type) {
        super(type);
    }

    @Override
    public boolean apply(EntityPlayer user) {
        return true;
    }
}
