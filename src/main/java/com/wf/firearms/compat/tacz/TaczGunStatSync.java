package com.wf.firearms.compat.tacz;

import com.wf.firearms.combat.FirearmDamage;
import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.combat.FirearmSpec;
import com.wf.firearms.combat.FirearmStackState;
import com.wf.firearms.combat.PlayerAmmoInventory;
import com.wf.firearms.gameplay.PerkEffectService;
import com.wf.firearms.gameplay.WeaponExtensionService;
import com.wf.firearms.item.AmmoItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

/** 将 {@link FirearmRegistry} 伤害同步到 TaCZ 展示与命中重算（对标已移除的 CGM {@code CgmGunStatSync}）。 */
public final class TaczGunStatSync {
    static final String NBT_DISPLAY_DAMAGE = "gunsrpg_tacz_display_damage";

    private TaczGunStatSync() {}

    /** 写入枪 NBT，供 AttachmentProperty / tooltip 读取。 */
    public static void syncDisplayDamage(Player player, ItemStack gun, String weaponKey) {
        if (player == null || gun.isEmpty() || weaponKey == null) {
            return;
        }
        FirearmSpec spec = FirearmRegistry.getOrDefault(weaponKey);
        float dmg = computePelletDamage(player, gun, spec);
        gun.getOrCreateTag().putFloat(NBT_DISPLAY_DAMAGE, Math.max(0.5f, dmg));
    }

    static float computePelletDamage(Player player, ItemStack gun, FirearmSpec spec) {
        ensureLoadedAmmoForDamage(player, gun, spec);
        return FirearmDamage.pelletDamage(player, gun, spec);
    }

    /**
     * 弹匣有弹但未换过弹时，TaCZ 不会写入材料弹类型，导致命中桥接直接 return。
     * 按背包优先弹种推断，仅用于伤害计算。
     */
    static void ensureLoadedAmmoForDamage(Player player, ItemStack gun, FirearmSpec spec) {
        if (FirearmStackState.hasLoadedAmmoType(gun)) {
            return;
        }
        if (TaczBridge.getCurrentAmmoCount(gun) <= 0) {
            return;
        }
        Item ammo = PlayerAmmoInventory.resolveReloadAmmo(player, spec, gun);
        if (ammo instanceof AmmoItem ammoItem) {
            FirearmStackState.setLoadedMaterial(gun, ammoItem.getMaterial());
            FirearmStackState.setLoadedAmmoItem(gun, AmmoItem.itemId(ammoItem));
        }
    }

    static float readCachedDisplayDamage(ItemStack gun, FirearmSpec spec) {
        if (gun != null && gun.hasTag() && gun.getTag().contains(NBT_DISPLAY_DAMAGE)) {
            return Math.max(0.5f, gun.getTag().getFloat(NBT_DISPLAY_DAMAGE));
        }
        return Math.max(0.5f, spec.baseDamage());
    }

    public static float readDisplayDamage(ItemStack gun, FirearmSpec spec, @Nullable Player player) {
        if (player != null && gun != null && !gun.isEmpty()) {
            return computePelletDamage(player, gun, spec);
        }
        return readCachedDisplayDamage(gun, spec);
    }

    public static MutableComponent damageTooltipLine(float damage) {
        return Component.translatable(
                        "gunsrpg.gun.base_damage",
                        Component.literal(String.format("%.1f", damage)).withStyle(ChatFormatting.WHITE))
                .withStyle(ChatFormatting.GRAY);
    }

    static float reloadTimeMultiplier(Player player, ItemStack gun, String weaponKey) {
        float enchant = TaczGunEnchantBridge.reloadSpeedMultiplier(gun);
        double perk = PerkEffectService.reloadSpeedMultiplier(player);
        double ext = WeaponExtensionService.reloadTimeMultiplier(player, weaponKey);
        return (float) Math.max(0.35, enchant / perk * ext);
    }
}
