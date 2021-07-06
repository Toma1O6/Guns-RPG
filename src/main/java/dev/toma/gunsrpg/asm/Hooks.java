package dev.toma.gunsrpg.asm;

import dev.toma.gunsrpg.event.EntityEquippedItemEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class Hooks {

    public static void dispatchApplyAttributesFromItemStack(LivingEntity livingBase, ItemStack stack, EquipmentSlotType slot) {
        MinecraftForge.EVENT_BUS.post(new EntityEquippedItemEvent(livingBase, stack, slot));
    }
}
