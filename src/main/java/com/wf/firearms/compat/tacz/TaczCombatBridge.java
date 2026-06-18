package com.wf.firearms.compat.tacz;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.compat.TaczCompat;
import com.wf.firearms.combat.FirearmDamage;
import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.combat.FirearmSpec;
import com.wf.firearms.combat.FirearmStackState;
import com.wf.firearms.combat.LauncherShooter;
import com.wf.firearms.gameplay.WeaponExtensionService;
import com.wf.firearms.config.TaczBackendConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;

/**
 * TaCZ 命中：总伤害走 Guns RPG 枪身 × 材料弹 × 天赋；穿甲比例仍用枪 data / 配件的 armor_ignore（原版拆分）。
 */
public final class TaczCombatBridge {
    private static final String HURT_PRE = "com.tacz.guns.api.event.common.EntityHurtByGunEvent$Pre";

    private TaczCombatBridge() {}

    public static void registerHurtListener() {
        if (!TaczCompat.isTaczLoaded()) {
            return;
        }
        try {
            Class<?> raw = Class.forName(HURT_PRE);
            if (!Event.class.isAssignableFrom(raw)) {
                return;
            }
            @SuppressWarnings("unchecked")
            Class<? extends Event> eventClass = (Class<? extends Event>) raw;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(
                    EventPriority.HIGHEST,
                    false,
                    eventClass,
                    TaczCombatBridge::onHurtPre);
            GunsRpg.LOGGER.info("[gunsrpg] 已订阅 TaCZ EntityHurtByGunEvent.Pre（材料弹伤害）");
        } catch (ClassNotFoundException ex) {
            GunsRpg.LOGGER.warn("[gunsrpg] 无法注册 TaCZ 命中伤害桥接", ex);
        }
    }

    private static void onHurtPre(Event event) {
        if (TaczBridge.readLogicalSide(event).isClient()) {
            return;
        }
        LivingEntity attacker = readAttacker(event);
        if (!(attacker instanceof Player player)) {
            return;
        }
        if (!TaczBackendConfig.useTaczShooting()) {
            return;
        }
        ItemStack gun = player.getMainHandItem();
        if (TaczShootBlock.shouldBlockGunHit(player, gun)) {
            event.setCanceled(true);
            return;
        }
        if (!TaczBridge.isTaczGun(gun) || !TaczGunsrpgAmmoBridge.usesGunsrpgMaterialAmmo(gun)) {
            return;
        }
        String weaponKey =
                TaczGunsrpgAmmoBridge.resolveWeaponKey(gun).orElse(null);
        if (weaponKey == null) {
            return;
        }
        FirearmSpec spec = FirearmRegistry.getOrDefault(weaponKey);
        if (LauncherShooter.isLauncher(weaponKey)) {
            event.setCanceled(true);
            return;
        }
        TaczGunStatSync.ensureLoadedAmmoForDamage(player, gun, spec);

        float damage = TaczGunStatSync.computePelletDamage(player, gun, spec);
        Entity victim = readHurtEntity(event);
        if (victim instanceof LivingEntity living) {
            double dist = Math.sqrt(player.distanceToSqr(living));
            damage =
                    FirearmDamage.withDistanceFalloff(
                            damage, spec.weaponKey(), spec.weaponClass(), dist);
            damage = TaczPerkBridge.applyPuncturingCrit(gun, damage, player.getRandom());
            if (readHeadshot(event)) {
                setHeadshotMultiplier(
                        event,
                        TaczPerkBridge.resolveHeadshotMultiplier(event, player, gun, weaponKey));
            }
            damage *= WeaponExtensionService.contextualDamageMultiplier(player, weaponKey, living);
            damage = com.wf.firearms.gameplay.WeaponExtensionCombat.damageMultiplier(
                    player, weaponKey, living, damage);
            if (TaczGunEnchantBridge.hasFireStarter(gun)) {
                living.setSecondsOnFire(4);
            }
            com.wf.firearms.gameplay.WeaponExtensionCombat.onGunHit(player, weaponKey, living);
        }
        setBaseAmount(event, damage);
    }

    private static boolean readHeadshot(Event event) {
        try {
            Object v = event.getClass().getMethod("isHeadShot").invoke(event);
            return v instanceof Boolean b && b;
        } catch (ReflectiveOperationException ignored) {
        }
        return false;
    }

    private static void setHeadshotMultiplier(Event event, float mult) {
        try {
            event.getClass().getMethod("setHeadshotMultiplier", float.class).invoke(event, Math.max(0.1f, mult));
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.debug("[gunsrpg] 设置 TaCZ 爆头倍率失败", ex);
        }
    }

    private static void setBaseAmount(Event event, float amount) {
        try {
            event.getClass().getMethod("setBaseAmount", float.class).invoke(event, Math.max(0.5f, amount));
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.debug("[gunsrpg] 设置 TaCZ 命中伤害失败", ex);
        }
    }

    private static LivingEntity readAttacker(Event event) {
        try {
            Object entity = event.getClass().getMethod("getAttacker").invoke(event);
            if (entity instanceof LivingEntity living) {
                return living;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return null;
    }

    private static Entity readHurtEntity(Event event) {
        try {
            Object entity = event.getClass().getMethod("getHurtEntity").invoke(event);
            if (entity instanceof Entity e) {
                return e;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return null;
    }
}
