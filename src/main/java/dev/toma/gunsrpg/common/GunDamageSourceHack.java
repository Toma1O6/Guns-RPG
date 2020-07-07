package dev.toma.gunsrpg.common;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class GunDamageSourceHack extends GunDamageSource {

    public GunDamageSourceHack(Entity src, Entity indirect, ItemStack stacc) {
        super(src, indirect, stacc);
    }

    @Nullable
    @Override
    public Entity getTrueSource() {
        return null;
    }
}
