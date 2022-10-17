package dev.toma.gunsrpg.api.common.data;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidator;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidatorFactory;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skills.core.TransactionValidatorRegistry;
import dev.toma.gunsrpg.common.skills.core.WeaponTransactionValidator;
import dev.toma.gunsrpg.resource.progression.FakeLevelingStrategy;
import dev.toma.gunsrpg.resource.progression.IProgressionStrategy;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.helper.JsonHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.INBTSerializable;

public class GunKillData implements IKillData, ILockStateChangeable, INBTSerializable<CompoundNBT> {

    private final PlayerEntity player;
    private final IProgressionStrategy strategy;
    private int killCount;
    private int requiredKillCount;
    private int level;
    private int points;

    public GunKillData(PlayerEntity player, GunItem gunItem) {
        this.player = player;
        this.level = 1;
        ITransactionValidatorFactory<?, ?> factory = TransactionValidatorRegistry.getValidatorFactory(WeaponTransactionValidator.ID);
        ITransactionValidator validator = TransactionValidatorRegistry.getTransactionValidator(factory, JsonHelper.toSimpleJson(gunItem.getRegistryName()));
        this.strategy = ModUtils.firstNonnull(GunsRPG.getModLifecycle().getProgressionStrategyManager().getStrategy(validator), FakeLevelingStrategy.INSTANCE);
        requiredKillCount = this.updateKillRequirement();
    }

    @Override
    public void addLevels(int levels) {
        this.level = Math.min(getLevelLimit(), level + levels);
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void advanceLevel(boolean notify) {
        this.level = Math.min(level + 1, getLevelLimit());
        requiredKillCount = updateKillRequirement();
        strategy.applyRewards(player, this, level);
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
        if (level < getLevelLimit() && requiredKillCount <= killCount) {
            killCount = 0;
            advanceLevel(false);
            ITextComponent msg = new TranslationTextComponent("text.weapon.level_up", weapon.getDisplayName().getString()).withStyle(TextFormatting.GREEN);
            player.sendMessage(msg, Util.NIL_UUID);
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
        return 9;
    }

    @Override
    public void doUnlock() {
        level = getLevelLimit();
        requiredKillCount = 0;
        points = Integer.MAX_VALUE;
    }

    @Override
    public void doLock() {
        level = 1;
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
        level = Math.max(nbt.getInt("level"), 1);
        points = nbt.getInt("points");
    }

    private int updateKillRequirement() {
        return strategy.getRequiredKillCount(level);
    }
}
