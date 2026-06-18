package com.wf.firearms.compat.tacz;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.compat.TaczCompat;
import com.wf.firearms.combat.FirearmCombat;
import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.combat.FirearmSpec;
import com.wf.firearms.combat.FirearmStackState;
import com.wf.firearms.combat.LauncherShooter;
import com.wf.firearms.combat.WeaponBalance;
import com.wf.firearms.combat.WeaponWearService;
import com.wf.firearms.combat.WeaponWearState;
import com.wf.firearms.config.TaczBackendConfig;
import com.wf.firearms.gameplay.GunshotAlertService;
import com.wf.firearms.gameplay.PerkEffectService;
import com.wf.firearms.gameplay.WeaponExtensionService;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import java.util.Map;

/**
 * TaCZ 模式下：武器扩展天赋、天赋（换弹/散布/射速）、CGM 附魔、磨损与弹匣上限。
 */
public final class TaczPerkBridge {
    private static final String ATTACHMENT_PROPERTY = "com.tacz.guns.api.event.common.AttachmentPropertyEvent";
    private static final String GUN_FIRE = "com.tacz.guns.api.event.common.GunFireEvent";
    private static final String GUN_RELOAD = "com.tacz.guns.api.event.common.GunReloadEvent";

    private static final String NBT_OWNER = "gunsrpg_tacz_sync_owner";
    private static final String NBT_MAG_CAP = "gunsrpg_tacz_mag_cap";
    private static final String NBT_RPM_MULT = "gunsrpg_tacz_rpm_mult";
    private static final String NBT_SPREAD_MULT = "gunsrpg_tacz_spread_mult";
    private static final String NBT_RECOIL_MULT = "gunsrpg_tacz_recoil_mult";
    private static final String NBT_HEADSHOT_EXT = "gunsrpg_tacz_headshot_ext";
    private static final String NBT_JAM_CHANCE = "gunsrpg_tacz_jam_chance";
    private static final String NBT_JAMMED = "gunsrpg_tacz_jammed";
    private static final String NBT_PIERCE_BONUS = "gunsrpg_tacz_pierce_bonus";

    private TaczPerkBridge() {}

    public static void registerListeners() {
        if (!TaczCompat.isTaczLoaded() || !TaczBackendConfig.useTaczShooting()) {
            return;
        }
        registerAttachmentPropertyListener();
        registerGunFireListener();
        registerReloadClampListener();
        GunsRpg.LOGGER.info("[gunsrpg] 已订阅 TaCZ 天赋/扩展/附魔/磨损桥接");
    }

