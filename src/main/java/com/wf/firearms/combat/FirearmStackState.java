package com.wf.firearms.combat;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

/** 枪械物品 NBT：弹匣、装填弹药类型、卡弹、开火模式、换弹/排障进度。 */
public final class FirearmStackState {
    public static final String TAG = "WfFirearm";
    private static final String AMMO = "ammo";
    private static final String AMMO_MATERIAL = "ammo_material";
    private static final String AMMO_ITEM = "ammo_item";
    private static final String JAMMED = "jammed";
    private static final String FIRE_MODE = "fire_mode";
    private static final String ACTION = "action";
    private static final String ACTION_START = "action_start";
    private static final String ACTION_END = "action_end";
    private static final String AIMING = "aiming";

    public enum Action {
        NONE,
        RELOAD,
        UNJAM
    }

    private FirearmStackState() {}

    public static CompoundTag tag(ItemStack stack) {
        CompoundTag root = stack.getOrCreateTag();
        if (!root.contains(TAG)) {
            root.put(TAG, new CompoundTag());
        }
        return root.getCompound(TAG);
    }

    public static int getAmmo(ItemStack stack) {
        return tag(stack).getInt(AMMO);
    }

    public static void setAmmo(ItemStack stack, int ammo) {
        int value = Math.max(0, ammo);
        tag(stack).putInt(AMMO, value);
        if (value <= 0) {
            clearLoadedAmmoType(stack);
        }
    }

    public static AmmoMaterial getLoadedMaterial(ItemStack stack) {
        return AmmoMaterial.fromId(tag(stack).getString(AMMO_MATERIAL));
    }

    public static AmmoCaliber getLoadedCaliber(ItemStack stack) {
        String itemId = getLoadedAmmoItemId(stack);
        if (!itemId.isEmpty()) {
            int idx = itemId.indexOf(':');
            String path = idx >= 0 ? itemId.substring(idx + 1) : itemId;
            int us = path.lastIndexOf('_');
            if (us > 0) {
                String suffix = path.substring(us + 1);
                for (AmmoCaliber c : AmmoCaliber.values()) {
                    if (c.suffix().equalsIgnoreCase(suffix)) {
                        return c;
                    }
                }
            }
        }
        return AmmoCaliber.UNKNOWN;
    }

    public static void setLoadedMaterial(ItemStack stack, AmmoMaterial material) {
        tag(stack).putString(AMMO_MATERIAL, material.getId());
    }

    public static String getLoadedAmmoItemId(ItemStack stack) {
        return tag(stack).getString(AMMO_ITEM);
    }

    public static void setLoadedAmmoItem(ItemStack stack, String itemId) {
        if (itemId == null || itemId.isEmpty()) {
            tag(stack).remove(AMMO_ITEM);
        } else {
            tag(stack).putString(AMMO_ITEM, itemId);
        }
    }

    public static void clearLoadedAmmoType(ItemStack stack) {
        CompoundTag t = tag(stack);
        t.remove(AMMO_MATERIAL);
        t.remove(AMMO_ITEM);
    }

    public static boolean hasLoadedAmmoType(ItemStack stack) {
        return !tag(stack).getString(AMMO_MATERIAL).isEmpty();
    }

    public static boolean isJammed(ItemStack stack) {
        return tag(stack).getBoolean(JAMMED);
    }

    public static void setJammed(ItemStack stack, boolean jammed) {
        tag(stack).putBoolean(JAMMED, jammed);
    }

    public static FirearmMode getFireMode(ItemStack stack, FirearmSpec spec) {
        FirearmMode mode = FirearmMode.fromOrdinal(tag(stack).getInt(FIRE_MODE));
        if (!spec.supportsFireModeSwitch() && mode == FirearmMode.AUTO && !spec.defaultAutomatic()) {
            return FirearmMode.SINGLE;
        }
        return mode;
    }

    public static void setFireMode(ItemStack stack, FirearmMode mode) {
        tag(stack).putInt(FIRE_MODE, mode.ordinal());
    }

    public static void ensureInitialized(ItemStack stack, FirearmSpec spec) {
        CompoundTag t = tag(stack);
        if (!t.contains(AMMO) && !t.contains(FIRE_MODE)) {
            t.putInt(AMMO, 0);
            t.putInt(
                    FIRE_MODE,
                    spec.defaultAutomatic() ? FirearmMode.AUTO.ordinal() : FirearmMode.SINGLE.ordinal());
            t.putBoolean(JAMMED, false);
            t.putString(ACTION, Action.NONE.name());
            t.putLong(ACTION_START, 0L);
            t.putLong(ACTION_END, 0L);
        }
    }

