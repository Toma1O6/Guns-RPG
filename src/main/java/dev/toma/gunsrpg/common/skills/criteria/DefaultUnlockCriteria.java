package dev.toma.gunsrpg.common.skills.criteria;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.skills.core.SkillType;

public class DefaultUnlockCriteria implements UnlockCriteria {

    @Override
    public boolean isUnlockAvailable(PlayerData data, SkillType<?> skillType) {
        PlayerSkills skills = data.getSkills();
        return skills.getLevel() >= skillType.levelRequirement && skills.getSkillPoints() >= skillType.price;
    }

    @Override
    public void onActivated(PlayerData data, SkillType<?> skillType) {
        PlayerSkills skills = data.getSkills();
        skills.addSkillPoints(-skillType.price);
        skills.unlockSkill(skillType);
    }
}
