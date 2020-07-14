package dev.toma.gunsrpg.common.skills.criteria;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.skills.core.SkillType;

public interface UnlockCriteria {

    boolean isUnlockAvailable(PlayerData data, SkillType<?> skillType);

    void onActivated(PlayerData data, SkillType<?> skillType);
}
