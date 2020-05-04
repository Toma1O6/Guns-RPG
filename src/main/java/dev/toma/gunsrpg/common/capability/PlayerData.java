package dev.toma.gunsrpg.common.capability;

import dev.toma.gunsrpg.common.capability.object.DebuffData;
import dev.toma.gunsrpg.common.capability.object.GunStats;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public interface PlayerData extends INBTSerializable<NBTTagCompound> {

    GunStats getStats(GunItem item);

    DebuffData getDebuffData();

    void tick();

    void sync();
}
