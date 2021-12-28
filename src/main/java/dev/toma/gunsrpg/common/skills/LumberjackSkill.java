package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.object.Pair;

public class LumberjackSkill extends BasicSkill {

    private static final ChancesContainer[] data;

    private final int level;

    public LumberjackSkill(SkillType<?> type, int level) {
        super(type);
        this.level = level;
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