    /** 持枪 tick 时写入同步参数（供 AttachmentPropertyEvent 读取）。 */
    public static void syncHeldGun(Player player, ItemStack gun, String weaponKey) {
        if (player == null || gun.isEmpty() || weaponKey == null || !TaczGunsrpgAmmoBridge.usesGunsrpgMaterialAmmo(gun)) {
            return;
        }
        TaczGunEnchantBridge.refreshFromItemEnchantments(gun);
        FirearmSpec spec = FirearmRegistry.getOrDefault(weaponKey);
        WeaponExtensionService.WeaponExtensionStats ext = WeaponExtensionService.stats(player, weaponKey);

        ResourceLocation gunId = TaczWeaponCatalog.gunIdForWeapon(weaponKey).orElse(null);
        int taczBaseMag = gunId != null ? TaczBridge.defaultMagazineCapacity(gunId) : spec.magazineSize();
        int magCap =
                (int)
                        (WeaponExtensionService.magazineCapacity(player, weaponKey, taczBaseMag)
                                + TaczGunEnchantBridge.magBonus(gun));
        magCap = Math.max(1, magCap);

        double rpmMult = ext.fireIntervalMult() * TaczGunEnchantBridge.rpmMultiplier(gun);
        int interval =
                Math.max(
                        1,
                        (int)
                                Math.round(
                                        spec.fireIntervalTicks() * ext.fireIntervalMult()
                                                + WeaponExtensionService.contextualFireIntervalDelta(
                                                        player, gun, spec)));
        if (spec.fireIntervalTicks() > 0 && interval > 0) {
            rpmMult *= (double) spec.fireIntervalTicks() / interval;
        }
        int taczRpm = TaczBridge.getRoundsPerMinute(gun);
        if (taczRpm > 0 && spec.fireIntervalTicks() > 0) {
            int targetRpm = (int) Math.max(1L, Math.round(1200.0 / interval));
            rpmMult *= (double) targetRpm / taczRpm;
        }

        boolean automatic = FirearmCombat.isAutomaticMode(gun, spec);
        float weaponSpreadMult = WeaponBalance.spreadMultiplier(weaponKey, automatic);
        float weaponRecoilMult = WeaponBalance.recoilMultiplier(weaponKey, automatic);
        float spreadMult =
                (float)
                        (ext.spreadMult()
                                * PerkEffectService.recoilMultiplier(player)
                                * weaponSpreadMult
                                * (1.0 / TaczGunEnchantBridge.lightweightAdsFactor(gun)));
        float recoilMult =
                (float)
                        (ext.recoilMult()
                                * PerkEffectService.recoilMultiplier(player)
                                * weaponRecoilMult);
        recoilMult *= com.wf.firearms.gameplay.WeaponExtensionCombat.recoilMultiplier(player, weaponKey);

        CompoundTag tag = gun.getOrCreateTag();
        tag.putInt(NBT_OWNER, player.getId());
        tag.putInt(NBT_MAG_CAP, magCap);
        tag.putFloat(NBT_RPM_MULT, (float) rpmMult);
        tag.putFloat(NBT_SPREAD_MULT, Math.max(0.1f, spreadMult));
        tag.putFloat(NBT_RECOIL_MULT, Math.max(0.1f, recoilMult));
        tag.putFloat(NBT_HEADSHOT_EXT, (float) WeaponExtensionService.headshotBonusMultiplier(player, weaponKey));
        tag.putFloat(
                NBT_JAM_CHANCE,
                (float)
                        ((spec.jamChancePerShot()
                                        + FirearmStackState.loadedAmmoStats(gun, spec).jamChanceAdd())
                                * ext.jamMult()
                                * com.wf.firearms.gameplay.WeaponExtensionCombat.extraJamChanceMultiplier(
                                        player, weaponKey)
                                * PerkEffectService.jamChanceMultiplier(player, weaponKey)));
        tag.putInt(NBT_PIERCE_BONUS, ext.pierceBonus());

        int current = TaczBridge.getCurrentAmmoCount(gun);
        if (current > magCap) {
            TaczBridge.setCurrentAmmoCount(gun, magCap);
        }
        TaczGunStatSync.syncDisplayDamage(player, gun, weaponKey);
    }

    public static int magazineCap(ItemStack gun) {
        return gun.getOrCreateTag().getInt(NBT_MAG_CAP);
    }

    public static boolean isJammed(ItemStack gun) {
        return gun.getOrCreateTag().getBoolean(NBT_JAMMED);
    }

    public static void clearJam(ItemStack gun) {

        gun.getOrCreateTag().remove(NBT_JAMMED);

    }



    private static void registerAttachmentPropertyListener() {

        try {

            Class<?> raw = Class.forName(ATTACHMENT_PROPERTY);

            if (!Event.class.isAssignableFrom(raw)) {

                return;

            }

            @SuppressWarnings("unchecked")

            Class<? extends Event> eventClass = (Class<? extends Event>) raw;

            net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(

                    EventPriority.LOW,

                    false,

                    eventClass,

                    TaczPerkBridge::onAttachmentProperty);

        } catch (ClassNotFoundException ex) {

            GunsRpg.LOGGER.warn("[gunsrpg] 无法注册 TaCZ AttachmentPropertyEvent", ex);

        }

    }



    private static void registerGunFireListener() {

        try {

            Class<?> raw = Class.forName(GUN_FIRE);

            if (!Event.class.isAssignableFrom(raw)) {

                return;

            }

            @SuppressWarnings("unchecked")

            Class<? extends Event> eventClass = (Class<? extends Event>) raw;

            net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(

                    EventPriority.NORMAL, false, eventClass, TaczPerkBridge::onGunFire);

        } catch (ClassNotFoundException ex) {

            GunsRpg.LOGGER.warn("[gunsrpg] 无法注册 TaCZ GunFireEvent", ex);

        }

    }



