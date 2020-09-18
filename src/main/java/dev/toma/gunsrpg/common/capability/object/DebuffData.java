package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.debuffs.Debuff;
import dev.toma.gunsrpg.debuffs.DebuffTypes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.function.Consumer;

public class DebuffData implements INBTSerializable<NBTTagCompound> {

    private final Debuff[] debuffs = {
            DebuffTypes.POISON.createInstance(),
            DebuffTypes.INFECTION.createInstance(),
            DebuffTypes.BROKEN_BONE.createInstance(),
            DebuffTypes.BLEED.createInstance()
    };

    public Debuff[] getDebuffs() {
        return debuffs;
    }

    public void forEachDebuff(Consumer<Debuff> action) {
        for(Debuff debuff : debuffs) {
            action.accept(debuff);
        }
    }

    public void onTick(EntityPlayer player) {
        forEachDebuff(b -> b.onTick(player));
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("poison", debuffs[0].serializeNBT());
        nbt.setTag("infection", debuffs[1].serializeNBT());
        nbt.setTag("brokenBone", debuffs[2].serializeNBT());
        nbt.setTag("bleed", debuffs[3].serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        debuffs[0].deserializeNBT(nbt.hasKey("poison") ? nbt.getCompoundTag("poison") : new NBTTagCompound());
        debuffs[1].deserializeNBT(nbt.hasKey("infection") ? nbt.getCompoundTag("infection") : new NBTTagCompound());
        debuffs[2].deserializeNBT(nbt.hasKey("brokenBone") ? nbt.getCompoundTag("brokenBone") : new NBTTagCompound());
        debuffs[3].deserializeNBT(nbt.hasKey("bleed") ? nbt.getCompoundTag("bleed") : new NBTTagCompound());
    }
}
