package dev.toma.gunsrpg.common.capability.object;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.ITransaction;
import dev.toma.gunsrpg.api.common.data.*;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.transaction.TransactionManager;
import dev.toma.gunsrpg.common.skills.transaction.TransactionTypes;
import dev.toma.gunsrpg.common.skills.transaction.WeaponPointTransaction;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class PlayerGenericData implements IData, IPlayerCapEntry {

    private final PlayerEntity player;
    private final ISkillProvider skillProvider;
    private final ITransactionManager transactionManager = new TransactionManager();
    private final Map<GunItem, GunKillData> weaponStats = new HashMap<>();
    private IClientSynchReq request = () -> {};
    private boolean known;
    private int level;
    private int skillPoints;
    private int kills;
    private int requiredKills;

    public PlayerGenericData(PlayerEntity player, ISkillProvider skillProvider) {
        this.player = player;
        this.skillProvider = skillProvider;

        this.transactionManager.registerHandler(TransactionTypes.SKILLPOINT_TRANSACTION, this::hasEnoughSkillpoints, this::handleSkillpointTransaction);
        this.transactionManager.registerHandler(TransactionTypes.WEAPON_POINT_TRANSACTION, this::hasEnoughWeaponPoints, this::handleWeaponPointTransaction);
    }

    @Override
    public IKillData getWeaponStats(GunItem item) {
        return weaponStats.computeIfAbsent(item, k -> new GunKillData(player));
    }

    @Override
    public boolean hasFunds(ITransaction<?> transaction) {
        return transactionManager.hasFunds(transaction);
    }

    @Override
    public void processTransaction(ITransaction<?> transaction) {
        transactionManager.handleTransaction(transaction);
    }

    @Override
    public void onLogIn() {
        if (!known) {
            known = true;
            GunsRPG.getModLifecycle().getStartingGearManager().giveStartingGear(player);
            request.makeSyncRequest();
        }
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getLevelLimit() {
        return 100;
    }

    @Override
    public void advanceLevel(boolean notify) {
        this.level = Math.min(level + 1, getLevelLimit());
        this.kills = 0;
        awardPoints(this.getPointsToAward());
        updateRequiredKills();

        if (notify) {
            player.sendMessage(new StringTextComponent(TextFormatting.YELLOW + "=====[ LEVEL UP ]====="), Util.NIL_UUID);
            player.sendMessage(new StringTextComponent(TextFormatting.YELLOW + "Current level: " + level), Util.NIL_UUID);
            skillProvider.onLevelUp(level, player);
            request.makeSyncRequest();
        }
    }

    @Override
    public int getPoints() {
        return skillPoints;
    }

    @Override
    public void awardPoints(int points) {
        skillPoints += points;
    }

    @Override
    public void onEnemyKilled(Entity enemy, ItemStack weapon) {
        if (!(enemy instanceof IMob))
            return;
        ++kills;
        if (level < getLevelLimit() && kills > requiredKills) {
            advanceLevel(true);
        }
        if (weapon.getItem() instanceof GunItem) {
            GunItem gun = (GunItem) weapon.getItem();
            getWeaponStats(gun).onEnemyKilled(enemy, weapon);
        }
        request.makeSyncRequest();
    }

    @Override
    public void doUnlock() {
        level = getLevelLimit();
        skillPoints = Integer.MAX_VALUE;
        ForgeRegistries.ITEMS.getValues().stream()
                .filter(item -> item instanceof GunItem)
                .map(item -> (GunItem) item)
                .forEach(gun -> {
                    GunKillData killData = weaponStats.computeIfAbsent(gun, k -> new GunKillData(player));
                    killData.doUnlock();
                });
    }

    @Override
    public void doLock() {
        level = 0;
        skillPoints = 0;
        kills = 0;
        weaponStats.clear();
        updateRequiredKills();
    }

    @Override
    public int getKills() {
        return kills;
    }

    @Override
    public int getRequiredKillCount() {
        return requiredKills;
    }

    @Override
    public int getFlag() {
        return DataFlags.DATA;
    }

    @Override
    public void toNbt(CompoundNBT nbt) {
        CompoundNBT cnbt = new CompoundNBT();
        cnbt.putBoolean("known", known);
        cnbt.putInt("killCount", kills);
        cnbt.putInt("requiredKillCount", requiredKills);
        cnbt.putInt("level", level);
        cnbt.putInt("points", skillPoints);
        CompoundNBT killData = new CompoundNBT();
        for (Map.Entry<GunItem, GunKillData> entry : weaponStats.entrySet()) {
            String id = entry.getKey().getRegistryName().toString();
            killData.put(id, entry.getValue().serializeNBT());
        }
        cnbt.put("killData", killData);
        nbt.put("genericData", cnbt);
    }

    @Override
    public void fromNbt(CompoundNBT nbt) {
        weaponStats.clear();
        CompoundNBT cnbt = nbt.contains("genericData", Constants.NBT.TAG_COMPOUND) ? nbt.getCompound("genericData") : new CompoundNBT();
        known = cnbt.getBoolean("known");
        kills = cnbt.getInt("killCount");
        requiredKills = cnbt.getInt("requiredKillCount");
        level = cnbt.getInt("level");
        skillPoints = cnbt.getInt("points");
        CompoundNBT killData = cnbt.contains("killData", Constants.NBT.TAG_COMPOUND) ? cnbt.getCompound("killData") : new CompoundNBT();
        for (String key : killData.getAllKeys()) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(key));
            if (item instanceof GunItem) {
                GunItem gun = (GunItem) item;
                GunKillData data = new GunKillData(player);
                CompoundNBT dataNbt = killData.getCompound(key);
                data.deserializeNBT(dataNbt);
                weaponStats.put(gun, data);
            }
        }
    }

    @Override
    public void setClientSynch(IClientSynchReq request) {
        this.request = request;
    }

    private int getPointsToAward() {
        if (level == 100) {
            return 25;
        } else if (level == 50) {
            return 20;
        } else if (level % 10 == 0) {
            return 5;
        } else if (level % 5 == 0) {
            return 4;
        } else return 1;
    }

    private void updateRequiredKills() {
        if (level >= 95) {
            requiredKills = 100;
        } else if (level >= 90) {
            requiredKills = 85;
        } else if (level >= 80) {
            requiredKills = 70;
        } else if (level >= 70) {
            requiredKills = 60;
        } else if (level >= 60) {
            requiredKills = 50;
        } else if (level >= 50) {
            requiredKills = 45;
        } else if (level >= 40) {
            requiredKills = 35;
        } else if (level >= 30) {
            requiredKills = 30;
        } else if (level >= 20) {
            requiredKills = 25;
        } else if (level >= 10) {
            requiredKills = 15;
        } else requiredKills = 10;
    }

    private boolean hasEnoughSkillpoints(ITransaction<SkillType<?>> transaction) {
        return transaction.total() <= skillPoints;
    }

    private void handleSkillpointTransaction(ITransaction<SkillType<?>> transaction) {
        skillPoints -= transaction.total();
        skillProvider.unlock(transaction.getData());
    }

    private boolean hasEnoughWeaponPoints(ITransaction<WeaponPointTransaction.IWeaponData> transaction) {
        GunItem item = transaction.getData().item();
        IKillData data = getWeaponStats(item);
        return transaction.total() <= data.getPoints();
    }

    private void handleWeaponPointTransaction(ITransaction<WeaponPointTransaction.IWeaponData> transaction) {
        GunItem item = transaction.getData().item();
        IKillData data = getWeaponStats(item);
        data.awardPoints(-transaction.total());
        skillProvider.unlock(transaction.getData().skill());
    }
}