    private static void registerReloadClampListener() {

        try {

            Class<?> raw = Class.forName(GUN_RELOAD);

            if (!Event.class.isAssignableFrom(raw)) {

                return;

            }

            @SuppressWarnings("unchecked")

            Class<? extends Event> eventClass = (Class<? extends Event>) raw;

            net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(

                    EventPriority.LOW,

                    false,

                    eventClass,

                    TaczPerkBridge::onGunReloadEnd);

        } catch (ClassNotFoundException ex) {

            GunsRpg.LOGGER.warn("[gunsrpg] 无法注册 TaCZ 换弹钳制", ex);

        }

    }



    private static void onAttachmentProperty(Event event) {

        ItemStack gun = readGunItem(event);

        if (gun.isEmpty() || !TaczGunsrpgAmmoBridge.usesGunsrpgMaterialAmmo(gun)) {

            return;

        }

        CompoundTag tag = gun.getTag();

        if (tag == null || !tag.contains(NBT_MAG_CAP)) {

            return;

        }

        Object cache = readCacheProperty(event);

        if (cache == null) {

            return;

        }

        applyCacheMultipliers(cache, tag);
        applyPelletCountCache(cache, tag, gun);

        applyCgmEnchantCache(cache, gun, tag);

        applyDisplayDamageCache(cache, tag, gun);

    }



    private static void applyPelletCountCache(Object cacheProperty, CompoundTag tag, ItemStack gun) {
        String weaponKey = TaczGunsrpgAmmoBridge.resolveWeaponKey(gun).orElse(null);
        if (weaponKey == null || !tag.contains(NBT_OWNER)) {
            return;
        }
        try {
            Object cur =
                    cacheProperty.getClass().getMethod("getCache", String.class).invoke(cacheProperty, "bullet_amount");
            int base = cur instanceof Integer v ? v : 0;
            if (base <= 0) {
                return;
            }
            net.minecraft.server.MinecraftServer server =
                    net.minecraftforge.server.ServerLifecycleHooks.getCurrentServer();
            if (server == null) {
                return;
            }
            net.minecraft.world.entity.Entity entity = null;
            for (var level : server.getAllLevels()) {
                entity = level.getEntity(tag.getInt(NBT_OWNER));
                if (entity != null) {
                    break;
                }
            }
            if (!(entity instanceof Player player)) {
                return;
            }
            int pellets = com.wf.firearms.gameplay.WeaponExtensionCombat.pelletCount(player, weaponKey, base);
            if (pellets != base) {
                setCacheById(cacheProperty, "bullet_amount", pellets);
            }
        } catch (ReflectiveOperationException ignored) {
        }
    }

    private static void applyCacheMultipliers(Object cacheProperty, CompoundTag tag) {

        float rpmMult = tag.getFloat(NBT_RPM_MULT);

        if (rpmMult > 0 && rpmMult != 1f) {

            scaleIntCache(cacheProperty, "rpm", rpmMult);

        }

        float spreadMult = tag.getFloat(NBT_SPREAD_MULT);

        if (spreadMult > 0 && spreadMult != 1f) {

            scaleInaccuracyMap(cacheProperty, spreadMult);

        }

        float recoilMult = tag.getFloat(NBT_RECOIL_MULT);

        if (recoilMult > 0 && recoilMult != 1f) {

            scaleRecoilPair(cacheProperty, recoilMult);

        }

    }



    private static void applyDisplayDamageCache(Object cacheProperty, CompoundTag tag, ItemStack gun) {
        if (tag == null || !tag.contains(TaczGunStatSync.NBT_DISPLAY_DAMAGE)) {
            return;
        }
        String weaponKey = TaczGunsrpgAmmoBridge.resolveWeaponKey(gun).orElse(null);
        if (weaponKey == null) {
            return;
        }
        float target = tag.getFloat(TaczGunStatSync.NBT_DISPLAY_DAMAGE);
        FirearmSpec spec = FirearmRegistry.getOrDefault(weaponKey);
        scaleDamageListCache(cacheProperty, target, spec.baseDamage());
    }

