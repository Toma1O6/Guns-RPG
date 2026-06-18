package com.wf.firearms.combat;

import com.wf.firearms.gameplay.PerkEffectService;
import com.wf.firearms.gameplay.WeaponExtensionService;
import com.wf.firearms.item.AmmoItem;
import com.wf.firearms.item.FirearmItem;
import com.wf.firearms.client.sound.ReloadSoundClip;
import com.wf.firearms.network.FirearmStateSyncPacket;
import com.wf.firearms.network.ReloadSoundPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** 换弹、排障、开火模式与弹匣装填（服务端）。 */
public final class FirearmCombat {
    private static final Map<UUID, Integer> LAST_HOTBAR_SLOT = new ConcurrentHashMap<>();

    private FirearmCombat() {}

    public static ItemStack getHeldGun(Player player) {
        ItemStack main = player.getMainHandItem();
        if (main.getItem() instanceof FirearmItem) {
            return main;
        }
        ItemStack off = player.getOffhandItem();
        if (off.getItem() instanceof FirearmItem) {
            return off;
        }
        return ItemStack.EMPTY;
    }

    public static FirearmSpec specOf(ItemStack stack) {
        if (stack.getItem() instanceof FirearmItem gun) {
            return gun.getSpec();
        }
        return FirearmRegistry.getOrDefault("m1911");
    }

    /**
     * 切换快捷栏槽位时取消上一把 / 当前枪的瞄准（含 FOV 与 NBT）。
     *
     * @return 是否发生了槽位切换
     */
    public static boolean tickHotbarAimReset(Player player) {
        int sel = player.getInventory().selected;
        Integer prev = LAST_HOTBAR_SLOT.get(player.getUUID());
        LAST_HOTBAR_SLOT.put(player.getUUID(), sel);
        if (prev == null || prev == sel) {
            return false;
        }
        ItemStack oldStack = player.getInventory().getItem(prev);
        if (!oldStack.isEmpty() && oldStack.getItem() instanceof FirearmItem) {
            FirearmStackState.setAiming(oldStack, false);
        }
        ItemStack held = getHeldGun(player);
        if (!held.isEmpty() && held.getItem() instanceof FirearmItem) {
            FirearmStackState.setAiming(held, false);
            if (!player.level().isClientSide) {
                syncState(player, held);
            }
        }
        return true;
    }

    public static void tickPlayer(Player player) {
        tickHotbarAimReset(player);
        if (player.level().isClientSide) {
            return;
        }
        ItemStack gun = getHeldGun(player);
        if (gun.isEmpty()) {
            return;
        }
        FirearmSpec spec = specOf(gun);
        FirearmStackState.ensureInitialized(gun, spec);
        long now = player.level().getGameTime();
        FirearmStackState.Action action = FirearmStackState.getAction(gun);
        if (action == FirearmStackState.Action.NONE) {
            return;
        }
        if (now < FirearmStackState.getActionEnd(gun)) {
            return;
        }
        if (action == FirearmStackState.Action.RELOAD) {
            if (spec.reloadStyle() == ReloadStyle.SHELL_BY_SHELL) {
                if (finishShellReload(player, gun, spec)) {
                    int ticks = shellReloadTicks(player, spec);
                    FirearmStackState.setAction(gun, FirearmStackState.Action.RELOAD, now, now + ticks);
                    sendReloadSounds(player, spec, gun, ticks);
                    syncState(player, gun);
                    return;
                }
            } else {
                finishReload(player, gun, spec);
            }
        } else if (action == FirearmStackState.Action.UNJAM) {
            finishUnjam(player, gun);
        }
        FirearmStackState.setAction(gun, FirearmStackState.Action.NONE, 0L, 0L);
        syncState(player, gun);
    }

    public static boolean startReload(Player player, ItemStack gun) {
        FirearmSpec spec = specOf(gun);
        FirearmStackState.ensureInitialized(gun, spec);
        if (FirearmStackState.isBusy(gun) || FirearmStackState.isJammed(gun)) {
            return false;
        }
        FirearmStackState.setAiming(gun, false);
        if (FirearmStackState.getAmmo(gun) >= effectiveMagazineSize(player, spec)) {
            return false;
        }
        if (!hasCompatibleAmmoInInventory(player, spec, gun)) {
            player.displayClientMessage(Component.translatable("gunsrpg.gun.no_ammo_reserve"), true);
            return false;
        }
        int ticks =
                spec.reloadStyle() == ReloadStyle.SHELL_BY_SHELL
                        ? initialShellReloadTicks(player, gun, spec)
                        : reloadTicks(player, spec);
        long now = player.level().getGameTime();
        FirearmStackState.setAction(gun, FirearmStackState.Action.RELOAD, now, now + ticks);
        player.displayClientMessage(Component.translatable("gunsrpg.gun.reload_start"), true);
        sendReloadSounds(player, spec, gun, ticks);
        syncState(player, gun);
        return true;
    }

