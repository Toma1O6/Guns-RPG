package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.PlayerEntity;

public class AdrenalineRushSkill extends BasicSkill {

    private static final float[] MODIFIERS = new float[]{0.85F, 0.70F, 0.50F};
    private final int level;
    private final float reloadMultiplier;

    public AdrenalineRushSkill(SkillType<?> type, int level, float reloadMultiplier) {
        super(type);
        this.level = level;
        this.reloadMultiplier = reloadMultiplier;
    }

    public float getAttackSpeedBoost() {
        return MODIFIERS[level];
    }

    public float getReloadMultiplier() {
        return reloadMultiplier;
    }

    @Override
    public boolean apply(PlayerEntity user) {
        return user.getHealth() <= 8;
    }
}
