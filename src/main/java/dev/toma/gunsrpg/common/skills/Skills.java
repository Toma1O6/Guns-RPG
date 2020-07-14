package dev.toma.gunsrpg.common.skills;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemPickaxe;

public class Skills {

    public static float getAxeSpeedModifier(float input, ItemAxe axe, EntityPlayer player) {
        return input;
    }

    public static float getPickaxeSpeedModifier(float input, ItemPickaxe pickaxe, EntityPlayer player) {
        return input;
    }
}
