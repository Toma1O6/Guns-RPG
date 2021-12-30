package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.common.attribute.IValueFormatter;
import dev.toma.gunsrpg.common.skills.core.DescriptionContainer;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;

// TODO apply temporary attribute modifier to reload speed and attack speed
public class AdrenalineRushSkill extends BasicSkill implements IDescriptionProvider {

    private final DescriptionContainer container;
    private static final float[] MODIFIERS = new float[]{0.85F, 0.70F, 0.50F};
    private final int level;
    private final float reloadMultiplier;

    public AdrenalineRushSkill(SkillType<?> type, int level, float reloadMultiplier) {
        super(type);
        this.level = level;
        this.reloadMultiplier = reloadMultiplier;
        this.container = new DescriptionContainer(type);
        this.container.addProperty("attack", IValueFormatter.INV_PERCENT.formatAttributeValue(getAttackSpeedBoost()));
        this.container.addProperty("reload", IValueFormatter.PERCENT.formatAttributeValue(reloadMultiplier));
    }

    @Override
    public ITextComponent[] supplyDescription(int desiredLineCount) {
        return container.getLines();
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
