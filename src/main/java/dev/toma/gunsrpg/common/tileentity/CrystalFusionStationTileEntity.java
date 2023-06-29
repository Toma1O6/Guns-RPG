package dev.toma.gunsrpg.common.tileentity;

import com.google.common.collect.ImmutableList;
import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IPerkProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.container.CrystalStationContainer;
import dev.toma.gunsrpg.common.init.ModBlockEntities;
import dev.toma.gunsrpg.common.item.perk.Crystal;
import dev.toma.gunsrpg.common.item.perk.CrystalItem;
import dev.toma.gunsrpg.common.item.perk.PerkItem;
import dev.toma.gunsrpg.common.item.perk.PerkVariant;
import dev.toma.gunsrpg.resource.perks.FusionConfiguration;
import dev.toma.gunsrpg.resource.perks.PerkConfiguration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class CrystalFusionStationTileEntity extends InventoryTileEntity {

    public static final int OUTPUT = 0;
    public static final int[] INPUTS = {1, 2};
    public static final int[] ORBS = {3, 4, 5};

    protected CrystalFusionStationTileEntity(TileEntityType<CrystalFusionStationTileEntity> type) {
        super(type);
    }

    public CrystalFusionStationTileEntity() {
        this(ModBlockEntities.CRYSTAL_FUSION.get());
    }

    @Override
    public IItemHandlerModifiable createInventory() {
        return new ItemStackHandler(6);
    }

    public void fuse(PlayerEntity user) {
        IPlayerData data = PlayerData.getUnsafe(user);
        PerkConfiguration perkConfig = this.getPerkConfig();
        FusionConfiguration fusionConfig = perkConfig.getFusionConfig();
        int orbCount = (int) Arrays.stream(ORBS).mapToObj(num -> itemHandler.getStackInSlot(num)).filter(stack -> !stack.isEmpty()).count();
        FusionConfiguration.Swap swapConfig = fusionConfig.getSwaps().getSwapStat(orbCount);
        Crystal crystal1 = CrystalItem.getCrystal(itemHandler.getStackInSlot(INPUTS[0]));
        Crystal crystal2 = CrystalItem.getCrystal(itemHandler.getStackInSlot(INPUTS[1]));
        int targetLevel = crystal1.getLevel() + crystal2.getLevel();
        FusionConfiguration.Upgrade upgrade = fusionConfig.getUpgrades().getUpgradeStat(targetLevel);
        FusionConfiguration.BreakChanceReductions reductions = fusionConfig.getBreakChanceReductions();
        int price = upgrade.getPrice() + swapConfig.getPrice();
        float breakChance = upgrade.getBreakChance();
        data.getPerkProvider().awardPoints(-price);
        Random random = level.random;
        EnumSet<PerkVariant> usedVariants = this.getCurrentlyUsedVariants();
        if (usedVariants.size() == 1) { // when same only one color is used
            FusionConfiguration.BreakChanceReduction reduction = reductions.getReductionForOrbs(orbCount);
            if (reduction != null) {
                breakChance *= reduction.getMultiplier();
            }
        }
        float randomChance = random.nextFloat();
        if (randomChance < breakChance) {
            clearInputs();
            return;
        }
        List<PerkVariant> variants = ImmutableList.copyOf(usedVariants);
        PerkVariant targetVariant = this.getTargetedOrbType();
        PerkVariant other;
        if (targetVariant == null) {
            targetVariant = variants.get(0);
        }
        if (variants.size() != 1) {
            other = variants.get(1);
        } else {
            other = targetVariant;
        }
        targetVariant = random.nextFloat() < swapConfig.getChance() ? targetVariant : other;
        Crystal crystal = Crystal.mergeAndLevelUp(crystal1, crystal2, targetLevel);
        CrystalItem item = CrystalStationContainer.mapCrystalVariants().get(targetVariant);
        ItemStack output = new ItemStack(item);
        CrystalItem.addCrystal(output, crystal);
        itemHandler.setStackInSlot(OUTPUT, output);
        clearInputs();
    }

    public boolean canFuse(PlayerEntity player) {
        boolean emptyOutput = itemHandler.getStackInSlot(OUTPUT).isEmpty();
        return emptyOutput && canFuseInputs() && checkFunds(player);
    }

    public boolean canAddItem(PerkVariant variant) {
        EnumSet<PerkVariant> set = this.getCrystalVariants();
        PerkVariant orbTarget = this.getTargetedOrbType();
        ItemStack item1 = itemHandler.getStackInSlot(INPUTS[0]);
        ItemStack item2 = itemHandler.getStackInSlot(INPUTS[1]);
        if (orbTarget != null) {
            return variant == orbTarget || set.contains(orbTarget);
        } else if (!item1.isEmpty() && !item2.isEmpty()) {
            PerkVariant variant1 = this.getItemVariant(item1);
            PerkVariant variant2 = this.getItemVariant(item2);
            if (variant1 == variant2 && variant != variant1) {
                return false;
            }
        }
        return set.size() < 2 || set.contains(variant);
    }

    public PerkVariant getItemVariant(ItemStack stack) {
        return stack.isEmpty() ? null : ((PerkItem) stack.getItem()).getVariant();
    }

    public EnumSet<PerkVariant> getCrystalVariants() {
        EnumSet<PerkVariant> result = EnumSet.noneOf(PerkVariant.class);
        for (int slot : INPUTS) {
            ItemStack stack = itemHandler.getStackInSlot(slot);
            PerkVariant variant = getItemVariant(stack);
            if (variant != null) {
                result.add(variant);
            }
        }
        return result;
    }

    public EnumSet<PerkVariant> getCurrentlyUsedVariants() {
        EnumSet<PerkVariant> result = EnumSet.noneOf(PerkVariant.class);
        for (int slot : INPUTS) {
            ItemStack stack = itemHandler.getStackInSlot(slot);
            PerkVariant variant = getItemVariant(stack);
            if (variant != null) {
                result.add(variant);
            }
        }
        PerkVariant variant = this.getTargetedOrbType();
        if (variant != null) {
            result.add(variant);
        }
        return result;
    }

    private boolean checkFunds(PlayerEntity player) {
        IPlayerData data = PlayerData.getUnsafe(player);
        IPerkProvider provider = data.getPerkProvider();
        PerkConfiguration perkConfig = this.getPerkConfig();
        FusionConfiguration fusionConfig = perkConfig.getFusionConfig();
        int orbCount = (int) Arrays.stream(ORBS).mapToObj(num -> itemHandler.getStackInSlot(num)).filter(stack -> !stack.isEmpty()).count();
        FusionConfiguration.Swap swapConfig = fusionConfig.getSwaps().getSwapStat(orbCount);
        Crystal crystal1 = CrystalItem.getCrystal(itemHandler.getStackInSlot(INPUTS[0]));
        Crystal crystal2 = CrystalItem.getCrystal(itemHandler.getStackInSlot(INPUTS[1]));
        int targetLevel = crystal1.getLevel() + crystal2.getLevel();
        FusionConfiguration.Upgrade upgrade = fusionConfig.getUpgrades().getUpgradeStat(targetLevel);
        int price = upgrade.getPrice() + swapConfig.getPrice();
        return provider.getPoints() >= price;
    }

    private boolean canFuseInputs() {
        ItemStack stack1 = itemHandler.getStackInSlot(INPUTS[0]);
        ItemStack stack2 = itemHandler.getStackInSlot(INPUTS[1]);
        if (stack1.isEmpty() || stack2.isEmpty()) {
            return false;
        }
        Crystal crystal1 = CrystalItem.getCrystal(stack1);
        Crystal crystal2 = CrystalItem.getCrystal(stack2);
        if (crystal1 == null || crystal2 == null) {
            return false;
        }
        int target = crystal1.getLevel() + crystal2.getLevel();
        FusionConfiguration.Upgrade upgrade = this.getPerkConfig().getFusionConfig().getUpgrades().getUpgradeStat(target);
        return upgrade != null;
    }

    private PerkConfiguration getPerkConfig() {
        return GunsRPG.getModLifecycle().getPerkManager().configLoader.getConfiguration();
    }

    public PerkVariant getTargetedOrbType() {
        for (int slot : ORBS) {
            ItemStack stack = itemHandler.getStackInSlot(slot);
            if (!stack.isEmpty()) {
                return this.getItemVariant(stack);
            }
        }
        return null;
    }

    private void clearInputs() {
        for (int slot : INPUTS) {
            itemHandler.setStackInSlot(slot, ItemStack.EMPTY);
        }
        for (int slot : ORBS) {
            ItemStack stack = itemHandler.getStackInSlot(slot);
            if (!stack.isEmpty()) {
                stack.shrink(1);
            }
        }
    }
}
