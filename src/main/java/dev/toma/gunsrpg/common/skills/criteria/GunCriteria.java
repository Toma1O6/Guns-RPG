package dev.toma.gunsrpg.common.skills.criteria;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.object.GunData;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skills.core.SkillType;

import java.util.Map;
import java.util.function.Supplier;

public class GunCriteria extends DefaultUnlockCriteria {

    private final Supplier<GunItem> gunItemSupplier;

    public GunCriteria(Supplier<GunItem> gunItemSupplier) {
        this.gunItemSupplier = gunItemSupplier;
    }

    @Override
    public boolean isUnlockAvailable(PlayerData data, SkillType<?> skillType) {
        Map<GunItem, GunData> stats = data.getSkills().getGunStats();
        GunData v = stats.get(gunItemSupplier.get());
        return v != null && v.getGunPoints() >= skillType.price && v.getLevel() >= skillType.levelRequirement;
    }

    @Override
    public void onActivated(PlayerData data, SkillType<?> skillType) {
        PlayerSkills skills = data.getSkills();
        GunItem key = gunItemSupplier.get();
        skills.getGunData(gunItemSupplier.get()).consumePoint();
        skills.unlockSkill(skillType);
    }
}