    public static boolean startUnjam(Player player, ItemStack gun) {
        FirearmSpec spec = specOf(gun);
        FirearmStackState.ensureInitialized(gun, spec);
        if (FirearmStackState.isBusy(gun)) {
            return false;
        }
        if (!FirearmStackState.isJammed(gun)) {
            return false;
        }
        int ticks = unjamTicks(player, spec);
        long now = player.level().getGameTime();
        FirearmStackState.setAction(gun, FirearmStackState.Action.UNJAM, now, now + ticks);
        player.displayClientMessage(Component.translatable("gunsrpg.gun.unjam_start"), true);
        if (player instanceof ServerPlayer server) {
            ReloadSoundPacket.send(server, spec.weaponKey(), ReloadSoundClip.UNJAM, ticks);
        }
        syncState(player, gun);
        return true;
    }

    public static boolean cycleFireMode(Player player, ItemStack gun) {
        return cycleFireMode(player, gun, specOf(gun));
    }

    public static boolean cycleFireMode(Player player, ItemStack gun, FirearmSpec spec) {
        if (!spec.supportsFireModeSwitch()) {
            return false;
        }
        FirearmStackState.ensureInitialized(gun, spec);
        if (FirearmStackState.isBusy(gun)) {
            return false;
        }
        FirearmMode next = FirearmStackState.getFireMode(gun, spec).toggle(true);
        FirearmStackState.setFireMode(gun, next);
        player.displayClientMessage(
                Component.translatable(
                        "gunsrpg.gun.firemode",
                        Component.translatable("gunsrpg.gun.firemode." + next.name().toLowerCase())),
                true);
        syncState(player, gun, spec);
        return true;
    }

    public static boolean canFire(Player player, ItemStack gun, FirearmSpec spec) {
        FirearmStackState.ensureInitialized(gun, spec);
        if (FirearmBurstHandler.hasPendingBurst(player, spec.weaponKey())) {
            return false;
        }
        if (FirearmStackState.isBusy(gun) || FirearmStackState.isJammed(gun)) {
            return false;
        }
        if (WeaponWearState.isDestroyed(gun, spec.weaponKey())) {
            return false;
        }
        if (FirearmStackState.getAmmo(gun) <= 0 || !FirearmStackState.hasLoadedAmmoType(gun)) {
            return false;
        }
        return !player.getCooldowns().isOnCooldown(gun.getItem());
    }

    public static void onShotFired(Player player, ItemStack gun, FirearmSpec spec, Level level) {
        int left = FirearmStackState.getAmmo(gun) - 1;
        FirearmStackState.setAmmo(gun, left);
        if (left <= 0) {
            player.displayClientMessage(Component.translatable("gunsrpg.gun.empty_mag"), true);
        }
        AmmoStatProfile ammoStats = FirearmStackState.loadedAmmoStats(gun, spec);
        double jamChance =
                (spec.jamChancePerShot() + ammoStats.jamChanceAdd())
                        * WeaponExtensionService.jamMultiplier(player, spec.weaponKey())
                        * com.wf.firearms.gameplay.WeaponExtensionCombat.extraJamChanceMultiplier(
                                player, spec.weaponKey())
                        * PerkEffectService.jamChanceMultiplier(player, spec.weaponKey());
        if (jamChance > 0 && level.random.nextDouble() < Math.max(0, jamChance)) {
            FirearmStackState.setJammed(gun, true);
            player.displayClientMessage(Component.translatable("gunsrpg.gun.jammed_notice"), true);
        }
        WeaponWearService.applyShotWear(player, gun, spec.weaponKey(), ammoStats);
        com.wf.firearms.gameplay.GunshotAlertService.onGunFire(player, spec.weaponKey(), spec);
        com.wf.firearms.gameplay.WeaponExtensionCombat.onGunFire(player, spec.weaponKey());
        syncState(player, gun);
    }

