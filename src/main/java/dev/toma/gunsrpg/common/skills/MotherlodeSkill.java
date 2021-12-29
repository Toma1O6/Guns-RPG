package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.util.object.Pair;
import net.minecraft.util.text.ITextComponent;

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

    public ITextComponent[] generateDescription() {
        ChancesContainer chancesContainer = data[level - 1];
        ITextComponent[] components = new ITextComponent[chancesContainer.getParameterCount() + 2];
        chancesContainer.appendInfo(getType(), components);
        return components;
    }

    private static class ChancesContainer {

        private final float doubleDrop;
        private final float trippleDrop;

        public ChancesContainer(float doubleDrop, float trippleDrop) {
            this.doubleDrop = doubleDrop;
            this.trippleDrop = trippleDrop;
        }

        int getParameterCount() {
            return trippleDrop > 0 ? 2 : 1;
        }

        void appendInfo(SkillType<?> type, ITextComponent[] components) {
            components[0] = SkillUtil.Localizations.translation(type, "double", SkillUtil.Localizations.percent(doubleDrop));
            if (trippleDrop > 0) {
                components[1] = SkillUtil.Localizations.translation(type, "tripple", SkillUtil.Localizations.percent(trippleDrop));
            }
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
