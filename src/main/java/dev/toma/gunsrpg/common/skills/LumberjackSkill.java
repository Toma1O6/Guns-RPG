package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.attribute.IValueFormatter;
import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.common.skills.core.DescriptionContainer;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.object.Pair;
import net.minecraft.util.text.ITextComponent;

public class LumberjackSkill extends SimpleSkill implements IDescriptionProvider {

    private static final ChancesContainer[] data;

    private final DescriptionContainer container;
    private final int level;

    public LumberjackSkill(SkillType<?> type, int level) {
        super(type);
        this.level = level;
        this.container = new DescriptionContainer(type);
        data[level - 1].append(container);
    }

    @Override
    public ITextComponent[] supplyDescription(int desiredLineCount) {
        return container.getLines();
    }

    public Pair<Float, Float> getDropChances() {
        ChancesContainer chancesContainer = data[level - 1];
        return Pair.of(chancesContainer.extraPlanks, chancesContainer.extraSticks);
    }

    private static final class ChancesContainer {

        private final float extraPlanks;
        private final float extraSticks;

        public ChancesContainer(float extraPlanks, float extraSticks) {
            this.extraPlanks = extraPlanks;
            this.extraSticks = extraSticks;
        }

        private void append(DescriptionContainer container) {
            container.addProperty("plank", IValueFormatter.PERCENT.formatAttributeValue(extraPlanks));
            container.addProperty("stick", IValueFormatter.PERCENT.formatAttributeValue(extraSticks));
        }
    }

    static {
        data = new ChancesContainer[] {
                new ChancesContainer(0.10F, 0.20F),
                new ChancesContainer(0.25F, 0.40F),
                new ChancesContainer(0.40F, 0.60F),
                new ChancesContainer(0.55F, 0.80F),
                new ChancesContainer(0.70F, 0.95F)
        };
    }
}
