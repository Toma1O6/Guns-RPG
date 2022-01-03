package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.skill.ISkill;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.nbt.CompoundNBT;

public abstract class AbstractSkill implements ISkill {

    private final SkillType<?> type;

    public AbstractSkill(SkillType<?> type) {
        this.type = type;
    }

    @Override
    public final SkillType<?> getType() {
        return type;
    }

    @Override
    public CompoundNBT saveData() {
        return null;
    }

    @Override
    public void readData(CompoundNBT nbt) {}
}
