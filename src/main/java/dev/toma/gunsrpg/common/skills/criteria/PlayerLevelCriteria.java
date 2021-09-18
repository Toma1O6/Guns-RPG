package dev.toma.gunsrpg.common.skills.criteria;

import dev.toma.gunsrpg.api.common.IUnlockCriteria;
import dev.toma.gunsrpg.api.common.data.IKillData;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ITransactionProcessor;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.transaction.SkillPointTransaction;

public class PlayerLevelCriteria implements IUnlockCriteria {

    @Override
    public boolean isUnlockAvailable(IPlayerData data, SkillType<?> skillType) {
        IKillData killData = data.getGenericData();
        return killData.getLevel() >= skillType.levelRequirement && killData.getPoints() >= skillType.price;
    }

    @Override
    public void onActivated(ITransactionProcessor processor, SkillType<?> skillType) {
        processor.processTransaction(new SkillPointTransaction(skillType));
    }
}