    @SuppressWarnings("unchecked")
    private static void scaleDamageListCache(Object cacheProperty, float targetDamage, float fallbackBase) {
        try {
            Object listObj =
                    cacheProperty.getClass().getMethod("getCache", String.class).invoke(cacheProperty, "damage");
            if (!(listObj instanceof java.util.LinkedList<?> list) || list.isEmpty()) {
                return;
            }
            Object first = list.getFirst();
            float taczBase = (float) first.getClass().getMethod("getDamage").invoke(first);
            if (taczBase <= 0.01f) {
                taczBase = fallbackBase;
            }
            if (taczBase <= 0.01f) {
                return;
            }
            float mult = targetDamage / taczBase;
            Class<?> pairClass =
                    Class.forName("com.tacz.guns.resource.pojo.data.gun.ExtraDamage$DistanceDamagePair");
            java.util.LinkedList<Object> scaled = new java.util.LinkedList<>();
            for (Object pair : list) {
                float distance = (float) pair.getClass().getMethod("getDistance").invoke(pair);
                float damage = (float) pair.getClass().getMethod("getDamage").invoke(pair);
                scaled.add(pairClass.getConstructor(float.class, float.class).newInstance(distance, damage * mult));
            }
            setCacheById(cacheProperty, "damage", scaled);
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.debug("[gunsrpg] 同步 TaCZ 展示伤害失败", ex);
        }
    }

    private static void applyCgmEnchantCache(Object cacheProperty, ItemStack gun, CompoundTag tag) {

        float speed = TaczGunEnchantBridge.ammoSpeedMultiplier(gun);

        if (speed != 1f) {

            scaleFloatCache(cacheProperty, "ammo_speed", speed);

        }

        int pierce = TaczGunEnchantBridge.pierceBonus(gun);
        if (tag != null) {
            pierce += tag.getInt(NBT_PIERCE_BONUS);
        }

        if (pierce > 0) {

            addIntCache(cacheProperty, "pierce", pierce);

        }

    }



    private static void onGunFire(Event event) {

        if (TaczBridge.readLogicalSide(event).isClient()) {

            return;

        }

        LivingEntity shooter = readShooter(event);

        if (!(shooter instanceof Player player)) {

            return;

        }

        ItemStack gun = readGunStack(event);

        if (!TaczBridge.isTaczGun(gun) || !TaczGunsrpgAmmoBridge.usesGunsrpgMaterialAmmo(gun)) {

            return;

        }

        String weaponKey = TaczGunsrpgAmmoBridge.resolveWeaponKey(gun).orElse(null);

        if (weaponKey == null) {

            return;

        }

        if (WeaponWearState.isDestroyed(gun, weaponKey)) {

            event.setCanceled(true);

            player.displayClientMessage(Component.translatable("gunsrpg.gun.wear_destroyed"), true);

            return;

        }

        if (isJammed(gun)) {

            event.setCanceled(true);

            player.displayClientMessage(Component.translatable("gunsrpg.gun.jammed_notice"), true);

            return;

        }

        if (TaczUnjamBridge.isUnjamming(player)) {

            event.setCanceled(true);

            return;

        }

        float jamChance = gun.getOrCreateTag().getFloat(NBT_JAM_CHANCE);

        if (jamChance > 0f && player.getRandom().nextFloat() < jamChance) {

            gun.getOrCreateTag().putBoolean(NBT_JAMMED, true);

            event.setCanceled(true);

            player.displayClientMessage(Component.translatable("gunsrpg.gun.jammed_notice"), true);

            return;

        }

        FirearmSpec spec = FirearmRegistry.getOrDefault(weaponKey);

        if (LauncherShooter.isLauncher(weaponKey)) {
            LauncherShooter.fireTacz(player, gun, spec);
        }

        WeaponWearService.applyShotWear(

                player, gun, weaponKey, FirearmStackState.loadedAmmoStats(gun, spec));

        GunshotAlertService.onGunFire(player, weaponKey, spec);
        com.wf.firearms.gameplay.WeaponExtensionCombat.onGunFire(player, weaponKey);

        float reclaim =
                TaczGunEnchantBridge.reclaimedChance(gun)
                        + WeaponExtensionService.ammoReclaimChance(player, weaponKey);
        reclaim = Math.min(1f, reclaim);

        if (reclaim > 0f && player.getRandom().nextFloat() < reclaim) {

            int cur = TaczBridge.getCurrentAmmoCount(gun);

            TaczBridge.setCurrentAmmoCount(gun, cur + 1);

            gun.getOrCreateTag().putInt("gunsrpg_last_mag", TaczBridge.getCurrentAmmoCount(gun));

        }

        float heatMult = WeaponExtensionService.heatPerShotMultiplier(player, weaponKey);
        if (heatMult < 0.999f) {
            float perShot = TaczBridge.heatPerShot(gun);
            if (perShot > 0f) {
                float heat = TaczBridge.getHeatAmount(gun);
                TaczBridge.setHeatAmount(gun, Math.max(0f, heat - perShot * (1f - heatMult)));
            }
        }

    }



