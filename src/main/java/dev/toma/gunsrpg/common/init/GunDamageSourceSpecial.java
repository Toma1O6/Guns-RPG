package dev.toma.gunsrpg.common.init;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class GunDamageSourceSpecial extends GunDamageSource {

    protected GunDamageSourceSpecial(Entity src, Entity indirect, ItemStack stacc) {
        super(src, indirect, stacc);
    }

    @Nullable
    @Override
    public Entity getEntity() {
        return null;
    }
}
