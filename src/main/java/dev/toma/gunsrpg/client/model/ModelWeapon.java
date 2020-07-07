package dev.toma.gunsrpg.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.item.ItemStack;

public abstract class ModelWeapon extends ModelBase {

    public abstract void doRender(ItemStack stack);
}
