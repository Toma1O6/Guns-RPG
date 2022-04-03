package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.common.LootStashDetectorHandler;
import dev.toma.gunsrpg.common.skills.core.DescriptionContainer;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.util.text.ITextComponent;

public class TreasureHunterSkill extends SimpleSkill implements IDescriptionProvider {

    private static final DetectionRadius[] RADII = {
            new DetectionRadius(40.0F, 6.0F),
            new DetectionRadius(55.0F, 5.0F),
            new DetectionRadius(75.0F, 3.0F)
    };
    private final DescriptionContainer container;
    private final int level;

    public TreasureHunterSkill(SkillType<? extends TreasureHunterSkill> type, int level) {
        super(type);
        this.level = level - 1;
        DetectionRadius radius = this.getRadius();
        this.container = new DescriptionContainer(type);
        this.container.addProperty("maxRange", Math.round(radius.maxRange));
        this.container.addProperty("sensitivity", Math.round(radius.foundRange));
    }

    @Override
    public ITextComponent[] supplyDescription(int desiredLineCount) {
        return SkillUtil.Localizations.generateAndMerge(desiredLineCount, getType(), container.getLines());
    }

    public DetectionRadius getRadius() {
        return RADII[level];
    }

    public static final class DetectionRadius {

        private final float maxRange;
        private final float foundRange;

        DetectionRadius(float maxRange, float foundRange) {
            this.maxRange = maxRange;
            this.foundRange = foundRange;
        }

        public float getSoundIntensity(double distance) {
            if (distance <= foundRange) {
                return 1.0F;
            }
            if (distance > maxRange) {
                return 0.0F;
            }
            double diff = distance - foundRange;
            double limit = maxRange - foundRange;
            return (float) (diff / limit);
        }

        public LootStashDetectorHandler.Status getStatusByDistance(double distance) {
            if (distance <= foundRange) {
                return LootStashDetectorHandler.Status.LOCATED;
            } else if (distance <= maxRange) {
                return LootStashDetectorHandler.Status.NEARBY;
            }
            return LootStashDetectorHandler.Status.UNDETECTED;
        }
    }
}