    private static void onGunReloadEnd(Event event) {

        if (TaczBridge.readLogicalSide(event).isClient()) {

            return;

        }

        Player player = readReloadPlayer(event);

        ItemStack gun = readReloadGun(event);

        if (player == null || gun.isEmpty() || !TaczGunsrpgAmmoBridge.usesGunsrpgMaterialAmmo(gun)) {

            return;

        }

        int cap = magazineCap(gun);

        if (cap > 0 && TaczBridge.getCurrentAmmoCount(gun) > cap) {

            TaczBridge.setCurrentAmmoCount(gun, cap);

        }

    }



    static float resolveHeadshotMultiplier(Event hurtPre, Player player, ItemStack gun, String weaponKey) {

        float base = readHeadshotMultiplier(hurtPre);

        float factor = PerkEffectService.taczHeadshotMultiplierFactor(player);

        float ext = gun != null && gun.hasTag() ? gun.getTag().getFloat(NBT_HEADSHOT_EXT) : 1f;

        if (ext <= 0f) {

            ext = (float) WeaponExtensionService.headshotBonusMultiplier(player, weaponKey);

        }

        return Math.max(0.1f, base * factor * ext);

    }



    static float applyPuncturingCrit(ItemStack gun, float baseDamage, RandomSource random) {
        float chance = TaczGunEnchantBridge.puncturingCritChance(gun);
        if (chance > 0f && random.nextFloat() < chance) {
            return baseDamage * 1.5f;
        }
        return baseDamage;
    }

    private static void scaleIntCache(Object cacheProperty, String id, float mult) {
        try {
            Integer cur =
                    (Integer)
                            cacheProperty
                                    .getClass()
                                    .getMethod("getCache", String.class)
                                    .invoke(cacheProperty, id);
            if (cur != null && cur > 0) {
                Object cacheValue =
                        cacheProperty
                                .getClass()
                                .getMethod("getCache", String.class)
                                .invoke(cacheProperty, id);
                if (cacheValue instanceof Integer v) {
                    int next = Math.max(1, Math.round(v * mult));
                    setCacheById(cacheProperty, id, next);
                }
            }
        } catch (ReflectiveOperationException ex) {
            trySetGunPropertyCache(cacheProperty, id, Integer.class, mult);
        }
    }

    private static void trySetGunPropertyCache(Object cacheProperty, String id, Class<?> type, float mult) {
        try {
            Object cur = cacheProperty.getClass().getMethod("getCache", String.class).invoke(cacheProperty, id);
            if (cur instanceof Integer v) {
                setCacheById(cacheProperty, id, Math.max(1, Math.round(v * mult)));
            } else if (cur instanceof Float f) {
                setCacheById(cacheProperty, id, f * mult);
            }
        } catch (ReflectiveOperationException ignored) {
        }
    }

