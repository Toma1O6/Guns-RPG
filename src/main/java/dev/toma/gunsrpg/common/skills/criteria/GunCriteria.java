package dev.toma.gunsrpg.common.skills.criteria;

import dev.toma.gunsrpg.api.common.data.IKillData;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ITransactionProcessor;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.transaction.WeaponPointTransaction;

import java.util.function.Supplier;

public class GunCriteria extends PlayerLevelCriteria {

    private final Supplier<GunItem> gunItemSupplier;

    public GunCriteria(Supplier<GunItem> gunItemSupplier) {
        this.gunItemSupplier = gunItemSupplier;
    }

    @Override
    public boolean isUnlockAvailable(IPlayerData data, SkillType<?> skillType) {
        IKillData killData = data.getGenericData().getWeaponStats(gunItemSupplier.get());
        return killData != null && killData.getPoints() >= skillType.price && killData.getLevel() >= skillType.levelRequirement;
    }

    @Override
    public void onActivated(ITransactionProcessor processor, SkillType<?> skillType) {
        processor.processTransaction(new WeaponPointTransaction(WeaponPointTransaction.IWeaponData.of(gunItemSupplier.get(), skillType)));
    }
}
