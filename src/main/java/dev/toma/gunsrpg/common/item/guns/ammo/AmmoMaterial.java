package dev.toma.gunsrpg.common.item.guns.ammo;

import net.minecraft.util.text.TextFormatting;

public enum AmmoMaterial {

    WOOD(TextFormatting.GOLD),
    STONE(TextFormatting.DARK_GRAY),
    IRON(TextFormatting.GRAY),
    GOLD(TextFormatting.YELLOW),
    DIAMOND(TextFormatting.AQUA),
    EMERALD(TextFormatting.GREEN),
    AMETHYST(TextFormatting.DARK_PURPLE);

    private final TextFormatting color;

    AmmoMaterial(TextFormatting color) {
        this.color = color;
    }

    public TextFormatting getColor() {
        return color;
    }
}
