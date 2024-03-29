package dev.toma.gunsrpg.common.skills.core;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IKillData;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.api.common.data.ITransactionProcessor;
import dev.toma.gunsrpg.api.common.skill.ISkillProperties;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skills.transaction.WeaponPointTransaction;
import net.minecraft.util.ResourceLocation;

public class WeaponTransactionValidator extends PlayerLevelTransactionValidator {

    public static final ResourceLocation ID = GunsRPG.makeResource("weapon");

    final GunItem item;

    public WeaponTransactionValidator(GunItem item) {
        this.item = item;
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public JsonElement getData() {
        return new JsonPrimitive(item.getRegistryName().toString());
    }

    @Override
    public boolean canUnlock(IPlayerData data, SkillType<?> skillType) {
        IKillData killData = data.getProgressData().getWeaponStats(item);
        ISkillProperties properties = skillType.getProperties();
        return killData != null && killData.getPoints() >= properties.getPrice() && killData.getLevel() >= properties.getRequiredLevel();
    }

    @Override
    public void onUnlocked(ITransactionProcessor processor, SkillType<?> skillType) {
        processor.processTransaction(new WeaponPointTransaction(WeaponPointTransaction.IWeaponData.of(item, skillType)));
    }

    @Override
    public IKillData getData(IPlayerData data) {
        return data.getProgressData().getWeaponStats(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeaponTransactionValidator that = (WeaponTransactionValidator) o;
        return ID.equals(that.getId()) && item.getRegistryName().equals(that.item.getRegistryName());
    }

    @Override
    public int hashCode() {
        int result = ID.hashCode();
        result = 31 * result + item.hashCode();
        return result;
    }
}
