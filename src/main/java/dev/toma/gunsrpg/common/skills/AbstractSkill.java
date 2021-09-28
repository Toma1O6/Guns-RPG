package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.api.common.ISkill;
import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

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
