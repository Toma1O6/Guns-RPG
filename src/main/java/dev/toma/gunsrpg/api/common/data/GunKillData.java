package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.common.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.INBTSerializable;

public class GunKillData implements IKillData, ILockStateChangeable, INBTSerializable<CompoundNBT> {

    private final PlayerEntity player;
    private int killCount;
    private int requiredKillCount;
    private int level;
    private int points;

    public GunKillData(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void advanceLevel(boolean notify) {
        requiredKillCount = updateKillRequirement();
        switch (level) {
            case 8:
                awardPoints(2);
                player.addItem(new ItemStack(ModItems.GOLD_EGG_SHARD));
                break;
            case 7:
            case 5:
            case 3:
                awardPoints(1);
                break;
        }
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public void awardPoints(int points) {
        this.points += points;
    }

    @Override
    public void onEnemyKilled(Entity enemy, ItemStack weapon) {
        ++killCount;
        if (level < getLevelLimit() && killCount >= requiredKillCount) {
            killCount = 0;
            advanceLevel(false);
            String msg = String.format("%s%s has leveled up!", TextFormatting.GREEN, weapon.getDisplayName().getString());
            player.sendMessage(new StringTextComponent(msg), Util.NIL_UUID);
        }
    }

    @Override
    public int getKills() {
        return killCount;
    }

    @Override
    public int getRequiredKillCount() {
        return requiredKillCount;
    }

    @Override
    public int getLevelLimit() {
        return 7;
    }

    @Override
    public void doUnlock() {
        level = getLevelLimit();
        requiredKillCount = 0;
        points = Short.MAX_VALUE;
    }

    @Override
    public void doLock() {
        level = 0;
        points = 0;
        killCount = 0;
        requiredKillCount = updateKillRequirement();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("killCount", killCount);
        nbt.putInt("requiredKillCount", requiredKillCount);
        nbt.putInt("level", level);
        nbt.putInt("points", points);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        killCount = nbt.getInt("killCount");
        requiredKillCount = nbt.getInt("requiredKillCount");
        level = nbt.getInt("level");
        points = nbt.getInt("points");
    }

    private int updateKillRequirement() {
        switch (level) {
            case 0:
                return 15;
            case 1:
                return 30;
            case 2:
                return 50;
            case 3:
                return 100;
            case 4:
                return 180;
            case 5:
                return 240;
            case 6:
                return 350;
            default:
                return 0;
        }
    }
}
