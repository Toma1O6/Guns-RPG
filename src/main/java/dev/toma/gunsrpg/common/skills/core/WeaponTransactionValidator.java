package dev.toma.gunsrpg.common.skills.core;

import dev.toma.gunsrpg.api.common.data.IKillData;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ITransactionProcessor;
import dev.toma.gunsrpg.api.common.skill.ISkillProperties;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skills.transaction.WeaponPointTransaction;

public class WeaponTransactionValidator extends PlayerLevelTransactionValidator {

    final GunItem item;

    public WeaponTransactionValidator(GunItem item) {
        this.item = item;
    }

    @Override
    public boolean canUnlock(IPlayerData data, SkillType<?> skillType) {
        IKillData killData = data.getGenericData().getWeaponStats(item);
        ISkillProperties properties = skillType.getProperties();
        return killData != null && killData.getPoints() >= properties.getPrice() && killData.getLevel() >= properties.getRequiredLevel();
    }

    @Override
    public void onUnlocked(ITransactionProcessor processor, SkillType<?> skillType) {
        processor.processTransaction(new WeaponPointTransaction(WeaponPointTransaction.IWeaponData.of(item, skillType)));
    }
}
