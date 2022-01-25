package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.skill.IDescriptionProvider;
import dev.toma.gunsrpg.common.attribute.IValueFormatter;
import dev.toma.gunsrpg.common.skills.core.DescriptionContainer;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.util.text.ITextComponent;

import java.util.Random;

public class MotherlodeSkill extends BasicSkill implements IDescriptionProvider {

    private static final ChancesContainer[] DATA;

    private final DescriptionContainer container;
    private final int level;

    public MotherlodeSkill(SkillType<?> type, int level) {
        super(type);
        this.level = level;
        this.container = new DescriptionContainer(type);
        DATA[level - 1].appendInfo(container);
    }

    public int getDropMultiplier(Random random) {
        ChancesContainer container = DATA[level - 1];
        float f = random.nextFloat();
        return f < container.trippleDrop ? 3 : (f - container.trippleDrop) < container.doubleDrop ? 2 : 1;
    }

    @Override
    public ITextComponent[] supplyDescription(int desiredLineCount) {
        return container.getLines();
    }

    private static class ChancesContainer {

        private final float doubleDrop;
        private final float trippleDrop;

        public ChancesContainer(float doubleDrop, float trippleDrop) {
            this.doubleDrop = doubleDrop;
            this.trippleDrop = trippleDrop;
        }

        void appendInfo(DescriptionContainer container) {
            container.addProperty("double", IValueFormatter.PERCENT.formatAttributeValue(doubleDrop));
            if (trippleDrop > 0) {
                container.addProperty("tripple", IValueFormatter.PERCENT.formatAttributeValue(trippleDrop));
            }
        }
    }

    static {
        DATA = new ChancesContainer[] {
                new ChancesContainer(0.10F, 0.00F),
                new ChancesContainer(0.20F, 0.00F),
                new ChancesContainer(0.35F, 0.00F),
                new ChancesContainer(0.50F, 0.15F),
                new ChancesContainer(0.65F, 0.25F)
        };
    }
}
