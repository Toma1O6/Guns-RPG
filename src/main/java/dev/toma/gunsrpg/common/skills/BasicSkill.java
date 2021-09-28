package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.PlayerEntity;

public class BasicSkill extends AbstractSkill {

    public BasicSkill(SkillType<?> type) {
        super(type);
    }

    @Override
    public boolean canApply(PlayerEntity user) {
        return true;
    }
}