    public static float spreadForShot(Player player, ItemStack gun, FirearmSpec spec, boolean aiming) {
        float spread = spec.spread();
        if (aiming) {
            spread *= spec.aimSpreadMultiplier();
        }
        spread *= (float) PerkEffectService.recoilMultiplier(player);
        spread *= (float) com.wf.firearms.gameplay.WeaponExtensionService.spreadMultiplier(player, spec.weaponKey());
        spread *= com.wf.firearms.gameplay.WeaponExtensionCombat.recoilMultiplier(player, spec.weaponKey());
        spread *= WeaponBalance.spreadMultiplier(spec.weaponKey(), isAutomaticMode(gun, spec));
        AmmoStatProfile ammoStats = FirearmStackState.loadedAmmoStats(gun, spec);
        spread *= 1f + Math.max(0f, ammoStats.recoilAdd());
        return Math.max(0.001f, spread);
    }

    public static float damageForShot(Player player, ItemStack gun, FirearmSpec spec) {
        return FirearmDamage.pelletDamage(player, gun, spec);
    }

    public static boolean isAutomaticMode(ItemStack gun, FirearmSpec spec) {
        if (spec.defaultAutomatic() && !spec.supportsFireModeSwitch()) {
            return true;
        }
        if (spec.supportsFireModeSwitch()) {
            return FirearmStackState.getFireMode(gun, spec) == FirearmMode.AUTO;
        }
        return spec.defaultAutomatic();
    }

    private static boolean finishShellReload(Player player, ItemStack gun, FirearmSpec spec) {
        Item ammoItem = PlayerAmmoInventory.resolveReloadAmmo(player, spec, gun);
        if (ammoItem == null) {
            player.displayClientMessage(Component.translatable("gunsrpg.gun.no_ammo_reserve"), true);
            return false;
        }

        AmmoMaterial material = AmmoMaterial.fromItem(ammoItem);
        String itemId = AmmoItem.itemId(ammoItem);

        if (player.getAbilities().instabuild) {
            if (!FirearmStackState.hasLoadedAmmoType(gun)) {
                FirearmStackState.setLoadedMaterial(gun, material);
                FirearmStackState.setLoadedAmmoItem(gun, itemId);
            }
            int cap = effectiveMagazineSize(player, spec);
            int next = Math.min(cap, FirearmStackState.getAmmo(gun) + 1);
            FirearmStackState.setAmmo(gun, next);
        } else {
            int moved = PlayerAmmoInventory.transferAmmo(player, ammoItem, 1);
            if (moved <= 0) {
                player.displayClientMessage(Component.translatable("gunsrpg.gun.no_ammo_reserve"), true);
                return false;
            }
            if (!FirearmStackState.hasLoadedAmmoType(gun)) {
                FirearmStackState.setLoadedMaterial(gun, material);
                FirearmStackState.setLoadedAmmoItem(gun, itemId);
            }
            FirearmStackState.setAmmo(
                    gun, Math.min(effectiveMagazineSize(player, spec), FirearmStackState.getAmmo(gun) + moved));
        }

        boolean canContinue =
                FirearmStackState.getAmmo(gun) < effectiveMagazineSize(player, spec)
                        && hasCompatibleAmmoInInventory(player, spec, gun);
        if (!canContinue) {
            player.displayClientMessage(reloadDoneMessage(material, AmmoCaliber.fromAmmoItem(ammoItem)), true);
        }
        return canContinue;
    }

    private static void finishReload(Player player, ItemStack gun, FirearmSpec spec) {
        Item ammoItem = PlayerAmmoInventory.resolveReloadAmmo(player, spec, gun);
        if (ammoItem == null) {
            player.displayClientMessage(Component.translatable("gunsrpg.gun.no_ammo_reserve"), true);
            return;
        }

        AmmoMaterial material = AmmoMaterial.fromItem(ammoItem);
        String itemId = AmmoItem.itemId(ammoItem);

        if (player.getAbilities().instabuild) {
            FirearmStackState.setLoadedMaterial(gun, material);
            FirearmStackState.setLoadedAmmoItem(gun, itemId);
            FirearmStackState.setAmmo(gun, effectiveMagazineSize(player, spec));
            player.displayClientMessage(reloadDoneMessage(material, AmmoCaliber.fromAmmoItem(ammoItem)), true);
            return;
        }

        int need = effectiveMagazineSize(player, spec) - FirearmStackState.getAmmo(gun);
        int moved = PlayerAmmoInventory.transferAmmo(player, ammoItem, need);
        if (moved <= 0) {
            player.displayClientMessage(Component.translatable("gunsrpg.gun.no_ammo_reserve"), true);
            return;
        }
        if (!FirearmStackState.hasLoadedAmmoType(gun)) {
            FirearmStackState.setLoadedMaterial(gun, material);
            FirearmStackState.setLoadedAmmoItem(gun, itemId);
        }
        FirearmStackState.setAmmo(
                gun, Math.min(effectiveMagazineSize(player, spec), FirearmStackState.getAmmo(gun) + moved));
        player.displayClientMessage(reloadDoneMessage(material, AmmoCaliber.fromAmmoItem(ammoItem)), true);
    }

