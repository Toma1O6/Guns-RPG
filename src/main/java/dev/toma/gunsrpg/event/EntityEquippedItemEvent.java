package dev.toma.gunsrpg.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EntityEquippedItemEvent extends Event {

    private final EntityLivingBase entityLivingBase;
    private final ItemStack stack;
    private final EntityEquipmentSlot slot;

    public EntityEquippedItemEvent(EntityLivingBase entityLivingBase, ItemStack stack, EntityEquipmentSlot slot) {
        this.entityLivingBase = entityLivingBase;
        this.stack = stack;
        this.slot = slot;
    }

    public EntityLivingBase getEntity() {
        return entityLivingBase;
    }

    public ItemStack getStack() {
        return stack;
    }

    public EntityEquipmentSlot getSlot() {
        return slot;
    }
}
