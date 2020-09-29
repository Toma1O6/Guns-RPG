package dev.toma.gunsrpg.common;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class GRPGPotion extends Potion {

    public GRPGPotion(String name, boolean isBad, int color) {
        super(isBad, color);
        setRegistryName(name);
    }

    @Override
    public boolean shouldRender(PotionEffect effect) {
        return false;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }
}
