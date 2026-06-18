package com.wf.firearms.gameplay;

import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.data.WeaponMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

/** 装配天赋：用于武器熟练度、扩展天赋与 TaCZ 改装界面（Z 键）校验。 */
public final class WeaponUseGate {
    private WeaponUseGate() {}

    public static boolean hasAssemblyUnlocked(Player player, String weaponKey) {
        if (player == null || weaponKey == null || weaponKey.isEmpty()) {
            return false;
        }
        return PlayerFirearmsData.isUnlocked(player, weaponKey + "_assembly");
    }

    public static Optional<Component> denyReason(Player player, ItemStack gun) {
        return Optional.empty();
    }

    public static Optional<Component> denyReason(Player player, String weaponKey) {
        return Optional.empty();
    }

    public static Optional<Component> denyProgressionReason(Player player, String weaponKey) {
        if (weaponKey == null || weaponKey.isEmpty()) {
            return Optional.of(Component.translatable("gunsrpg.gun.unknown_weapon"));
        }
        if (!hasAssemblyUnlocked(player, weaponKey)) {
            return Optional.of(
                    Component.translatable(
                            "gunsrpg.gun.need_assembly_progression",
                            WeaponMapping.displayNameForWeaponKey(weaponKey)));
        }
        return Optional.empty();
    }

    public static Optional<Component> denyAttachmentReason(Player player, String weaponKey) {
        return denyProgressionReason(player, weaponKey);
    }
}
