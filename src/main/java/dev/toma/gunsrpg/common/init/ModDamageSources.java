package dev.toma.gunsrpg.common.init;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public final class ModDamageSources {

    public static final DamageSource SPIKE_DAMAGE = new DamageSource("spikes").bypassArmor();

    public static DamageSource dealWeaponDamage(Entity source, Entity indirectSource, ItemStack stack) {
        return new GunDamageSource(source, indirectSource, stack);
    }

    public static DamageSource dealSpecialWeaponDamage(Entity source, Entity indirectSource, ItemStack stack) {
        return new GunDamageSourceSpecial(source, indirectSource, stack);
    }

    private ModDamageSources() {}
}
