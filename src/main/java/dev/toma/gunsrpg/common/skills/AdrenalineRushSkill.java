package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.common.attribute.IValueFormatter;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;

// TODO apply temporary attribute modifier to reload speed and attack speed
public class AdrenalineRushSkill extends BasicSkill implements IDescriptionProvider {

    private static final float[] MODIFIERS = new float[]{0.85F, 0.70F, 0.50F};
    private final int level;
    private final float reloadMultiplier;

    public AdrenalineRushSkill(SkillType<?> type, int level, float reloadMultiplier) {
        super(type);
        this.level = level;
        this.reloadMultiplier = reloadMultiplier;
    }

    @Override
    public ITextComponent[] supplyDescription(int desiredLineCount) {
        ITextComponent[] components = new ITextComponent[desiredLineCount + 2];
        SkillUtil.Localizations.prepareEmptyDescriptionLines(getType(), desiredLineCount, components);
        int first = desiredLineCount - 1;
        components[first] = SkillUtil.Localizations.translation(getType(), "attack", IValueFormatter.INV_PERCENT.formatAttributeValue(getAttackSpeedBoost()));
        components[first + 1] = SkillUtil.Localizations.translation(getType(), "reload", IValueFormatter.PERCENT.formatAttributeValue(getReloadMultiplier()));
        return components;
    }

    public float getAttackSpeedBoost() {
        return MODIFIERS[level];
    }

    public float getReloadMultiplier() {
        return reloadMultiplier;
    }

    @Override
    public boolean canApply(PlayerEntity user) {
        return user.getHealth() <= 8;
    }
}