    private static void setCacheById(Object cacheProperty, String id, Object value) {
        try {
            Class<?> gunProps = Class.forName("com.tacz.guns.api.GunProperties");
            Object prop = gunProps.getField(id.toUpperCase()).get(null);
            cacheProperty
                    .getClass()
                    .getMethod("setCache", prop.getClass(), Object.class)
                    .invoke(cacheProperty, prop, value);
        } catch (ReflectiveOperationException e1) {
            try {
                for (java.lang.reflect.Field f : Class.forName("com.tacz.guns.api.GunProperties").getFields()) {
                    if (!java.lang.reflect.Modifier.isStatic(f.getModifiers())) {
                        continue;
                    }
                    Object prop = f.get(null);
                    if (prop != null && prop.getClass().getMethod("name").invoke(prop).equals(id)) {
                        cacheProperty
                                .getClass()
                                .getMethod("setCache", prop.getClass(), Object.class)
                                .invoke(cacheProperty, prop, value);
                        return;
                    }
                }
            } catch (ReflectiveOperationException ignored) {
            }
        }
    }

    private static void scaleFloatCache(Object cacheProperty, String id, float mult) {
        try {
            Object cur = cacheProperty.getClass().getMethod("getCache", String.class).invoke(cacheProperty, id);
            if (cur instanceof Float f) {
                setCacheById(cacheProperty, id, f * mult);
            }
        } catch (ReflectiveOperationException ignored) {
        }
    }

    private static void addIntCache(Object cacheProperty, String id, int add) {
        try {
            Object cur = cacheProperty.getClass().getMethod("getCache", String.class).invoke(cacheProperty, id);
            int base = cur instanceof Integer v ? v : 0;
            setCacheById(cacheProperty, id, base + add);
        } catch (ReflectiveOperationException ignored) {
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void scaleInaccuracyMap(Object cacheProperty, float mult) {
        try {
            Map map =
                    (Map)
                            cacheProperty
                                    .getClass()
                                    .getMethod("getCache", String.class)
                                    .invoke(cacheProperty, "inaccuracy");
            if (map == null || map.isEmpty()) {
                return;
            }
            for (Object key : map.keySet()) {
                Object value = map.get(key);
                if (value instanceof Float f) {
                    map.put(key, f * mult);
                }
            }
            setCacheById(cacheProperty, "inaccuracy", map);
        } catch (ReflectiveOperationException ignored) {
        }
    }

    private static void scaleRecoilPair(Object cacheProperty, float mult) {
        try {
            Object pair =
                    cacheProperty.getClass().getMethod("getCache", String.class).invoke(cacheProperty, "recoil");
            if (pair == null) {
                return;
            }
            Object left = pair.getClass().getMethod("getLeft").invoke(pair);
            Object right = pair.getClass().getMethod("getRight").invoke(pair);
            if (left instanceof Float l) {
                pair.getClass().getMethod("setLeft", Float.class).invoke(pair, l * mult);
            }
            if (right instanceof Float r) {
                pair.getClass().getMethod("setRight", Float.class).invoke(pair, r * mult);
            }
            setCacheById(cacheProperty, "recoil", pair);
        } catch (ReflectiveOperationException ignored) {
        }
    }

    private static float readHeadshotMultiplier(Event event) {
        try {
            Object v = event.getClass().getMethod("getHeadshotMultiplier").invoke(event);
            if (v instanceof Float f) {
                return f;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return 1f;
    }

    private static ItemStack readGunItem(Event event) {
        try {
            Object stack = event.getClass().getMethod("getGunItem").invoke(event);
            if (stack instanceof ItemStack itemStack) {
                return itemStack;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return ItemStack.EMPTY;
    }

    private static Object readCacheProperty(Event event) {
        try {
            return event.getClass().getMethod("getCacheProperty").invoke(event);
        } catch (ReflectiveOperationException ignored) {
        }
        return null;
    }

    private static LivingEntity readShooter(Event event) {
        try {
            Object s = event.getClass().getMethod("getShooter").invoke(event);
            if (s instanceof LivingEntity living) {
                return living;
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