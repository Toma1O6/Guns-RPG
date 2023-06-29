package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.api.common.data.IPerkProvider;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModBlockEntities;
import dev.toma.gunsrpg.common.item.perk.*;
import dev.toma.gunsrpg.common.perk.PerkType;
import dev.toma.gunsrpg.resource.perks.PerkConfiguration;
import dev.toma.gunsrpg.resource.perks.PurificationConfiguration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CrystalPurificationStationTileEntity extends InventoryTileEntity {

    public static final int OUT = 0;
    public static final int IN = 1;
    public static final int[] ORB = {2, 3, 4};

    protected CrystalPurificationStationTileEntity(TileEntityType<? extends CrystalPurificationStationTileEntity> type) {
        super(type);
    }

    public CrystalPurificationStationTileEntity() {
        this(ModBlockEntities.CRYSTAL_PURIFICATION.get());
    }

    @Override
    public IItemHandlerModifiable createInventory() {
        return new ItemStackHandler(5);
    }

    public void purify(PlayerEntity player) {
        IPlayerData data = PlayerData.getUnsafe(player);
        IPerkProvider provider = data.getPerkProvider();
        PerkConfiguration perkConfig = GunsRPG.getModLifecycle().getPerkManager().configLoader.getConfiguration();
        PurificationConfiguration puficationConfig = perkConfig.getPurificationConfig();
        int orbCount = getOrbCount();
        PurificationConfiguration.Entry entry = puficationConfig.getValueForAmount(orbCount);
        int price = entry.getPrice();
        provider.awardPoints(-price);
        Random random = player.getRandom();
        float breakChance = entry.getBreakChance();
        if (random.nextFloat() < breakChance) {
            clearInputs();
            return;
        }
        ItemStack input = itemHandler.getStackInSlot(IN);
        Crystal crystal = CrystalItem.getCrystal(input);
        Map<PerkType, List<CrystalAttribute>> attributeMap = crystal.groupByType();
        float debuffChance = entry.getSuccessChance();
        PerkType type = random.nextFloat() < debuffChance ? PerkType.DEBUFF : PerkType.BUFF;
        List<CrystalAttribute> list = attributeMap.get(type);
        if (!list.isEmpty()) {
            int randomAttributeIndex = random.nextInt(list.size());
            list.remove(randomAttributeIndex);
        }
        Crystal newCrystal = Crystal.mergeAttributes(crystal.getLevel(), attributeMap);
        ItemStack result = new ItemStack(input.getItem());
        CrystalItem.addCrystal(result, newCrystal);
        clearInputs();
        itemHandler.setStackInSlot(OUT, result);
    }

    public boolean canPurify(IPerkProvider provider) {
        int orbs = this.getOrbCount();
        PerkConfiguration perkConfig = GunsRPG.getModLifecycle().getPerkManager().configLoader.getConfiguration();
        PurificationConfiguration config = perkConfig.getPurificationConfig();
        PurificationConfiguration.Entry entry = config.getValueForAmount(orbs);
        if (entry == null) return false;
        int price = entry.getPrice();
        if (!itemHandler.getStackInSlot(OUT).isEmpty()) return false;
        Crystal crystal = CrystalItem.getCrystal(itemHandler.getStackInSlot(IN));
        if (crystal == null || !crystal.hasAnyAttributes()) return false;
        return price <= provider.getPoints();
    }

    public int getPrice() {
        return this.getPrice(this.getOrbCount());
    }

    public int getPrice(int orbCount) {
        PerkConfiguration perkConfig = GunsRPG.getModLifecycle().getPerkManager().configLoader.getConfiguration();
        PurificationConfiguration puficationConfig = perkConfig.getPurificationConfig();
        PurificationConfiguration.Entry entry = puficationConfig.getValueForAmount(orbCount);
        return itemHandler.getStackInSlot(IN).isEmpty() || entry == null ? -1 : entry.getPrice();
    }

    public int getOrbCount() {
        return (int) Arrays.stream(ORB).mapToObj(value -> itemHandler.getStackInSlot(value)).filter(stack -> !stack.isEmpty()).count();
    }

    public PerkVariant getTargetedPerkVariant() {
        PerkVariant input = this.getItemVariant(itemHandler.getStackInSlot(IN));
        if (input != null) {
            return input;
        }
        for (int slot : ORB) {
            PerkVariant variant = this.getItemVariant(itemHandler.getStackInSlot(slot));
            if (variant != null) {
                return variant;
            }
        }
        return null;
    }

    public PerkVariant getItemVariant(ItemStack stack) {
        return stack.isEmpty() ? null : ((PerkItem) stack.getItem()).getVariant();
    }

    private void clearInputs() {
        itemHandler.setStackInSlot(IN, ItemStack.EMPTY);
        for (int slot : ORB) {
            ItemStack stack = itemHandler.getStackInSlot(slot);
            if (!stack.isEmpty()) {
                stack.shrink(1);
            }
        }
    }
}
