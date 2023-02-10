package dev.toma.gunsrpg.common.init;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class WeaponDamageSource extends DamageSource {

    private final Entity directSource;
    private final ProjectileEntity indirectSource;
    private final ItemStack weaponStack;

    public WeaponDamageSource(Entity directSource, ProjectileEntity indirectSource, ItemStack weaponStack) {
        super("gun");
        this.directSource = directSource;
        this.indirectSource = indirectSource;
        this.weaponStack = weaponStack;
    }

    @Nullable
    @Override
    public Entity getEntity() {
        return directSource;
    }

    @Nullable
    @Override
    public Entity getDirectEntity() {
        return indirectSource;
    }

    public ItemStack getKillWeapon() {
        return weaponStack;
    }

    @Override
    public ITextComponent getLocalizedDeathMessage(LivingEntity victim) {
        String base = "death.attack." + this.msgId;
        String playerBase = base + ".player";
        String itemBase = playerBase + ".weapon";
        if (this.directSource != null) {
            return this.weaponStack.isEmpty() ?
                    new TranslationTextComponent(base, victim.getDisplayName(), this.directSource.getDisplayName()) :
                    new TranslationTextComponent(itemBase, victim.getDisplayName(), this.directSource.getDisplayName(), this.weaponStack.getDisplayName());
        } else {
            return new TranslationTextComponent(base, victim.getDisplayName());
        }
    }
}
