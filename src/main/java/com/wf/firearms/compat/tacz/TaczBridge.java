package com.wf.firearms.compat.tacz;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.compat.TaczCompat;
import com.wf.firearms.combat.FirearmMode;
import com.wf.firearms.combat.FirearmSpec;
import com.wf.firearms.combat.FirearmStackState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.LogicalSide;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 反射调用 TaCZ API，避免 gunsrpg 编译期硬依赖 tacz jar。
 */
public final class TaczBridge {
    private static final String GUN_BUILDER = "com.tacz.guns.api.item.builder.GunItemBuilder";
    private static final String AMMO_BUILDER = "com.tacz.guns.api.item.builder.AmmoItemBuilder";
    private static final String IGUN = "com.tacz.guns.api.item.IGun";
    private static final String TIMELESS_API = "com.tacz.guns.api.TimelessAPI";
    private static final String KILL_EVENT = "com.tacz.guns.api.event.common.EntityKillByGunEvent";

    private TaczBridge() {}

    public static void setCurrentAmmoCount(ItemStack gun, int count) {
        if (gun.isEmpty()) {
            return;
        }
        try {
            Method set = gun.getItem().getClass().getMethod("setCurrentAmmoCount", ItemStack.class, int.class);
            set.invoke(gun.getItem(), gun, Math.max(0, count));
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.debug("[gunsrpg] 设置 TaCZ 弹匣数失败", ex);
        }
    }

    public static void registerKillListener() {
        if (!TaczCompat.isTaczLoaded()) {
            return;
        }
        try {
            Class<?> raw = Class.forName(KILL_EVENT);
            if (!Event.class.isAssignableFrom(raw)) {
                GunsRpg.LOGGER.warn("[gunsrpg] TaCZ 击杀事件类型不继承 Event: {}", KILL_EVENT);
                return;
            }
            @SuppressWarnings("unchecked")
            Class<? extends Event> eventClass = (Class<? extends Event>) raw;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(
                    EventPriority.NORMAL,
                    false,
                    eventClass,
                    TaczKillBridge::onEntityKillByGun);
            GunsRpg.LOGGER.info("[gunsrpg] 已订阅 TaCZ EntityKillByGunEvent");
        } catch (ClassNotFoundException ex) {
            GunsRpg.LOGGER.warn("[gunsrpg] 无法注册 TaCZ 击杀事件（TaCZ 未安装或版本不匹配）");
        }
    }

