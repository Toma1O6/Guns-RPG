package dev.toma.gunsrpg.event;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

@Deprecated
public class EntityEquippedItemEvent extends Event {

    private final LivingEntity livingEntity;
    private final ItemStack stack;
    private final EquipmentSlotType slot;

    public EntityEquippedItemEvent(LivingEntity livingEntity, ItemStack stack, EquipmentSlotType slot) {
        this.livingEntity = livingEntity;
        this.stack = stack;
        this.slot = slot;
    }

    public LivingEntity getEntity() {
        return livingEntity;
    }

    public ItemStack getStack() {
        return stack;
    }

    public EquipmentSlotType getSlot() {
        return slot;
    }
}
