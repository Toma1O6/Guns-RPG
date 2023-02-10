package dev.toma.gunsrpg.common.container;

import dev.toma.gunsrpg.common.entity.TurretEntity;
import dev.toma.gunsrpg.common.init.ModContainers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class TurretContainer extends AbstractContainer {

    private final int entityId;
    private final IInventory inventory;

    public TurretContainer(ContainerType<? extends TurretContainer> type, int containerId, PlayerInventory playerInventory, int entityId, IInventory inventory) {
        super(type, containerId);
        this.entityId = entityId;
        this.inventory = inventory;

        Predicate<ItemStack> filter = this.getInputFilter(playerInventory.player.level);
        addSlots(inventory, 2, 4, 8, 8, 0, (inv, index, x, y) -> new AmmoSlot(inv, index, x, y, filter));
        addPlayerInventory(playerInventory, 97);
    }

    private TurretContainer(int containerId, PlayerInventory inventory, int entityId) {
        this(ModContainers.TURRET_CONTAINER.get(), containerId, inventory, entityId, resolveInventory(inventory.player.level, entityId));
    }

    public TurretContainer(int containerId, PlayerInventory inventory, PacketBuffer buffer) {
        this(containerId, inventory, buffer.readInt());
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = getSlot(index);
        int size = inventory.getContainerSize();
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            stack = slotStack.copy();
            if (index < size) {
                if (!moveItemStackTo(slotStack, size, slots.size(), true))
                    return ItemStack.EMPTY;
            } else if (!moveItemStackTo(slotStack, 0, size, false))
                return ItemStack.EMPTY;
            if (slotStack.isEmpty())
                slot.set(ItemStack.EMPTY);
            else
                slot.setChanged();
        }
        return stack;
    }

    public int getEntityId() {
        return entityId;
    }

    private Predicate<ItemStack> getInputFilter(World world) {
        Entity entity = world.getEntity(entityId);
        if (!(entity instanceof TurretEntity)) {
            return t -> false;
        }
        return ((TurretEntity) entity).getAmmoFilter();
    }

    private static IInventory resolveInventory(World world, int entityId) {
        Entity entity = world.getEntity(entityId);
        if (!(entity instanceof TurretEntity)) {
            return new Inventory(TurretEntity.INVENTORY_SIZE);
        }
        return ((TurretEntity) entity).getInventory();
    }

    private static final class AmmoSlot extends Slot {

        private final Predicate<ItemStack> filter;

        public AmmoSlot(IInventory inventory, int slotIndex, int xPos, int yPos, Predicate<ItemStack> filter) {
            super(inventory, slotIndex, xPos, yPos);
            this.filter = filter;
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return this.filter.test(stack);
        }
    }
}
