package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.common.item.guns.GunItem;

public interface IProgressData extends ILockStateChangeable, IKillData, ITransactionProcessor {

    void onLogIn();

    IKillData getWeaponStats(GunItem item);
}
