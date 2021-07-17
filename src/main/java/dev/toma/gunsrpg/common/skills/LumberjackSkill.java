package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.object.Pair;

import java.util.HashMap;
import java.util.Map;

public class LumberjackSkill extends BasicSkill {

    private static final Map<Integer, Pair<Float, Float>> chances;

    static {
        chances = new HashMap<>();
        chances.put(1, Pair.of(0.10F, 0.20F));
        chances.put(2, Pair.of(0.25F, 0.40F));
        chances.put(3, Pair.of(0.40F, 0.60F));
        chances.put(4, Pair.of(0.55F, 0.80F));
        chances.put(5, Pair.of(0.70F, 0.95F));
    }

    private final int level;

    public LumberjackSkill(SkillType<?> type, int level) {
        super(type);
        this.level = level;
    }

    public Pair<Float, Float> getDropChances() {
        return chances.containsKey(level) ? chances.get(level) : Pair.of(0.0F, 0.0F);
    }
}
