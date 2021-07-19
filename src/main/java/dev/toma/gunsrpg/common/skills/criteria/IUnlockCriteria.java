package dev.toma.gunsrpg.common.skills.criteria;

import dev.toma.gunsrpg.common.capability.IPlayerData;
import dev.toma.gunsrpg.common.skills.core.SkillType;

public interface IUnlockCriteria {

    boolean isUnlockAvailable(IPlayerData data, SkillType<?> skillType);

    void onActivated(IPlayerData data, SkillType<?> skillType);
}