    public static Action getAction(ItemStack stack) {
        String name = tag(stack).getString(ACTION);
        if (name.isEmpty()) {
            return Action.NONE;
        }
        try {
            return Action.valueOf(name);
        } catch (IllegalArgumentException ex) {
            return Action.NONE;
        }
    }

    public static void setAction(ItemStack stack, Action action, long startGameTime, long endGameTime) {
        CompoundTag t = tag(stack);
        t.putString(ACTION, action.name());
        t.putLong(ACTION_START, startGameTime);
        t.putLong(ACTION_END, endGameTime);
    }

    public static long getActionStart(ItemStack stack) {
        return tag(stack).getLong(ACTION_START);
    }

    public static long getActionEnd(ItemStack stack) {
        return tag(stack).getLong(ACTION_END);
    }

    public static boolean isBusy(ItemStack stack) {
        Action action = getAction(stack);
        return action == Action.RELOAD || action == Action.UNJAM;
    }

    public static boolean isAiming(ItemStack stack) {
        return tag(stack).getBoolean(AIMING);
    }

    public static void setAiming(ItemStack stack, boolean aiming) {
        tag(stack).putBoolean(AIMING, aiming);
    }

    public static void toggleAiming(ItemStack stack) {
        setAiming(stack, !isAiming(stack));
    }

    public static AmmoStatProfile loadedAmmoStats(ItemStack gun, FirearmSpec spec) {
        if (!hasLoadedAmmoType(gun)) {
            return AmmoStatProfile.EMPTY;
        }
        return AmmoStatsRegistry.profile(getLoadedCaliber(gun), getLoadedMaterial(gun));
    }

    public static void appendTooltip(ItemStack stack, FirearmSpec spec, java.util.List<Component> lines) {
        ensureInitialized(stack, spec);
        lines.add(
                Component.translatable(
                        "gunsrpg.gun.ammo",
                        getAmmo(stack),
                        spec.magazineSize()));
        lines.add(
                Component.translatable(
                        "gunsrpg.gun.caliber", AmmoCaliber.forWeapon(spec).display()));
        if (getAmmo(stack) > 0 && hasLoadedAmmoType(stack)) {
            AmmoMaterial mat = getLoadedMaterial(stack);
            lines.add(
                    Component.translatable(
                            "gunsrpg.gun.material",
                            Component.translatable("gunsrpg.ammo.material." + mat.getId())));
            AmmoCaliber loadedCal = getLoadedCaliber(stack);
            if (loadedCal == AmmoCaliber.UNKNOWN) {
                loadedCal = AmmoCaliber.forWeapon(spec);
            }
            int dmgPct = AmmoStatsRegistry.damagePercent(mat, loadedCal);
            lines.add(Component.translatable("gunsrpg.gun.loaded_damage", dmgPct));
        } else if (getAmmo(stack) > 0) {
            lines.add(Component.translatable("gunsrpg.gun.material_unknown"));
        }
        if (isJammed(stack)) {
            lines.add(Component.translatable("gunsrpg.gun.jammed"));
        }
        appendWearTooltip(lines, stack, spec.weaponKey());
        if (spec.supportsFireModeSwitch()) {
            lines.add(
                    Component.translatable(
                            "gunsrpg.gun.firemode",
                            Component.translatable(
                                    "gunsrpg.gun.firemode."
                                            + getFireMode(stack, spec).name().toLowerCase())));
        }
        Action action = getAction(stack);
        if (action == Action.RELOAD) {
            lines.add(Component.translatable("gunsrpg.gun.reloading"));
        } else if (action == Action.UNJAM) {
            lines.add(Component.translatable("gunsrpg.gun.unjamming"));
        }
    }

    public static void appendWearTooltip(java.util.List<Component> lines, ItemStack stack, String weaponKey) {
        int max = WeaponWearState.getEffectiveMaxWear(stack, weaponKey);
        int wear = WeaponWearState.getWear(stack);
        if (WeaponWearState.isDestroyed(stack, weaponKey)) {
            lines.add(Component.translatable("gunsrpg.gun.wear_destroyed"));
            return;
        }
        if (wear > 0 || WeaponWearState.getWearLimit(stack) < 0.999f) {
            int pct = Math.round(WeaponWearState.conditionRatio(stack, weaponKey) * 100f);
            lines.add(Component.translatable("gunsrpg.gun.wear", pct, max - wear, max));
        }
    }
}
