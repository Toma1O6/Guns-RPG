package dev.toma.gunsrpg.world;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;

public class WeaponExplosiveDamageSource extends WeaponDamageSource {

    public WeaponExplosiveDamageSource(Entity directSource, ProjectileEntity indirectSource, ItemStack weaponStack) {
        super(directSource, indirectSource, weaponStack);
    }

    @Override
    public boolean isExplosion() {
        return true;
    }
}
