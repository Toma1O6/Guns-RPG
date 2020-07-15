package dev.toma.gunsrpg.common.capability.object;

import net.minecraft.nbt.NBTTagCompound;

public class GunData {

    private int kills;
    private int requiredKills = 15;
    private int level = 1;
    private int gunPoints;

    public void awardKill() {
        ++kills;
        if(!isAtMaxLevel()) {
            if(kills >= requiredKills) {
                kills = 0;
                ++level;
                onLevelUp();
            }
        }
    }

    public void onLevelUp() {
        switch (level) {
            case 8: awardPoints(2); break;
            case 3: case 5: case 7: awardPoints(1); break;
        }
    }

    public boolean isAtMaxLevel() {
        return level == 8;
    }

    public void awardPoints(int amount) {
        this.gunPoints += amount;
    }

    public void consumePoint() {
        --gunPoints;
    }

    public boolean hasGunPoints() {
        return gunPoints > 0;
    }

    public int getKills() {
        return kills;
    }

    public int getRequiredKills() {
        return requiredKills;
    }

    public int getGunPoints() {
        return gunPoints;
    }

    public int getLevel() {
        return level;
    }

    public NBTTagCompound saveData() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("level", level);
        nbt.setInteger("kills", kills);
        nbt.setInteger("points", gunPoints);
        return nbt;
    }

    public void readData(NBTTagCompound nbt) {
        level = nbt.getInteger("level");
        kills = nbt.getInteger("kills");
        gunPoints = nbt.getInteger("points");
        requiredKills = getRequiredKills(level);
    }

    private int getRequiredKills(int level) {
        switch (level) {
            case 1: return 15;
            case 2: return 30;
            case 3: return 50;
            case 4: return 100;
            case 5: return 180;
            case 6: return 240;
            case 7: return 350;
            default: return 0;
        }
    }
}
