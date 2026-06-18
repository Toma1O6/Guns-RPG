package com.wf.firearms.combat;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.data.WeaponMapping;
import com.wf.firearms.item.FirearmItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** 连射调度：首发射击后，按短间隔自动打出后续几发（如 S686 快速双发）。 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class FirearmBurstHandler {
    private static final String KEY_WEAPON = "wf_burst_weapon";
    private static final String KEY_REMAIN = "wf_burst_remain";
    private static final String KEY_NEXT = "wf_burst_next";
    private static final String KEY_INTERVAL = "wf_burst_interval";

    private FirearmBurstHandler() {}

    public static boolean hasPendingBurst(Player player, String weaponKey) {
        CompoundTag tag = player.getPersistentData();
        return tag.contains(KEY_REMAIN) && weaponKey.equals(tag.getString(KEY_WEAPON));
    }

    public static void scheduleBurst(Player player, ItemStack gun, FirearmSpec spec) {
        if (spec.burstShots() <= 1 || player.level().isClientSide) {
            return;
        }
        int followUps = spec.burstShots() - 1;
        if (followUps <= 0 || !hasAmmoForBurst(player, gun, spec)) {
            return;
        }
        CompoundTag tag = player.getPersistentData();
        tag.putString(KEY_WEAPON, spec.weaponKey());
        tag.putInt(KEY_REMAIN, followUps);
        tag.putInt(KEY_INTERVAL, spec.burstIntervalTicks());
        tag.putLong(KEY_NEXT, player.level().getGameTime() + spec.burstIntervalTicks());
    }

    public static void clearBurst(Player player) {
        CompoundTag tag = player.getPersistentData();
        tag.remove(KEY_WEAPON);
        tag.remove(KEY_REMAIN);
        tag.remove(KEY_NEXT);
        tag.remove(KEY_INTERVAL);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer player)) {
            return;
        }
        CompoundTag tag = player.getPersistentData();
        if (!tag.contains(KEY_REMAIN)) {
            return;
        }
        if (player.level().getGameTime() < tag.getLong(KEY_NEXT)) {
            return;
        }

        String weaponKey = tag.getString(KEY_WEAPON);
        ItemStack gun = heldGun(player, weaponKey);
        if (gun.isEmpty()) {
            clearBurst(player);
            return;
        }
        FirearmSpec spec = FirearmRegistry.getOrDefault(weaponKey);
        if (!fireFollowUp(player, gun, spec)) {
            clearBurst(player);
            return;
        }

        int remain = tag.getInt(KEY_REMAIN) - 1;
        if (remain > 0 && hasAmmoForBurst(player, gun, spec)) {
            tag.putInt(KEY_REMAIN, remain);
            tag.putLong(KEY_NEXT, player.level().getGameTime() + tag.getInt(KEY_INTERVAL));
        } else {
            clearBurst(player);
        }
    }

    private static boolean fireFollowUp(Player player, ItemStack gun, FirearmSpec spec) {
        if (gun.getItem() instanceof FirearmItem) {
            return FirearmShooter.fireBurstFollowUp(player, gun, spec);
        }
        return false;
    }

    private static boolean hasAmmoForBurst(Player player, ItemStack gun, FirearmSpec spec) {
        if (gun.getItem() instanceof FirearmItem) {
            FirearmStackState.ensureInitialized(gun, spec);
            return FirearmStackState.getAmmo(gun) > 0 && FirearmStackState.hasLoadedAmmoType(gun);
        }
        return false;
    }

    private static ItemStack heldGun(Player player, String weaponKey) {
        for (ItemStack stack : new ItemStack[] {player.getMainHandItem(), player.getOffhandItem()}) {
            if (stack.isEmpty()) {
                continue;
            }
            if (stack.getItem() instanceof FirearmItem firearm && weaponKey.equals(firearm.getWeaponKey())) {
                return stack;
            }
            if (WeaponMapping.weaponKeyFromStack(stack).filter(weaponKey::equals).isPresent()) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }
}
