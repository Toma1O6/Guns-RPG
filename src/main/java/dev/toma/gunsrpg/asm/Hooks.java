package dev.toma.gunsrpg.asm;

import dev.toma.gunsrpg.event.EntityEquippedItemEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class Hooks {

    @Deprecated
    public static void dispatchApplyAttributesFromItemStack(LivingEntity livingBase, ItemStack stack, EquipmentSlotType slot) {
        MinecraftForge.EVENT_BUS.post(new EntityEquippedItemEvent(livingBase, stack, slot));
    }

    public static double modifyAttackDelay(PlayerEntity player) {
        double value = player.getAttributeValue(Attributes.ATTACK_SPEED);
        return value;
    }
}
