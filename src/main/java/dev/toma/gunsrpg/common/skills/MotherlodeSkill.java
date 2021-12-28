package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.object.Pair;

public class MotherlodeSkill extends BasicSkill {

    private static ChancesContainer[] data;

    private final int level;

    public MotherlodeSkill(SkillType<?> type, int level) {
        super(type);
        this.level = level;
    }

    public Pair<Float, Float> getDropChances() {
        ChancesContainer chancesContainer = data[level - 1];
        return Pair.of(chancesContainer.doubleDrop, chancesContainer.trippleDrop);
    }

    private static class ChancesContainer {

        private final float doubleDrop;
        private final float trippleDrop;

        public ChancesContainer(float doubleDrop, float trippleDrop) {
            this.doubleDrop = doubleDrop;
            this.trippleDrop = trippleDrop;
        }
    }

    static {
        data = new ChancesContainer[] {
                new ChancesContainer(0.10F, 0.00F),
                new ChancesContainer(0.20F, 0.00F),
                new ChancesContainer(0.35F, 0.00F),
                new ChancesContainer(0.50F, 0.15F),
                new ChancesContainer(0.65F, 0.25F)
        };
    }
}
