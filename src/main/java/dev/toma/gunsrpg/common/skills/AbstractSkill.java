package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.init.ModRegistries;
import dev.toma.gunsrpg.api.common.ISkill;
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
    public final CompoundNBT saveData() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("type", type.getRegistryName().toString());
        writeExtra(nbt);
        return nbt;
    }

    @Override
    public final void readData(CompoundNBT nbt) {
        SkillType<?> type = ModRegistries.SKILLS.getValue(new ResourceLocation(nbt.getString("type")));
        if (type != this.type) {
            throw new IllegalStateException("Loaded wrong SkillType");
        }
        readExtra(nbt);
    }

    public void writeExtra(CompoundNBT nbt) {

    }

    public void readExtra(CompoundNBT nbt) {

    }
}
