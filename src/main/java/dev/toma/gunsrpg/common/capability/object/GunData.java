package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.common.init.GRPGItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class GunData {

    private int kills;
    private int requiredKills = 15;
    private int level = 1;
    private int gunPoints;

    public void unlockAll() {
        this.kills = 0;
        this.requiredKills = 1;
        this.level = 8;
        this.gunPoints = 0;
    }

    public void awardKill(PlayerEntity player) {
        ++kills;
        if (!isAtMaxLevel()) {
            if (kills >= requiredKills) {
                kills = 0;
                ++level;
                onLevelUp(player);
                player.sendMessage(new StringTextComponent(TextFormatting.GREEN + "Reached next weapon level!"), Util.NIL_UUID);
            }
        }
    }

    public void onLevelUp(PlayerEntity player) {
        requiredKills = getRequiredKills(level);
        switch (level) {
            case 8:
                awardPoints(2);
                player.addItem(new ItemStack(GRPGItems.GOLD_EGG_SHARD));
                break;
            case 3:
            case 5:
            case 7:
                awardPoints(1);
                break;
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

    public CompoundNBT saveData() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("level", level);
        nbt.putInt("kills", kills);
        nbt.putInt("points", gunPoints);
        return nbt;
    }

    public void readData(CompoundNBT nbt) {
        level = nbt.getInt("level");
        kills = nbt.getInt("kills");
        gunPoints = nbt.getInt("points");
        requiredKills = getRequiredKills(level);
    }

    private int getRequiredKills(int level) {
        switch (level) {
            case 1:
                return 15;
            case 2:
                return 30;
            case 3:
                return 50;
            case 4:
                return 100;
            case 5:
                return 180;
            case 6:
                return 240;
            case 7:
                return 350;
            default:
                return 0;
        }
    }
}
