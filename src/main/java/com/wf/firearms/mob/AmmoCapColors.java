package com.wf.firearms.mob;

import com.wf.firearms.combat.AmmoMaterial;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/** 原版 Guns RPG：僵尸枪手皮帽颜色对应弹药材质（绿=绿宝石等）。 */
public final class AmmoCapColors {
    private AmmoCapColors() {}

    public static int rgbFor(AmmoMaterial material) {
        if (material == null || material == AmmoMaterial.UNKNOWN) {
            return 0x5A5A5A;
        }
        return switch (material) {
            case WOOD -> 0x6B4423;
            case STONE, COPPER -> 0x8A8A8A;
            case IRON, STEEL, INVAR, BRONZE, BRASS -> 0xB8B8B8;
            case LAPIS -> 0x2244CC;
            case GOLD -> 0xE8C040;
            case REDSTONE -> 0xCC2222;
            case EMERALD -> 0x2ECC40;
            case QUARTZ -> 0xF0E8E0;
            case DIAMOND -> 0x40E8E8;
            case NETHERITE -> 0x3A3232;
            case NEPTUNIUM -> 0x2A6B8A;
            case STARINIUM, STELLAR, STARLIGHT_MYTHRIL -> 0xE8D878;
            case VOID, DARK_CRYOPLA -> 0x281830;
            case COSMOS_AURORA, ABIDING_ALLOY, ULTIMATE -> 0xA060E0;
            default -> 0x707070;
        };
    }

    /** 略深一阶的上衣染色，区分于帽色。 */
    public static int clothingRgbFor(AmmoMaterial material) {
        int base = rgbFor(material);
        int r = (base >> 16) & 0xFF;
        int g = (base >> 8) & 0xFF;
        int b = base & 0xFF;
        r = Math.max(0, (int) (r * 0.55f));
        g = Math.max(0, (int) (g * 0.55f));
        b = Math.max(0, (int) (b * 0.55f));
        return (r << 16) | (g << 8) | b;
    }

    public static ItemStack leatherCap(AmmoMaterial material) {
        return dyeLeather(new ItemStack(Items.LEATHER_HELMET), rgbFor(material));
    }

    public static ItemStack leatherShirt(AmmoMaterial material) {
        return dyeLeather(new ItemStack(Items.LEATHER_CHESTPLATE), clothingRgbFor(material));
    }

    private static ItemStack dyeLeather(ItemStack stack, int rgb) {
        CompoundTag display = stack.getOrCreateTagElement("display");
        display.putInt("color", rgb);
        return stack;
    }
}
