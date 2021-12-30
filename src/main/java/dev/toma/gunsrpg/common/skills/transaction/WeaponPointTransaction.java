package dev.toma.gunsrpg.common.skills.transaction;

import dev.toma.gunsrpg.api.common.ITransaction;
import dev.toma.gunsrpg.api.common.ITransactionType;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skills.core.SkillType;

public class WeaponPointTransaction implements ITransaction<WeaponPointTransaction.IWeaponData> {

    private final IWeaponData data;

    public WeaponPointTransaction(IWeaponData data) {
        this.data = data;
    }

    @Override
    public int total() {
        return data.skill().getProperties().getPrice();
    }

    @Override
    public IWeaponData getData() {
        return data;
    }

    @Override
    public ITransactionType<IWeaponData> getType() {
        return TransactionTypes.WEAPON_POINT_TRANSACTION;
    }

    public interface IWeaponData {

        GunItem item();

        SkillType<?> skill();

        static IWeaponData of(GunItem item, SkillType<?> skill) {
            return new IWeaponData() {
                @Override
                public GunItem item() {
                    return item;
                }

                @Override
                public SkillType<?> skill() {
                    return skill;
                }
            };
        }
    }
}
