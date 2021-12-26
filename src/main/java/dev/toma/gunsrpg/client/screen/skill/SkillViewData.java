package dev.toma.gunsrpg.client.screen.skill;

import dev.toma.gunsrpg.common.skills.core.SkillCategory;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.math.Vec2iMutable;

public final class SkillViewData {

    private final Type type;
    private final Vec2iMutable pos;

    public SkillViewData(SkillType<?> skill, SkillCategory category, Vec2iMutable pos) {
        SkillCategory skillCategory = skill.getCategory();
        this.type = skillCategory.isInternal() ? Type.NONE : skillCategory == category ? Type.DEFAULT : Type.FOREIGN;
        this.pos = pos;
    }

    public boolean isValidForDisplay() {
        return type != Type.NONE;
    }

    public void move(int x, int y) {
        pos.growX(x);
        pos.growY(y);
    }

    public enum Type {
        DEFAULT,
        FOREIGN,
        NONE
    }
}
