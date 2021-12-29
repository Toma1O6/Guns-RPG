package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.util.object.Pair;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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

    public ITextComponent[] generateDescription() {
        ChancesContainer container = data[level - 1];
        ITextComponent[] components = new ITextComponent[4];
        container.append(getType(), components);
        return components;
    }

    private static final class ChancesContainer {

        private final float extraPlanks;
        private final float extraSticks;

        public ChancesContainer(float extraPlanks, float extraSticks) {
            this.extraPlanks = extraPlanks;
            this.extraSticks = extraSticks;
        }

        private void append(SkillType<?> type, ITextComponent[] desc) {
            desc[0] = SkillUtil.Localizations.translation(type, "planks", SkillUtil.Localizations.percent(extraPlanks));
            desc[1] = SkillUtil.Localizations.translation(type, "sticks", SkillUtil.Localizations.percent(extraSticks));
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
