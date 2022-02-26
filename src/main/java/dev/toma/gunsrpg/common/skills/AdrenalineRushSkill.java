package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.api.common.attribute.IValueFormatter;
import dev.toma.gunsrpg.common.skills.core.DescriptionContainer;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;

public class AdrenalineRushSkill extends BasicSkill implements IDescriptionProvider {

    private static final float[] MODIFIERS = new float[]{0.15F, 0.3F, 0.5F};
    private static final int HEALTH_LIMIT = 8;

    private final DescriptionContainer container;
    private final int level;
    private final float reloadMultiplier;

    public AdrenalineRushSkill(SkillType<?> type, int level, float reloadMultiplier) {
        super(type);
        this.level = level;
        this.reloadMultiplier = reloadMultiplier;
        this.container = new DescriptionContainer(type);
        this.container.addProperty("predicate", HEALTH_LIMIT);
        this.container.addProperty("attack", 100 - Math.round( this.getAttackSpeedBoost() * 100 ));
        this.container.addProperty("reload", IValueFormatter.PERCENT.formatAttributeValue(reloadMultiplier));
    }

    @Override
    public ITextComponent[] supplyDescription(int desiredLineCount) {
        return container.getLines();
    }

    public float getAttackSpeedBoost() {
        return 1.0F - MODIFIERS[level];
    }

    public float getReloadMultiplier() {
        return reloadMultiplier;
    }

    @Override
    public boolean canApply(PlayerEntity user) {
        return user.getHealth() <= HEALTH_LIMIT;
    }
}
