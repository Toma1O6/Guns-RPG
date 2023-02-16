package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModBlockEntities;
import dev.toma.gunsrpg.common.init.ModSounds;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.util.locate.SlotInventoryIterator;
import dev.toma.gunsrpg.util.locate.ammo.ItemLocator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class RepairStationTileEntity extends InventoryTileEntity {

    public static final int SLOT_INPUT = 0;
    public static final int[] STORAGE = {1,2,3};

    protected RepairStationTileEntity(TileEntityType<? extends RepairStationTileEntity> type) {
        super(type);
    }

    public RepairStationTileEntity() {
        this(ModBlockEntities.REPAIR_STATION.get());
    }

    @Override
    public IItemHandlerModifiable createInventory() {
        return new ItemStackHandler(5);
    }

    public void repair(PlayerEntity player) {
        ItemStack weapon = itemHandler.getStackInSlot(SLOT_INPUT);
        GunItem item = (GunItem) weapon.getItem();
        float durability = item.getDurabilityLimit(weapon);
        IAttributeProvider provider = PlayerData.getUnsafe(player).getAttributes();
        float durabilityMultiplier = provider.getAttribute(Attribs.REPAIR_PENALTY).floatValue();
        float newDurability = durabilityMultiplier * durability;
        item.setDurabilityLimit(weapon, newDurability);
        int itemDurability = (int) (weapon.getMaxDamage() * newDurability);
        weapon.setDamageValue(weapon.getMaxDamage() - itemDurability);
        ItemStack stack = getRepairKit();
        int repairKitDmg = stack.getDamageValue();
        stack.setDamageValue(repairKitDmg + 1);
        if (1 + repairKitDmg == stack.getMaxDamage()) {
            stack.shrink(1);
        }
        player.level.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.REPAIR, SoundCategory.MASTER, 1.0F, 1.0F);
    }

    public boolean canRepair() {
        ItemStack repairKit = this.getRepairKit();
        return isWeaponRepairable(itemHandler.getStackInSlot(SLOT_INPUT)) && !repairKit.isEmpty();
    }

    private boolean isWeaponRepairable(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        GunItem item = (GunItem) stack.getItem();
        float repairDurability = item.getDurabilityLimit(stack);
        int damage = stack.getDamageValue();
        int limit = (int) (stack.getMaxDamage() * repairDurability);
        return stack.getMaxDamage() - damage < limit && repairDurability > 0.1F;
    }

    private ItemStack getRepairKit() {
        return ItemLocator.INSTANCE.findFirst(itemHandler, new SlotInventoryIterator<>(STORAGE, IItemHandler::getStackInSlot), ItemLocator.notDestroyedItem());
    }
}
