package dev.toma.gunsrpg.common.capability.object;

import com.mojang.blaze3d.matrix.MatrixStack;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.ISyncRequestDispatcher;
import dev.toma.gunsrpg.api.common.ITransaction;
import dev.toma.gunsrpg.api.common.data.*;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidator;
import dev.toma.gunsrpg.api.common.skill.ITransactionValidatorFactory;
import dev.toma.gunsrpg.client.OverlayPlacement;
import dev.toma.gunsrpg.client.render.ProgressionRenderer;
import dev.toma.gunsrpg.common.init.ModItems;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.skills.core.PlayerLevelTransactionValidator;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.core.TransactionValidatorRegistry;
import dev.toma.gunsrpg.common.skills.transaction.TransactionManager;
import dev.toma.gunsrpg.common.skills.transaction.TransactionTypes;
import dev.toma.gunsrpg.common.skills.transaction.WeaponPointTransaction;
import dev.toma.gunsrpg.resource.progression.FakeLevelingStrategy;
import dev.toma.gunsrpg.resource.progression.IProgressionStrategy;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.math.IVec2i;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class PlayerProgressionData implements IProgressData, IPlayerCapEntry {

    private final PlayerEntity player;
    private final ISkillProvider skillProvider;
    private final IPointProvider weaponPool;
    private final ITransactionManager transactionManager = new TransactionManager();
    private final Map<GunItem, GunKillData> weaponStats = new HashMap<>();
    private final IProgressionStrategy strategy;
    private ISyncRequestDispatcher request = () -> {};
    private boolean known;
    private int level;
    private int skillPoints;
    private int kills;
    private int requiredKills;

    public PlayerProgressionData(PlayerEntity player, ISkillProvider skillProvider, IPointProvider weaponPool) {
        this.player = player;
        this.skillProvider = skillProvider;
        this.weaponPool = weaponPool;
        this.transactionManager.registerHandler(TransactionTypes.SKILLPOINT_TRANSACTION, this::hasEnoughSkillpoints, this::handleSkillpointTransaction);
        this.transactionManager.registerHandler(TransactionTypes.WEAPON_POINT_TRANSACTION, this::hasEnoughWeaponPoints, this::handleWeaponPointTransaction);
        ITransactionValidatorFactory<?, ?> factory = TransactionValidatorRegistry.getValidatorFactory(PlayerLevelTransactionValidator.ID);
        ITransactionValidator validator = TransactionValidatorRegistry.getTransactionValidator(factory, null);
        this.strategy = ModUtils.firstNonnull(GunsRPG.getModLifecycle().getProgressionStrategyManager().getStrategy(validator), FakeLevelingStrategy.INSTANCE);
        this.requiredKills = this.strategy.getRequiredKillCount(this.level);
    }

    @Override
    public IKillData getWeaponStats(GunItem item) {
        return weaponStats.computeIfAbsent(item, k -> new GunKillData(player, item, this.weaponPool));
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
            if (!player.isCreative())
                ModUtils.addItem(player, new ItemStack(ModItems.STARTER_GEAR));
            request.sendSyncRequest();
        }
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
    public int getLevelLimit() {
        return 100;
    }

    @Override
    public void advanceLevel(boolean notify) {
        this.level = Math.min(level + 1, getLevelLimit());
        this.kills = 0;
        strategy.applyRewards(player, this, level);
        updateRequiredKills();

        if (notify) {
            player.sendMessage(new StringTextComponent(TextFormatting.YELLOW + "=====[ " + new TranslationTextComponent("text.player.level_up").getString() + " ]====="), Util.NIL_UUID);
            player.sendMessage(new TranslationTextComponent("text.player.current_level", level).withStyle(TextFormatting.YELLOW), Util.NIL_UUID);
            skillProvider.onLevelUp(level, player);
            request.sendSyncRequest();
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
        if (!(enemy instanceof IMob) || enemy instanceof SlimeEntity)
            return;
        ++kills;
        if (level < getLevelLimit() && requiredKills <= kills) {
            advanceLevel(true);
        }
        if (weapon.getItem() instanceof GunItem) {
            GunItem gun = (GunItem) weapon.getItem();
            getWeaponStats(gun).onEnemyKilled(enemy, weapon);
        }
        request.sendSyncRequest();
    }

    @Override
    public void doUnlock() {
        level = getLevelLimit();
        skillPoints = Integer.MAX_VALUE;
        ForgeRegistries.ITEMS.getValues().stream()
                .filter(item -> item instanceof GunItem)
                .map(item -> (GunItem) item)
                .forEach(gun -> {
                    GunKillData killData = weaponStats.computeIfAbsent(gun, k -> new GunKillData(player, gun, this.weaponPool));
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
                GunKillData data = new GunKillData(player, gun, this.weaponPool);
                CompoundNBT dataNbt = killData.getCompound(key);
                data.deserializeNBT(dataNbt);
                weaponStats.put(gun, data);
            }
        }
    }

    @Override
    public void setSyncRequestTemplate(ISyncRequestDispatcher request) {
        this.request = request;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void draw(MatrixStack matrix, FontRenderer font, IVec2i position, int xOffset, int yOffset, int width, int height, PlayerEntity player, IPlayerData data) {
        ProgressionRenderer.renderProgressionBar(matrix, this, font, position.x() + xOffset, position.y() + yOffset, width, height, OverlayPlacement.VerticalAlignment.BOTTOM, 0xFF00FFFF, 0xFF008888);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public int getWidth(FontRenderer font, PlayerEntity player, IPlayerData data) {
        return ProgressionRenderer.getProgressBarWidth(font, this);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public int getHeight(FontRenderer font, PlayerEntity player, IPlayerData data) {
        return ProgressionRenderer.getProgressBarHeight();
    }

    private void updateRequiredKills() {
        requiredKills = strategy.getRequiredKillCount(level);
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
