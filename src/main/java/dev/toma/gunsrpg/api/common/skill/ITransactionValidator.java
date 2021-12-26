package dev.toma.gunsrpg.api.common.skill;

import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ITransactionProcessor;
import dev.toma.gunsrpg.common.skills.core.SkillType;

public interface ITransactionValidator {

    boolean canUnlock(IPlayerData data, SkillType<?> skillType);

    void onUnlocked(ITransactionProcessor transactionProcessor, SkillType<?> skillType);
}