    private static Component reloadDoneMessage(AmmoMaterial material, AmmoCaliber caliber) {
        int dmgPct = AmmoStatsRegistry.damagePercent(material, caliber);
        return Component.translatable(
                "gunsrpg.gun.reload_done_material",
                Component.translatable("gunsrpg.ammo.material." + material.getId()),
                dmgPct);
    }

    private static void finishUnjam(Player player, ItemStack gun) {
        FirearmStackState.setJammed(gun, false);
        player.displayClientMessage(Component.translatable("gunsrpg.gun.unjam_done"), true);
    }

    private static int initialShellReloadTicks(Player player, ItemStack gun, FirearmSpec spec) {
        int shell = shellReloadTicks(player, spec);
        if (FirearmStackState.getAmmo(gun) == 0 && spec.reloadPrepTicks() > 0) {
            shell += prepReloadTicks(player, spec);
        }
        return shell;
    }

    public static int effectiveMagazineSize(Player player, FirearmSpec spec) {
        return WeaponExtensionService.magazineCapacity(player, spec.weaponKey(), spec.magazineSize());
    }

    private static int shellReloadTicks(Player player, FirearmSpec spec) {
        int base = spec.shellReloadTicks() > 0 ? spec.shellReloadTicks() : spec.reloadTicks();
        return Math.max(5, scaledReloadTicks(player, spec.weaponKey(), base));
    }

    private static int prepReloadTicks(Player player, FirearmSpec spec) {
        return Math.max(5, scaledReloadTicks(player, spec.weaponKey(), spec.reloadPrepTicks()));
    }

    private static int reloadTicks(Player player, FirearmSpec spec) {
        return Math.max(5, scaledReloadTicks(player, spec.weaponKey(), spec.reloadTicks()));
    }

    private static int scaledReloadTicks(Player player, String weaponKey, int base) {
        double perk = PerkEffectService.reloadSpeedMultiplier(player);
        double ext = WeaponExtensionService.reloadTimeMultiplier(player, weaponKey);
        return Math.max(5, (int) Math.round(base / perk * ext));
    }

    private static int unjamTicks(Player player, FirearmSpec spec) {
        return Math.max(
                5,
                (int) Math.round(spec.unjamTicks() / PerkEffectService.unjammingSpeedMultiplier(player)));
    }

    public static boolean hasCompatibleAmmoInInventory(Player player, FirearmSpec spec, ItemStack gun) {
        return PlayerAmmoInventory.hasCompatibleAmmo(player, spec, gun);
    }

    public static void syncState(Player player, ItemStack gun) {
        syncState(player, gun, specOf(gun));
    }

    public static void syncState(Player player, ItemStack gun, FirearmSpec spec) {
        if (!(player instanceof ServerPlayer server)) {
            return;
        }
        FirearmStateSyncPacket.send(
                server,
                FirearmStackState.getAmmo(gun),
                effectiveMagazineSize(player, spec),
                FirearmStackState.isJammed(gun),
                FirearmStackState.getFireMode(gun, spec),
                FirearmStackState.getAction(gun),
                FirearmStackState.getActionStart(gun),
                FirearmStackState.getActionEnd(gun),
                player.level().getGameTime());
    }

    private static void sendReloadSounds(Player player, FirearmSpec spec, ItemStack gun, int durationTicks) {
        if (!(player instanceof ServerPlayer server)) {
            return;
        }
        ReloadSoundClip clip =
                spec.reloadStyle() == ReloadStyle.SHELL_BY_SHELL
                        ? ReloadSoundClip.SHELL
                        : ReloadSoundClip.FULL_MAG;
        ReloadSoundPacket.send(server, spec.weaponKey(), clip, durationTicks);
    }
}
