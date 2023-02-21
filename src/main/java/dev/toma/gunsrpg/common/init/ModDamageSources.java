package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.common.entity.projectile.AbstractProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public final class ModDamageSources {

    public static final DamageSource POISON_DAMAGE = new SimpleDamageSource("poison").bypassArmor();
    public static final DamageSource INFECTION_DAMAGE = new SimpleDamageSource("infection").bypassArmor();
    public static final DamageSource BLEED_DAMAGE = new SimpleDamageSource("bleeding").bypassArmor();
    public static final DamageSource FRACTURE_DAMAGE = new SimpleDamageSource("fracture").bypassArmor();

    public static DamageSource gunAttack(Entity owner, AbstractProjectile projectile, ItemStack gun) {
        return new WeaponDamageSource(owner, projectile, gun);
    }

    public static DamageSource spikes(Entity owner) {
        return new SpikeDamageSource(owner);
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

    private static class SpikeDamageSource extends EntityDamageSource {

        public SpikeDamageSource(@Nullable Entity owner) {
            super("spikes", owner);
            this.bypassArmor();
        }

        @Override
        public ITextComponent getLocalizedDeathMessage(LivingEntity victim) {
            String key = "death.attack." + this.msgId;
            if (entity != null) {
                key += ".owner";
                return new TranslationTextComponent(key, victim.getDisplayName(), entity.getDisplayName());
            } else {
                return new TranslationTextComponent(key, victim.getDisplayName());
            }
        }
    }
}