    public static boolean isTaczGun(ItemStack stack) {
        if (stack.isEmpty() || !TaczCompat.isTaczLoaded()) {
            return false;
        }
        try {
            Class<?> iGun = Class.forName(IGUN);
            return iGun.isInstance(stack.getItem());
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }

    /** 未解锁装配天赋时锁定 TaCZ Z 键改装界面与配件装卸。 */
    public static void setAttachmentLock(ItemStack gun, boolean locked) {
        if (gun.isEmpty() || !isTaczGun(gun)) {
            return;
        }
        try {
            Class<?> iGunClass = Class.forName(IGUN);
            Method getIGun = iGunClass.getMethod("getIGunOrNull", ItemStack.class);
            Object iGun = getIGun.invoke(null, gun);
            if (iGun == null) {
                return;
            }
            Method setLock = iGunClass.getMethod("setAttachmentLock", ItemStack.class, boolean.class);
            setLock.invoke(iGun, gun, locked);
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.debug("[gunsrpg] 设置 TaCZ AttachmentLock 失败", ex);
        }
    }

    public static boolean hasAttachmentLock(ItemStack gun) {
        if (gun.isEmpty() || !isTaczGun(gun)) {
            return false;
        }
        try {
            Class<?> iGunClass = Class.forName(IGUN);
            Method getIGun = iGunClass.getMethod("getIGunOrNull", ItemStack.class);
            Object iGun = getIGun.invoke(null, gun);
            if (iGun == null) {
                return false;
            }
            Method hasLock = iGunClass.getMethod("hasAttachmentLock", ItemStack.class);
            Object result = hasLock.invoke(iGun, gun);
            return result instanceof Boolean b && b;
        } catch (ReflectiveOperationException ex) {
            return false;
        }
    }

    public static Optional<ResourceLocation> readGunId(ItemStack stack) {
        if (!isTaczGun(stack)) {
            return Optional.empty();
        }
        try {
            Object iGun = stack.getItem();
            Method getGunId = iGun.getClass().getMethod("getGunId", ItemStack.class);
            Object result = getGunId.invoke(iGun, stack);
            if (result instanceof ResourceLocation loc) {
                return Optional.of(loc);
            }
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.debug("[gunsrpg] 读取 TaCZ GunId 失败", ex);
        }
        return Optional.empty();
    }

    public static ItemStack buildGun(ResourceLocation gunId, int ammoCount) {
        if (!TaczCompat.isTaczLoaded() || gunId == null) {
            return ItemStack.EMPTY;
        }
        try {
            Class<?> builderClass = Class.forName(GUN_BUILDER);
            Method create = builderClass.getMethod("create");
            Object builder = create.invoke(null);
            builderClass.getMethod("setId", ResourceLocation.class).invoke(builder, gunId);
            if (ammoCount >= 0) {
                builderClass.getMethod("setAmmoCount", int.class).invoke(builder, ammoCount);
            }
            Object stack = builderClass.getMethod("build").invoke(builder);
            if (stack instanceof ItemStack itemStack) {
                return itemStack;
            }
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.warn("[gunsrpg] 构建 TaCZ 枪 {} 失败", gunId, ex);
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack buildAmmo(ResourceLocation ammoId, int count) {
        if (!TaczCompat.isTaczLoaded() || ammoId == null) {
            return ItemStack.EMPTY;
        }
        try {
            Class<?> builderClass = Class.forName(AMMO_BUILDER);
            Object builder = builderClass.getMethod("create").invoke(null);
            builderClass.getMethod("setId", ResourceLocation.class).invoke(builder, ammoId);
            builderClass.getMethod("setCount", int.class).invoke(builder, count);
            Object stack = builderClass.getMethod("build").invoke(builder);
            if (stack instanceof ItemStack itemStack) {
                return itemStack;
            }
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.warn("[gunsrpg] 构建 TaCZ 弹药 {} 失败", ammoId, ex);
        }
        return ItemStack.EMPTY;
    }

    public static int defaultMagazineCapacity(ResourceLocation gunId) {
        if (!TaczCompat.isTaczLoaded() || gunId == null) {
            return 0;
        }
        try {
            Class<?> api = Class.forName(TIMELESS_API);
            Method getIndex = api.getMethod("getCommonGunIndex", ResourceLocation.class);
            Object optional = getIndex.invoke(null, gunId);
            if (!(optional instanceof Optional<?> opt) || opt.isEmpty()) {
                return 0;
            }
            Object index = opt.get();
            Method getGunData = index.getClass().getMethod("getGunData");
            Object gunData = getGunData.invoke(index);
            Method getAmmoAmount = gunData.getClass().getMethod("getAmmoAmount");
            Object amount = getAmmoAmount.invoke(gunData);
            if (amount instanceof Number n) {
                return n.intValue();
            }
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.debug("[gunsrpg] 读取 TaCZ 弹匣容量 {} 失败", gunId, ex);
        }
        return 0;
    }

    private static final String DUMMY_AMMO_TAG = "DummyAmmo";
    private static final String MAX_DUMMY_AMMO_TAG = "MaxDummyAmmo";

    /** @deprecated 使用 {@link TaczGunsrpgAmmoBridge#enableGunsrpgAmmoPool} */
    @Deprecated
    public static void enableDummyAmmoPool(ItemStack gun, int maxPool) {
        TaczGunsrpgAmmoBridge.enableGunsrpgAmmoPool(gun);
    }

    public static void setDummyAmmoAmount(ItemStack gun, int amount) {
        if (gun.isEmpty()) {
            return;
        }
        Object iGun = gun.getItem();
        try {
            Method setDummy = iGun.getClass().getMethod("setDummyAmmoAmount", ItemStack.class, int.class);
            setDummy.invoke(iGun, gun, Math.max(0, amount));
        } catch (ReflectiveOperationException ex) {
            gun.getOrCreateTag().putInt(DUMMY_AMMO_TAG, Math.max(0, amount));
        }
    }

    public static void setMaxDummyAmmoAmount(ItemStack gun, int amount) {
        if (gun.isEmpty()) {
            return;
        }
        Object iGun = gun.getItem();
        try {
            Method setMax =
                    iGun.getClass().getMethod("setMaxDummyAmmoAmount", ItemStack.class, int.class);
            setMax.invoke(iGun, gun, Math.max(0, amount));
        } catch (ReflectiveOperationException ex) {
            gun.getOrCreateTag().putInt(MAX_DUMMY_AMMO_TAG, Math.max(0, amount));
        }
    }

    /** 玩家是否处于 TaCZ 换弹状态（含战术换弹动画阶段）。 */
    public static boolean isReloading(Player player) {
        if (player == null) {
            return false;
        }
        try {
            Class<?> opClass = Class.forName("com.tacz.guns.api.entity.IGunOperator");
            Object operator = opClass.getMethod("fromLivingEntity", LivingEntity.class).invoke(null, player);
            Object reloadState = operator.getClass().getMethod("getSynReloadState").invoke(operator);
            Object stateType = reloadState.getClass().getMethod("getStateType").invoke(reloadState);
            Object result = stateType.getClass().getMethod("isReloading").invoke(stateType);
            return result instanceof Boolean b && b;
        } catch (ReflectiveOperationException ex) {
            return false;
        }
    }

    /**
     * 加快 TaCZ 换弹进度。{@code timeMultiplier} 越小越快（与 {@link TaczGunEnchantBridge#reloadSpeedMultiplier} 一致）。
     */
    public static void accelerateReload(Player player, float timeMultiplier) {
        if (player == null || timeMultiplier >= 0.999f || !isReloading(player)) {
            return;
        }
        try {
            Class<?> opClass = Class.forName("com.tacz.guns.api.entity.IGunOperator");
            Object operator = opClass.getMethod("fromLivingEntity", LivingEntity.class).invoke(null, player);
            Object holder = operator.getClass().getMethod("getDataHolder").invoke(operator);
            java.lang.reflect.Field tsField = holder.getClass().getField("reloadTimestamp");
            long ts = tsField.getLong(holder);
            if (ts <= 0L) {
                return;
            }
            long bonus = (long) ((1.0 / timeMultiplier - 1.0) * 50L);
            if (bonus > 0L) {
                tsField.setLong(holder, ts - bonus);
            }
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.debug("[gunsrpg] TaCZ 换弹加速失败", ex);
        }
    }

    /** 读取 TaCZ 枪 data 中当前开火模式的基础 RPM。 */
    public static int getRoundsPerMinute(ItemStack gun) {
        Optional<ResourceLocation> gunId = readGunId(gun);
        if (gunId.isEmpty()) {
            return 0;
        }
        try {
            Class<?> fireModeClass = Class.forName("com.tacz.guns.api.item.gun.FireMode");
            String modeName = readFireModeName(gun);
            if ("UNKNOWN".equals(modeName)) {
                modeName = "SEMI";
            }
            Object fireMode = Enum.valueOf((Class<? extends Enum>) fireModeClass, modeName);
            Class<?> api = Class.forName(TIMELESS_API);
            Object optional = api.getMethod("getCommonGunIndex", ResourceLocation.class).invoke(null, gunId.get());
            if (!(optional instanceof Optional<?> opt) || opt.isEmpty()) {
                return 0;
            }
            Object gunData = opt.get().getClass().getMethod("getGunData").invoke(opt.get());
            Object rpm = gunData.getClass().getMethod("getRoundsPerMinute", fireModeClass).invoke(gunData, fireMode);
            if (rpm instanceof Number n) {
                return Math.max(1, n.intValue());
            }
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.debug("[gunsrpg] 读取 TaCZ RPM 失败", ex);
        }
        return 0;
    }

    public static int getCurrentAmmoCount(ItemStack gun) {
        if (gun.isEmpty()) {
            return 0;
        }
        try {
            Method get = gun.getItem().getClass().getMethod("getCurrentAmmoCount", ItemStack.class);
            Object result = get.invoke(gun.getItem(), gun);
            if (result instanceof Number n) {
                return n.intValue();
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return 0;
    }

    public static Optional<ResourceLocation> defaultAmmoId(ResourceLocation gunId) {
        if (!TaczCompat.isTaczLoaded() || gunId == null) {
            return Optional.empty();
        }
        try {
            Class<?> api = Class.forName(TIMELESS_API);
            Object optional = api.getMethod("getCommonGunIndex", ResourceLocation.class).invoke(null, gunId);
            if (!(optional instanceof Optional<?> opt) || opt.isEmpty()) {
                return Optional.empty();
            }
            Object index = opt.get();
            Object gunData = index.getClass().getMethod("getGunData").invoke(index);
            Object ammo = gunData.getClass().getMethod("getAmmoId").invoke(gunData);
            if (ammo instanceof ResourceLocation loc) {
                return Optional.of(loc);
            }
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.debug("[gunsrpg] 读取 TaCZ 默认弹药 {} 失败", gunId, ex);
        }
        return Optional.empty();
    }

    static LogicalSide readLogicalSide(Object event) {
        try {
            Object side = event.getClass().getMethod("getLogicalSide").invoke(event);
            if (side instanceof LogicalSide logicalSide) {
                return logicalSide;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return LogicalSide.SERVER;
    }

    static ResourceLocation readGunIdFromEvent(Object event) {
        try {
            Object id = event.getClass().getMethod("getGunId").invoke(event);
            if (id instanceof ResourceLocation loc) {
                return loc;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return null;
    }

    static net.minecraft.world.entity.LivingEntity readAttacker(Object event) {
        try {
            Object entity = event.getClass().getMethod("getAttacker").invoke(event);
            if (entity instanceof net.minecraft.world.entity.LivingEntity living) {
                return living;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return null;
    }

    static net.minecraft.world.entity.LivingEntity readKilled(Object event) {
        try {
            Object entity = event.getClass().getMethod("getKilledEntity").invoke(event);
            if (entity instanceof net.minecraft.world.entity.LivingEntity living) {
                return living;
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return null;
    }

    /**
     * 按 Guns RPG 枪规格设置 TaCZ {@code GunFireMode}。
     * TaCZ 客户端 {@code ShootKey} 仅在 {@code FireMode.AUTO} 时允许按住连发；
     * 整合包发枪若未写入该 NBT（常为 UNKNOWN），会表现为只能点射。
     */
    public static void applyFireModeFromSpec(ItemStack gun, FirearmSpec spec) {
        if (gun == null || gun.isEmpty() || spec == null || !isTaczGun(gun)) {
            return;
        }
        String desired = resolveDesiredFireMode(gun, spec);
        if (desired == null) {
            desired = defaultFireModeFromGunData(gun);
        }
        if (desired == null) {
            return;
        }
        String current = readFireModeName(gun);
        if (desired.equals(current) && !"UNKNOWN".equals(current)) {
            return;
        }
        setFireModeByName(gun, desired);
    }

    /** 从 TaCZ gun data 的 fire_mode 列表取默认模式（auto → AUTO）。 */
    private static String defaultFireModeFromGunData(ItemStack gun) {
        Optional<ResourceLocation> gunId = readGunId(gun);
        if (gunId.isEmpty()) {
            return null;
        }
        try {
            Class<?> api = Class.forName(TIMELESS_API);
            Object indexOpt =
                    api.getMethod("getCommonGunIndex", ResourceLocation.class).invoke(null, gunId.get());
            if (!(indexOpt instanceof Optional<?> optional) || optional.isEmpty()) {
                return null;
            }
            Object gunIndex = optional.get();
            Object gunData = gunIndex.getClass().getMethod("getGunData").invoke(gunIndex);
            @SuppressWarnings("unchecked")
            java.util.List<Object> modes =
                    (java.util.List<Object>)
                            gunData.getClass().getMethod("getFireModeSet").invoke(gunData);
            if (modes == null || modes.isEmpty()) {
                return null;
            }
            return normalizeFireModeName(String.valueOf(modes.get(0)));
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.debug("[gunsrpg] 读取 TaCZ 默认射击模式失败", ex);
            return null;
        }
    }

    private static String normalizeFireModeName(String raw) {
        if (raw == null || raw.isEmpty()) {
            return null;
        }
        return switch (raw.toLowerCase()) {
            case "auto" -> "AUTO";
            case "semi" -> "SEMI";
            case "burst" -> "BURST";
            default -> raw.toUpperCase();
        };
    }

    public static void cycleFireMode(ItemStack gun) {
        if (!isTaczGun(gun)) {
            return;
        }
        Optional<ResourceLocation> gunId = readGunId(gun);
        if (gunId.isEmpty()) {
            return;
        }
        try {
            Class<?> api = Class.forName(TIMELESS_API);
            Object indexOpt =
                    api.getMethod("getCommonGunIndex", ResourceLocation.class)
                            .invoke(null, gunId.get());
            if (!(indexOpt instanceof Optional<?> optional) || optional.isEmpty()) {
                return;
            }
            Object gunIndex = optional.get();
            Object gunData = gunIndex.getClass().getMethod("getGunData").invoke(gunIndex);
            @SuppressWarnings("unchecked")
            java.util.List<Object> modes =
                    (java.util.List<Object>)
                            gunData.getClass().getMethod("getFireModeSet").invoke(gunData);
            if (modes == null || modes.isEmpty()) {
                return;
            }
            String current = readFireModeName(gun);
            int idx = -1;
            for (int i = 0; i < modes.size(); i++) {
                if (String.valueOf(modes.get(i)).equalsIgnoreCase(current)) {
                    idx = i;
                    break;
                }
            }
            Object next = modes.get((idx + 1 + modes.size()) % modes.size());
            setFireModeByName(gun, String.valueOf(next));
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.debug("[gunsrpg] 切换 TaCZ 射击模式失败", ex);
        }
    }

    private static String resolveDesiredFireMode(ItemStack gun, FirearmSpec spec) {
        if (spec.supportsFireModeSwitch()) {
            FirearmStackState.ensureInitialized(gun, spec);
            FirearmMode mode = FirearmStackState.getFireMode(gun, spec);
            return mode == FirearmMode.AUTO ? "AUTO" : "SEMI";
        }
        if (spec.defaultAutomatic()) {
            return "AUTO";
        }
        return "SEMI";
    }

    public static String readFireModeName(ItemStack gun) {
        try {
            Object iGun = gun.getItem();
            Object mode = iGun.getClass().getMethod("getFireMode", ItemStack.class).invoke(iGun, gun);
            return mode != null ? String.valueOf(mode) : "UNKNOWN";
        } catch (ReflectiveOperationException ex) {
            return "UNKNOWN";
        }
    }

    private static void setFireModeByName(ItemStack gun, String modeName) {
        try {
            Class<?> fireModeClass = Class.forName("com.tacz.guns.api.item.gun.FireMode");
            Object mode = Enum.valueOf((Class<? extends Enum>) fireModeClass, modeName);
            Object iGun = gun.getItem();
            iGun.getClass()
                    .getMethod("setFireMode", ItemStack.class, fireModeClass)
                    .invoke(iGun, gun, mode);
        } catch (ReflectiveOperationException | IllegalArgumentException ex) {
            GunsRpg.LOGGER.debug("[gunsrpg] 设置 TaCZ 射击模式 {} 失败", modeName, ex);
        }
    }

    /** 枪管热度 0–1（无过热数据时返回 0）。 */
    public static float heatRatio(ItemStack gun) {
        if (gun.isEmpty() || !isTaczGun(gun)) {
            return 0f;
        }
        try {
            Object iGun = gun.getItem();
            if (!(Boolean) iGun.getClass().getMethod("hasHeatData", ItemStack.class).invoke(iGun, gun)) {
                return 0f;
            }
            float heat = (float) iGun.getClass().getMethod("getHeatAmount", ItemStack.class).invoke(iGun, gun);
            float max = heatMax(gun);
            if (max <= 0f) {
                return 0f;
            }
            return Math.min(1f, Math.max(0f, heat / max));
        } catch (ReflectiveOperationException ex) {
            return 0f;
        }
    }

    public static float getHeatAmount(ItemStack gun) {
        if (gun.isEmpty() || !isTaczGun(gun)) {
            return 0f;
        }
        try {
            Object iGun = gun.getItem();
            return (float) iGun.getClass().getMethod("getHeatAmount", ItemStack.class).invoke(iGun, gun);
        } catch (ReflectiveOperationException ex) {
            return 0f;
        }
    }

    public static void setHeatAmount(ItemStack gun, float amount) {
        if (gun.isEmpty() || !isTaczGun(gun)) {
            return;
        }
        try {
            Object iGun = gun.getItem();
            iGun.getClass().getMethod("setHeatAmount", ItemStack.class, float.class).invoke(iGun, gun, amount);
        } catch (ReflectiveOperationException ex) {
            GunsRpg.LOGGER.debug("[gunsrpg] 设置 TaCZ 枪管热度失败", ex);
        }
    }

    /** 每发增加的过热值（无数据时 0）。 */
    public static float heatPerShot(ItemStack gun) {
        return heatDataFloat(gun, "getHeatPerShot");
    }

    private static float heatMax(ItemStack gun) {
        return heatDataFloat(gun, "getHeatMax");
    }

    private static float heatDataFloat(ItemStack gun, String getter) {
        try {
            Object index = resolveGunIndex(gun);
            if (index == null) {
                return 0f;
            }
            Object gunData = index.getClass().getMethod("getGunData").invoke(index);
            Object heatData = gunData.getClass().getMethod("getHeatData").invoke(gunData);
            if (heatData == null) {
                return 0f;
            }
            return (float) heatData.getClass().getMethod(getter).invoke(heatData);
        } catch (ReflectiveOperationException ex) {
            return 0f;
        }
    }

    @Nullable
    private static Object resolveGunIndex(ItemStack gun) {
        Optional<ResourceLocation> gunId = readGunId(gun);
        if (gunId.isEmpty()) {
            return null;
        }
        try {
            Class<?> api = Class.forName(TIMELESS_API);
            Optional<?> opt =
                    (Optional<?>)
                            api.getMethod("getCommonGunIndex", ResourceLocation.class)
                                    .invoke(null, gunId.get());
            return opt != null && opt.isPresent() ? opt.get() : null;
        } catch (ReflectiveOperationException ex) {
            return null;
        }
    }
}
