package com.wf.firearms.compat.tacz;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.compat.TaczCompat;
import com.wf.firearms.combat.WeaponWearState;
import com.wf.firearms.config.TaczBackendConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.LogicalSide;

/** TaCZ 射击/换弹：磨损、卡弹与排障门禁（客户端同步拦截开火动画/音效）。 */
public final class TaczWeaponUseBridge {
    private static final String GUN_SHOOT = "com.tacz.guns.api.event.common.GunShootEvent";
    private static final String GUN_RELOAD = "com.tacz.guns.api.event.common.GunReloadEvent";
    private static final String GUN_FIRE = "com.tacz.guns.api.event.common.GunFireEvent";

    private TaczWeaponUseBridge() {}

    public static void registerListeners() {
        if (!TaczCompat.isTaczLoaded() || !TaczBackendConfig.useTaczShooting()) {
            return;
        }
        registerShootListener();
        registerReloadListener();
        registerClientFireListener();
        GunsRpg.LOGGER.info("[gunsrpg] 已订阅 TaCZ 射击/换弹/击发事件（磨损/卡弹/客户端拦截）");
    }

    private static void registerShootListener() {
        try {
            Class<?> raw = Class.forName(GUN_SHOOT);
            if (!Event.class.isAssignableFrom(raw)) {
                return;
            }
            @SuppressWarnings("unchecked")
            Class<? extends Event> eventClass = (Class<? extends Event>) raw;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(
                    EventPriority.HIGHEST, false, eventClass, TaczWeaponUseBridge::onGunShoot);
        } catch (ClassNotFoundException ex) {
            GunsRpg.LOGGER.warn("[gunsrpg] 无法注册 TaCZ GunShootEvent 装配校验", ex);
        }
    }

    private static void registerReloadListener() {
        try {
            Class<?> raw = Class.forName(GUN_RELOAD);
            if (!Event.class.isAssignableFrom(raw)) {
                return;
            }
            @SuppressWarnings("unchecked")
            Class<? extends Event> eventClass = (Class<? extends Event>) raw;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(
                    EventPriority.HIGH, false, eventClass, TaczWeaponUseBridge::onGunReload);
        } catch (ClassNotFoundException ex) {
            GunsRpg.LOGGER.warn("[gunsrpg] 无法注册 TaCZ GunReloadEvent 装配校验", ex);
        }
    }

    private static void registerClientFireListener() {
        try {
            Class<?> raw = Class.forName(GUN_FIRE);
            if (!Event.class.isAssignableFrom(raw)) {
                return;
            }
            @SuppressWarnings("unchecked")
            Class<? extends Event> eventClass = (Class<? extends Event>) raw;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(
                    EventPriority.HIGHEST, false, eventClass, TaczWeaponUseBridge::onGunFire);
        } catch (ClassNotFoundException ex) {
            GunsRpg.LOGGER.warn("[gunsrpg] 无法注册 TaCZ GunFireEvent 客户端卡弹拦截", ex);
        }
    }

    private static void onGunShoot(Event event) {
        LivingEntity shooter = readShooter(event);
        if (!(shooter instanceof Player player)) {
            return;
        }
        ItemStack gun = readGun(event);
        if (TaczShootBlock.shouldBlockShoot(player, gun)) {
            event.setCanceled(true);
        }
    }

    private static void onGunReload(Event event) {
        Player player = readReloadPlayer(event);
        ItemStack gun = readReloadGun(event);
        if (shouldBlockReload(player, gun)) {
            event.setCanceled(true);
        }
    }

    private static void onGunFire(Event event) {
        LivingEntity shooter = readFireShooter(event);
        if (!(shooter instanceof Player player)) {
            return;
        }
        ItemStack gun = readFireGun(event);
        if (TaczShootBlock.shouldBlockShoot(player, gun)) {
            event.setCanceled(true);
        }
    }

    /** 卡弹时仍允许换弹（换弹完成会清除卡弹）。 */
    static boolean shouldBlockReload(Player player, ItemStack gun) {
        if (player == null || gun == null || gun.isEmpty() || !TaczBridge.isTaczGun(gun)) {
            return false;
        }
        if (!TaczGunsrpgAmmoBridge.usesGunsrpgMaterialAmmo(gun)) {
            return false;
        }
        String weaponKey = resolveWeaponKey(gun).orElse(null);
        if (weaponKey != null && WeaponWearState.isDestroyed(gun, weaponKey)) {
            return true;
        }
        return TaczUnjamBridge.isUnjamming(player);
    }

    static boolean shouldBlockShoot(Player player, ItemStack gun) {
        return TaczShootBlock.shouldBlockShoot(player, gun);
    }

    static java.util.Optional<String> resolveWeaponKey(ItemStack gun) {
        return TaczGunsrpgAmmoBridge.resolveWeaponKey(gun).or(() -> TaczWeaponBinding.read(gun));
    }

    private static LogicalSide readSide(Event event) {
        try {
            Object side = event.getClass().getMethod("getLogicalSide").invoke(event);
            if (side instanceof LogicalSide logicalSide) {
                return logicalSide;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return TaczBridge.readLogicalSide(event);
    }

    private static LivingEntity readShooter(Event event) {
        try {
            Object shooter = event.getClass().getMethod("getShooter").invoke(event);
            if (shooter instanceof LivingEntity living) {
                return living;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return null;
    }

    private static LivingEntity readFireShooter(Event event) {
        try {
            Object shooter = event.getClass().getMethod("getShooter").invoke(event);
            if (shooter instanceof LivingEntity living) {
                return living;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return readShooter(event);
    }

    private static ItemStack readGun(Event event) {
        try {
            Object stack = event.getClass().getMethod("getGunItemStack").invoke(event);
            if (stack instanceof ItemStack itemStack) {
                return itemStack;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack readFireGun(Event event) {
        return readGun(event);
    }

    private static Player readReloadPlayer(Event event) {
        try {
            Object entity = event.getClass().getMethod("getEntity").invoke(event);
            if (entity instanceof Player player) {
                return player;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return null;
    }

    private static ItemStack readReloadGun(Event event) {
        return readGun(event);
    }
}
