package dev.toma.gunsrpg.asm;

import dev.toma.gunsrpg.event.EntityEquippedItemEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class Hooks {

    public static void dispatchApplyAttributesFromItemStack(EntityLivingBase livingBase, ItemStack stack, EntityEquipmentSlot slot) {
        MinecraftForge.EVENT_BUS.post(new EntityEquippedItemEvent(livingBase, stack, slot));
    }
}
