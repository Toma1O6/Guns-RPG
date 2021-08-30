package dev.toma.gunsrpg.api.common;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.skills.core.SkillType;

public interface IUnlockCriteria {

    boolean isUnlockAvailable(IPlayerData data, SkillType<?> skillType);

    void onActivated(IPlayerData data, SkillType<?> skillType);
}
