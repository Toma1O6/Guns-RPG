package dev.toma.gunsrpg.common.skills;

import dev.toma.gunsrpg.common.init.GunsRPGRegistries;
import dev.toma.gunsrpg.common.skills.core.ISkill;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import net.minecraft.nbt.NBTTagCompound;
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
    public final NBTTagCompound saveData() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("type", type.getRegistryName().toString());
        writeExtra(nbt);
        return nbt;
    }

    @Override
    public final void readData(NBTTagCompound nbt) {
        SkillType<?> type = GunsRPGRegistries.SKILLS.getValue(new ResourceLocation(nbt.getString("type")));
        if(type != this.type) {
            throw new IllegalStateException("Loaded wrong SkillType");
        }
        readExtra(nbt);
    }

    public void writeExtra(NBTTagCompound nbt) {

    }

    public void readExtra(NBTTagCompound nbt) {

    }
}
