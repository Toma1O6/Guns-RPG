package dev.toma.gunsrpg.common.skills.core;

import dev.toma.gunsrpg.api.common.skill.ISkillProperties;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidator;
import dev.toma.gunsrpg.api.common.data.IKillData;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ITransactionProcessor;
import dev.toma.gunsrpg.common.skills.transaction.SkillPointTransaction;

public class PlayerLevelTransactionValidator implements ITransactionValidator {

    @Override
    public boolean canUnlock(IPlayerData data, SkillType<?> skillType) {
        IKillData killData = data.getGenericData();
        ISkillProperties properties = skillType.getProperties();
        return killData.getLevel() >= properties.getRequiredLevel() && killData.getPoints() >= properties.getPrice();
    }

    @Override
    public void onUnlocked(ITransactionProcessor processor, SkillType<?> skillType) {
        processor.processTransaction(new SkillPointTransaction(skillType));
    }
}
