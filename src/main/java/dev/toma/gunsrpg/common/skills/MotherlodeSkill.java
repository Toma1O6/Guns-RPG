package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.object.Pair;

import java.util.HashMap;
import java.util.Map;

public class MotherlodeSkill extends BasicSkill {

    private static final Map<Integer, Pair<Float, Float>> chances;
    private final int level;

    public MotherlodeSkill(SkillType<?> type, int level) {
        super(type);
        this.level = level;
    }

    public Pair<Float, Float> getDropChances() {
        return chances.containsKey(level) ? chances.get(level) : Pair.of(0.0F, 0.0F);
    }

    static {
        chances = new HashMap<>();
        chances.put(1, Pair.of(0.10F, 0.00F));
        chances.put(2, Pair.of(0.20F, 0.00F));
        chances.put(3, Pair.of(0.35F, 0.00F));
        chances.put(4, Pair.of(0.50F, 0.15F));
        chances.put(5, Pair.of(0.65F, 0.25F));
    }
}
