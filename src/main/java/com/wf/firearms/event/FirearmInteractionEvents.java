package com.wf.firearms.event;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.combat.FirearmCombat;
import com.wf.firearms.combat.FirearmStackState;
import com.wf.firearms.item.FirearmItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** 持枪时：左键仅开火、右键仅瞄准；不挥臂、不破坏方块、不与方块交互。 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class FirearmInteractionEvents {
    private FirearmInteractionEvents() {}

    private static ItemStack heldGun(Player player) {
        return FirearmCombat.getHeldGun(player);
    }

    private static boolean holdingFirearm(Player player) {
        ItemStack gun = heldGun(player);
        return !gun.isEmpty() && gun.getItem() instanceof FirearmItem;
    }

    private static void toggleAimServer(Player player) {
        ItemStack gun = heldGun(player);
        if (gun.isEmpty() || !(gun.getItem() instanceof FirearmItem firearm)) {
            return;
        }
        FirearmStackState.ensureInitialized(gun, firearm.getSpec());
        if (FirearmStackState.isBusy(gun)) {
            return;
        }
        FirearmStackState.toggleAiming(gun);
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (holdingFirearm(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLeftClickEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        if (holdingFirearm(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!holdingFirearm(event.getEntity())) {
            return;
        }
        event.setCanceled(true);
        if (!event.getLevel().isClientSide) {
            toggleAimServer(event.getEntity());
        }
    }
}
