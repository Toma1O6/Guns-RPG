package com.wf.firearms.combat;

import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.gameplay.PerkEffectService;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/** 射击时累积武器损耗（CGM 与原生枪共用）。 */
public final class WeaponWearService {
    private WeaponWearService() {}

    public static float wearChance(Player player, String weaponKey, AmmoStatProfile ammo) {
        float chance = 1.0f;
        chance *= (float) PerkEffectService.wearDamageMultiplier(player, weaponKey);
        chance *= (float) com.wf.firearms.gameplay.WeaponExtensionCombat.wearChanceMultiplier(player, weaponKey);
        chance *= Math.max(0.05f, 1.0f - ammo.durabilityAdd());
        return Math.max(0.01f, chance);
    }

    public static void applyShotWear(Player player, ItemStack gun, String weaponKey, AmmoStatProfile ammo) {
        if (player.getAbilities().instabuild || player.level().isClientSide) {
            return;
        }
        if (WeaponWearState.isDestroyed(gun, weaponKey)) {
            return;
        }
        float chance = wearChance(player, weaponKey, ammo);
        RandomSource random = player.level().random;
        int amount = rollWearAmount(chance, random, WeaponWearState.remainingWear(gun, weaponKey));
        if (amount <= 0) {
            return;
        }
        WeaponWearState.addWear(gun, weaponKey, amount);
        if (WeaponWearState.isDestroyed(gun, weaponKey)) {
            player.displayClientMessage(Component.translatable("gunsrpg.gun.wear_destroyed"), true);
        }
    }

    private static int rollWearAmount(float chance, RandomSource random, int remaining) {
        if (remaining <= 0) {
            return 0;
        }
        if (chance < 1.0f) {
            return random.nextFloat() < chance ? 1 : 0;
        }
        int amount = 1;
        if (random.nextFloat() < chance - 1.0f) {
            amount = 2;
        }
        return Math.min(amount, remaining);
    }
}
