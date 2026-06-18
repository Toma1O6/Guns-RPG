package com.wf.firearms.compat.tacz;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.compat.TaczCompat;
import com.wf.firearms.combat.AmmoCaliber;
import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.combat.FirearmSpec;
import com.wf.firearms.gameplay.WeaponUseGate;
import com.wf.firearms.config.TaczBackendConfig;
import com.wf.firearms.combat.FirearmStackState;
import com.wf.firearms.combat.PlayerAmmoInventory;
import com.wf.firearms.item.AmmoItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * TaCZ 枪 + Guns RPG 材料弹：虚拟备弹数 = 背包内 gunsrpg 子弹数量；无弹时取消换弹；进弹匣后扣除物品。
 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class TaczGunsrpgAmmoBridge {
    public static final String GUNSRPG_MATERIAL_AMMO_TAG = "gunsrpg_material_ammo";
    private static final String DUMMY_AMMO_TAG = "DummyAmmo";
    private static final String MAX_DUMMY_AMMO_TAG = "MaxDummyAmmo";
    private static final String LAST_MAG_TAG = "gunsrpg_last_mag";
    private static final int NO_AMMO_HINT_COOLDOWN_TICKS = 40;
    private static final Map<UUID, Long> LAST_NO_AMMO_HINT_TICK = new ConcurrentHashMap<>();
    /** 服务端换弹开始时记录的弹匣数，换弹结束后结算材料弹消耗。 */
    private static final Map<UUID, Integer> RELOAD_START_MAG = new ConcurrentHashMap<>();

    private TaczGunsrpgAmmoBridge() {}

    public static boolean usesGunsrpgMaterialAmmo(ItemStack gun) {
        return gun != null && !gun.isEmpty() && gun.getOrCreateTag().getBoolean(GUNSRPG_MATERIAL_AMMO_TAG);
    }

    /** 发枪 / 首次持枪时标记并启用虚拟备弹（数量由 tick 同步，不设 9999）。 */
    public static void enableGunsrpgAmmoPool(ItemStack gun) {
        if (gun.isEmpty() || !TaczCompat.isTaczLoaded()) {
            return;
        }
        gun.getOrCreateTag().putBoolean(GUNSRPG_MATERIAL_AMMO_TAG, true);
        TaczBridge.setDummyAmmoAmount(gun, 0);
        TaczBridge.setMaxDummyAmmoAmount(gun, 0);
        gun.getOrCreateTag().putInt(LAST_MAG_TAG, TaczBridge.getCurrentAmmoCount(gun));
        resolveWeaponKey(gun)
                .ifPresent(
                        key ->
                                TaczBridge.applyFireModeFromSpec(
                                        gun, FirearmRegistry.getOrDefault(key)));
    }

    public static Optional<String> resolveWeaponKey(ItemStack gun) {
        return TaczWeaponBinding.read(gun)
                .or(() -> TaczBridge.readGunId(gun).flatMap(TaczWeaponCatalog::weaponKeyForGunId));
    }

    public static int countGunsrpgRounds(Player player, ItemStack gun, FirearmSpec spec) {
        if (spec == null || spec.acceptedAmmo().isEmpty()) {
            return 0;
        }
        if (gun != null
                && !gun.isEmpty()
                && TaczBridge.getCurrentAmmoCount(gun) > 0
                && FirearmStackState.hasLoadedAmmoType(gun)) {
            String loadedId = FirearmStackState.getLoadedAmmoItemId(gun);
            if (loadedId != null && !loadedId.isEmpty()) {
                Item loaded = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(loadedId));
                if (loaded != null && spec.acceptedAmmo().contains(loaded)) {
                    return PlayerAmmoInventory.countItem(player, loaded);
                }
            }
        }
        int total = 0;
        for (Item item : spec.acceptedAmmo()) {
            total += PlayerAmmoInventory.countItem(player, item);
        }
        return total;
    }

    public static int consumeGunsrpgRounds(Player player, ItemStack gun, FirearmSpec spec, int need) {
        if (need <= 0 || spec == null) {
            return 0;
        }
        Item reloadAmmo = PlayerAmmoInventory.resolveReloadAmmo(player, spec, gun);
        if (reloadAmmo == null) {
            return 0;
        }
        int taken = PlayerAmmoInventory.transferAmmo(player, reloadAmmo, need);
        if (taken > 0 && gun != null && !gun.isEmpty()) {
            if (reloadAmmo instanceof AmmoItem ammoItem) {
                recordLoadedAmmoType(gun, ammoItem);
            } else {
                recordLoadedShell(gun, reloadAmmo);
            }
        }
        return taken;
    }

    private static void recordLoadedShell(ItemStack gun, Item shellItem) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(shellItem);
        if (id != null) {
            FirearmStackState.setLoadedAmmoItem(gun, id.toString());
        }
    }

    private static void recordLoadedAmmoType(ItemStack gun, AmmoItem ammoItem) {
        FirearmStackState.setLoadedMaterial(gun, ammoItem.getMaterial());
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(ammoItem);
        if (id != null) {
            FirearmStackState.setLoadedAmmoItem(gun, id.toString());
        }
    }

    /** 同步虚拟备弹（客户端/服务端均需，供 TaCZ 换弹与 HUD 读取）。 */
    public static void syncDummyPool(Player player, ItemStack gun, String weaponKey) {
        if (!usesGunsrpgMaterialAmmo(gun)) {
            return;
        }
        int rounds = countGunsrpgRounds(player, gun, FirearmRegistry.getOrDefault(weaponKey));
        TaczBridge.setDummyAmmoAmount(gun, rounds);
        TaczBridge.setMaxDummyAmmoAmount(gun, rounds);
    }

    static void tickGun(Player player, ItemStack gun, String weaponKey) {
        if (!usesGunsrpgMaterialAmmo(gun)) {
            return;
        }
        if (!gun.getOrCreateTag().getBoolean("gunsrpg_presentation")) {
            TaczGunPresentation.applyToPackGun(gun, weaponKey);
            gun.getOrCreateTag().putBoolean("gunsrpg_presentation", true);
        }
        FirearmSpec spec = FirearmRegistry.getOrDefault(weaponKey);
        syncDummyPool(player, gun, weaponKey);
        TaczBridge.applyFireModeFromSpec(gun, spec);

        Integer reloadStartMag = RELOAD_START_MAG.get(player.getUUID());
        if (reloadStartMag != null) {
            if (!TaczBridge.isReloading(player)) {
                reconcileReloadConsumption(player, gun, weaponKey, reloadStartMag);
                RELOAD_START_MAG.remove(player.getUUID());
            }
        } else {
            gun.getOrCreateTag().putInt(LAST_MAG_TAG, TaczBridge.getCurrentAmmoCount(gun));
        }
        syncDummyPool(player, gun, weaponKey);
        if (TaczBridge.isReloading(player)) {
            TaczBridge.accelerateReload(
                    player, TaczGunStatSync.reloadTimeMultiplier(player, gun, weaponKey));
        }
    }

    private static void reconcileReloadConsumption(
            Player player, ItemStack gun, String weaponKey, int startMag) {
        int mag = TaczBridge.getCurrentAmmoCount(gun);
        if (mag > startMag) {
            FirearmSpec spec = FirearmRegistry.getOrDefault(weaponKey);
            int delta = mag - startMag;
            int consumed = consumeGunsrpgRounds(player, gun, spec, delta);
            if (consumed < delta) {
                TaczBridge.setCurrentAmmoCount(gun, startMag + consumed);
                mag = startMag + consumed;
            }
        }
        int cap = TaczPerkBridge.magazineCap(gun);
        if (cap > 0 && TaczBridge.getCurrentAmmoCount(gun) > cap) {
            TaczBridge.setCurrentAmmoCount(gun, cap);
            mag = cap;
        }
        gun.getOrCreateTag().putInt(LAST_MAG_TAG, TaczBridge.getCurrentAmmoCount(gun));
        syncDummyPool(player, gun, weaponKey);
    }

    public static void registerReloadListener() {
        if (!TaczCompat.isTaczLoaded()) {
            return;
        }
        try {
            Class<?> reloadClass = Class.forName("com.tacz.guns.api.event.common.GunReloadEvent");
            if (!Event.class.isAssignableFrom(reloadClass)) {
                return;
            }
            @SuppressWarnings("unchecked")
            Class<? extends Event> eventClass = (Class<? extends Event>) reloadClass;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(
                    EventPriority.HIGH,
                    false,
                    eventClass,
                    TaczGunsrpgAmmoBridge::onGunReload);
            GunsRpg.LOGGER.info("[gunsrpg] 已订阅 TaCZ GunReloadEvent（材料弹校验）");
        } catch (ClassNotFoundException ex) {
            GunsRpg.LOGGER.warn("[gunsrpg] 无法注册 TaCZ 换弹校验", ex);
        }
    }

    private static void onGunReload(Event event) {
        Player player = readPlayer(event);
        ItemStack gun = readGunStack(event);
        if (player == null || gun.isEmpty() || !usesGunsrpgMaterialAmmo(gun)) {
            return;
        }
        String key = resolveWeaponKey(gun).orElse(null);
        if (key == null) {
            return;
        }
        FirearmSpec spec = FirearmRegistry.getOrDefault(key);
        if (!TaczBridge.readLogicalSide(event).isClient()) {
            int startMag = TaczBridge.getCurrentAmmoCount(gun);
            gun.getOrCreateTag().putInt(LAST_MAG_TAG, startMag);
            RELOAD_START_MAG.put(player.getUUID(), startMag);
            syncDummyPool(player, gun, key);
            if (TaczPerkBridge.isJammed(gun)) {
                TaczPerkBridge.clearJam(gun);
            }
        }
        if (countGunsrpgRounds(player, gun, spec) <= 0) {
            event.setCanceled(true);
            hintNoAmmoInInventory(player, spec);
        }
    }

    private static void hintNoAmmoInInventory(Player player, FirearmSpec spec) {
        long now = player.level().getGameTime();
        Long last = LAST_NO_AMMO_HINT_TICK.get(player.getUUID());
        if (last != null && now - last < NO_AMMO_HINT_COOLDOWN_TICKS) {
            return;
        }
        LAST_NO_AMMO_HINT_TICK.put(player.getUUID(), now);
        player.displayClientMessage(noAmmoMessage(spec), true);
    }

    private static Component noAmmoMessage(FirearmSpec spec) {
        LinkedHashSet<AmmoCaliber> calibers = new LinkedHashSet<>();
        if (spec != null) {
            for (Item item : spec.acceptedAmmo()) {
                if (item instanceof AmmoItem ammoItem) {
                    calibers.add(ammoItem.getCaliber());
                }
            }
        }
        if (calibers.size() == 1) {
            AmmoCaliber caliber = calibers.iterator().next();
            return Component.translatable(
                    "gunsrpg.tacz.reload_need_caliber",
                    Component.translatable("gunsrpg.caliber." + caliber.suffix()));
        }
        return Component.translatable("gunsrpg.gun.no_ammo_reserve");
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side != LogicalSide.SERVER
                || event.phase != TickEvent.Phase.END
                || !TaczBackendConfig.useTaczShooting()) {
            return;
        }
        Player player = event.player;
        for (ItemStack hand : List.of(player.getMainHandItem(), player.getOffhandItem())) {
            if (!TaczBridge.isTaczGun(hand)) {
                continue;
            }
            if (!usesGunsrpgMaterialAmmo(hand)) {
                Optional<String> key = resolveWeaponKey(hand);
                if (key.isPresent()) {
                    TaczWeaponBinding.bind(hand, key.get());
                    enableGunsrpgAmmoPool(hand);
                } else {
                    continue;
                }
            }
            resolveWeaponKey(hand)
                    .ifPresent(
                            key -> {
                                TaczBridge.setAttachmentLock(
                                        hand, !WeaponUseGate.hasAssemblyUnlocked(player, key));
                                TaczPerkBridge.syncHeldGun(player, hand, key);
                                tickGun(player, hand, key);
                            });
        }
    }

    private static Player readPlayer(Event event) {
        try {
            Object entity = event.getClass().getMethod("getEntity").invoke(event);
            if (entity instanceof Player player) {
                return player;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return null;
    }

    private static ItemStack readGunStack(Event event) {
        try {
            Object stack = event.getClass().getMethod("getGunItemStack").invoke(event);
            if (stack instanceof ItemStack itemStack) {
                return itemStack;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return ItemStack.EMPTY;
    }
}
