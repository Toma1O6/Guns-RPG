package com.wf.firearms.compat.tacz;

import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.config.TaczBackendConfig;
import com.wf.firearms.gameplay.ProgressionService;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.LogicalSide;

/** 发放 TaCZ 枪械（解锁 / 枪械台 / 指令）。 */
public final class TaczGiveGun {
    private TaczGiveGun() {}

    public static ItemStack create(String weaponKey, boolean fullMagazine) {
        if (!TaczBackendConfig.useTaczShooting() || FirearmRegistry.get(weaponKey).isEmpty()) {
            return ItemStack.EMPTY;
        }
        ResourceLocation gunId = TaczWeaponCatalog.gunIdForWeapon(weaponKey).orElse(null);
        if (gunId == null) {
            return ItemStack.EMPTY;
        }
        int ammo = fullMagazine ? TaczBridge.defaultMagazineCapacity(gunId) : 0;
        ItemStack stack = TaczBridge.buildGun(gunId, ammo);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        TaczWeaponBinding.bind(stack, weaponKey);
        TaczGunsrpgAmmoBridge.enableGunsrpgAmmoPool(stack);
        TaczGunPresentation.applyToPackGun(stack, weaponKey);
        return stack;
    }

    public static boolean give(Player player, String weaponKey, boolean fullMagazine) {
        ItemStack stack = create(weaponKey, fullMagazine);
        if (stack.isEmpty()) {
            return false;
        }
        if (!player.getInventory().add(stack.copy())) {
            player.drop(stack.copy(), false);
        }
        return true;
    }

    static void onEntityKillByGun(Object event) {
        if (TaczBridge.readLogicalSide(event).isClient()) {
            return;
        }
        LivingEntity killed = TaczBridge.readKilled(event);
        LivingEntity attacker = TaczBridge.readAttacker(event);
        if (killed == null || !(attacker instanceof ServerPlayer player)) {
            return;
        }
        ResourceLocation gunId = TaczBridge.readGunIdFromEvent(event);
        if (gunId == null) {
            return;
        }
        String wfKey = TaczWeaponCatalog.weaponKeyForGunId(gunId)
                .or(() -> TaczWeaponBinding.read(player.getMainHandItem()))
                .orElse(null);
        if (wfKey == null) {
            return;
        }
        ProgressionService.recordGunKill(player, wfKey);
    }
}
