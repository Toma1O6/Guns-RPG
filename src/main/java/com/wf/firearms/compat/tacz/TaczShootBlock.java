package com.wf.firearms.compat.tacz;

import com.wf.firearms.combat.WeaponWearState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** 卡弹 / 排障 / 损毁：统一射击与命中门禁（含客户端排障状态）。 */
public final class TaczShootBlock {
    private static final String CLIENT_UNJAM_STATE =
            "com.wf.firearms.client.compat.tacz.TaczUnjamClientState";

    private TaczShootBlock() {}

    public static boolean isPackTaczGun(ItemStack gun) {
        if (gun == null || gun.isEmpty() || !TaczBridge.isTaczGun(gun)) {
            return false;
        }
        return TaczGunsrpgAmmoBridge.usesGunsrpgMaterialAmmo(gun)
                || TaczWeaponBinding.read(gun).isPresent();
    }

    public static boolean isJammedOrUnjamming(Player player, ItemStack gun) {
        if (gun != null && !gun.isEmpty() && TaczPerkBridge.isJammed(gun)) {
            return true;
        }
        if (player != null && TaczUnjamBridge.isUnjamming(player)) {
            return true;
        }
        return isClientUnjamActive();
    }

    public static boolean shouldBlockShoot(Player player, ItemStack gun) {
        if (player == null || gun == null || gun.isEmpty() || !TaczBridge.isTaczGun(gun)) {
            return false;
        }
        if (isPackTaczGun(gun) && isJammedOrUnjamming(player, gun)) {
            return true;
        }
        if (!TaczGunsrpgAmmoBridge.usesGunsrpgMaterialAmmo(gun)) {
            return false;
        }
        String weaponKey = TaczWeaponUseBridge.resolveWeaponKey(gun).orElse(null);
        return weaponKey != null && WeaponWearState.isDestroyed(gun, weaponKey);
    }

    public static boolean shouldBlockGunHit(Player player, ItemStack gun) {
        return isPackTaczGun(gun) && isJammedOrUnjamming(player, gun);
    }

    private static boolean isClientUnjamActive() {
        try {
            Class<?> cls = Class.forName(CLIENT_UNJAM_STATE);
            Object result = cls.getMethod("isActive").invoke(null);
            return result instanceof Boolean active && active;
        } catch (ReflectiveOperationException ignored) {
            return false;
        }
    }
}
