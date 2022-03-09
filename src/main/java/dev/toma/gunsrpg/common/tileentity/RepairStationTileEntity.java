package dev.toma.gunsrpg.common.tileentity;

import dev.toma.gunsrpg.api.common.attribute.IAttributeProvider;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.init.ModBlockEntities;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.util.locate.SlotInventoryIterator;
import dev.toma.gunsrpg.util.locate.ammo.ItemLocator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
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
        weapon.setDamageValue(itemDurability);
        ItemStack stack = getRepairKit();
        stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
        // TODO play sound
    }

    public boolean canRepair() {
        return isWeaponRepairable(itemHandler.getStackInSlot(SLOT_INPUT)) && !getRepairKit().isEmpty();
    }

    private boolean isWeaponRepairable(ItemStack stack) {
        return false;
    }

    private ItemStack getRepairKit() {
        return new ItemLocator<>().locateFirst(itemHandler, new SlotInventoryIterator<>(STORAGE, itemHandler, IItemHandler::getStackInSlot), ItemLocator.notEmptyNorDestroyed());
    }
}
