package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class GunStats {

    private final Supplier<GunItem> type;
    private int weaponLevel = 1;
    private int requiredKillCount = 16;
    private int kills = 0;

    public GunStats(Supplier<GunItem> type) {
        this.type = type;
    }

    // TODO add skills
    public void onKill(Entity entity) {
        if(entity instanceof IMob) {
            if(kills >= requiredKillCount) {
                requiredKillCount = 16 * (int) Math.pow(2, weaponLevel);
                kills = 0;
                ++weaponLevel;
            }
        }
    }

    public NBTTagCompound save() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("level", weaponLevel);
        nbt.setInteger("required", requiredKillCount);
        nbt.setInteger("kills", kills);
        return nbt;
    }

    public void load(@Nullable NBTTagCompound nbt) {
        if(nbt == null) return;
        weaponLevel = nbt.getInteger("level");
        requiredKillCount = nbt.getInteger("required");
        kills = nbt.getInteger("kills");
    }
}
