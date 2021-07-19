package dev.toma.gunsrpg.common.skills.criteria;

import dev.toma.gunsrpg.common.capability.IPlayerData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.skills.core.SkillType;

public class DefaultUnlockCriteria implements IUnlockCriteria {

    @Override
    public boolean isUnlockAvailable(IPlayerData data, SkillType<?> skillType) {
        PlayerSkills skills = data.getSkills();
        return skills.getLevel() >= skillType.levelRequirement && skills.getSkillPoints() >= skillType.price;
    }

    @Override
    public void onActivated(IPlayerData data, SkillType<?> skillType) {
        PlayerSkills skills = data.getSkills();
        skills.addSkillPoints(-skillType.price);
        skills.unlockSkill(skillType);
    }
}
