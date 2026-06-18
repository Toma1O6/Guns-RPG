package com.wf.firearms.gameplay;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.combat.FirearmSpec;
import com.wf.firearms.combat.GunHitEffectCoalescer;
import com.wf.firearms.combat.GunHitEffects;
import com.wf.firearms.combat.ModDamageTypes;
import com.wf.firearms.compat.TaczCompat;
import com.wf.firearms.compat.tacz.TaczBridge;
import com.wf.firearms.config.TaczBackendConfig;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** 自研弹道命中附加效果（TaCZ 弹道由 TaCZ 侧处理）。 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class GunHitEffectHandler {
    private GunHitEffectHandler() {}

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide || event.getAmount() <= 0.01f) {
            return;
        }
        DamageSource source = event.getSource();
        Player shooter = resolveShooter(source);
        if (shooter == null) {
            return;
        }
        if (shouldSkipHitEffects(source, shooter)) {
            return;
        }
        if (!ModDamageTypes.isBullet(source)) {
            return;
        }
        if (!GunHitEffectCoalescer.shouldApply(event.getEntity(), shooter)) {
            return;
        }
        String weaponKey = GunKillHandler.heldFirearmKey(shooter);
        if (weaponKey == null) {
            return;
        }
        FirearmSpec spec = FirearmRegistry.getOrDefault(weaponKey);
        GunHitEffects.applyOnHit(event.getEntity(), shooter, weaponKey, spec, event.getAmount());
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        if (event.getEntity().level().isClientSide) {
            return;
        }
        GunHitEffects.tickArmorBreak(event.getEntity());
    }

    private static boolean shouldSkipHitEffects(DamageSource source, Player shooter) {
        if (!TaczBackendConfig.useTaczShooting()) {
            return false;
        }
        if (TaczCompat.isTaczBulletDamage(source)) {
            return true;
        }
        ItemStack main = shooter.getMainHandItem();
        ItemStack off = shooter.getOffhandItem();
        return TaczBridge.isTaczGun(main) || TaczBridge.isTaczGun(off);
    }

    private static Player resolveShooter(DamageSource source) {
        if (source.getEntity() instanceof Player p) {
            return p;
        }
        Entity direct = source.getDirectEntity();
        if (direct instanceof Projectile projectile && projectile.getOwner() instanceof Player p) {
            return p;
        }
        return null;
    }
}
