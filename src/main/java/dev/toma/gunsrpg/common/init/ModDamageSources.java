package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public final class ModDamageSources {

    public static final DamageSource SPIKE_DAMAGE = new SimpleDamageSource("spikes").bypassArmor();
    public static final DamageSource POISON_DAMAGE = new SimpleDamageSource("poison").bypassArmor();
    public static final DamageSource INFECTION_DAMAGE = new SimpleDamageSource("infection").bypassArmor();
    public static final DamageSource BLEED_DAMAGE = new SimpleDamageSource("bleeding").bypassArmor();
    public static final DamageSource FRACTURE_DAMAGE = new SimpleDamageSource("fracture").bypassArmor();

    public static DamageSource gunAttack(Entity owner, AbstractProjectile projectile, ItemStack gun) {
        return new WeaponDamageSource(owner, projectile, gun);
    }

    private ModDamageSources() {}

    private static class SimpleDamageSource extends DamageSource {

        private SimpleDamageSource(String messageId) {
            super(messageId);
        }

        @Override
        public ITextComponent getLocalizedDeathMessage(LivingEntity victim) {
            String messageId = this.getMsgId();
            String localizationText = "death.attack." + messageId;
            return new TranslationTextComponent(localizationText, victim.getDisplayName());
        }
    }
}
