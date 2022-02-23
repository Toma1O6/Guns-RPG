package dev.toma.gunsrpg.util.helper;

import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;

public final class PlayerHelper {

    public static boolean hasFullArmorSetOfMaterial(LivingEntity entity, IArmorMaterial material) {
        return  hasArmorMaterialOn(entity, EquipmentSlotType.HEAD, material) &&
                hasArmorMaterialOn(entity, EquipmentSlotType.CHEST, material) &&
                hasArmorMaterialOn(entity, EquipmentSlotType.LEGS, material) &&
                hasArmorMaterialOn(entity, EquipmentSlotType.FEET, material);
    }

    public static boolean hasArmorMaterialOn(LivingEntity entity, EquipmentSlotType slotType, IArmorMaterial material) {
        ItemStack stack = entity.getItemBySlot(slotType);
        return stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getMaterial() == material;
    }

    private PlayerHelper() {}
}
