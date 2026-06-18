package com.wf.firearms.combat;

import com.wf.firearms.config.WeaponCaliberOverrides;
import com.wf.firearms.compat.tacz.TaczBridge;
import com.wf.firearms.compat.tacz.TaczWeaponBinding;
import com.wf.firearms.compat.tacz.TaczWeaponCatalog;
import com.wf.firearms.item.AmmoItem;
import com.wf.firearms.registry.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/** 整合包材料弹：tooltip / 图标展示用口径与代表弹种。 */
public final class WeaponAmmoDisplay {
    private WeaponAmmoDisplay() {}

    public static Optional<String> resolveWeaponKey(ItemStack gun) {
        if (gun == null || gun.isEmpty()) {
            return Optional.empty();
        }
        return TaczWeaponBinding.read(gun)
                .or(() -> TaczBridge.readGunId(gun).flatMap(TaczWeaponCatalog::weaponKeyForGunId));
    }

    public static Set<AmmoCaliber> calibersFor(FirearmSpec spec) {
        LinkedHashSet<AmmoCaliber> out = new LinkedHashSet<>();
        if (spec == null) {
            return out;
        }
        WeaponCaliberOverrides.primaryCaliber(spec.weaponKey()).ifPresent(out::add);
        for (Item item : spec.acceptedAmmo()) {
            if (item instanceof AmmoItem ammoItem) {
                out.add(ammoItem.getCaliber());
            }
        }
        if (out.isEmpty()) {
            AmmoCaliber fallback = AmmoCaliber.forWeapon(spec);
            if (fallback != AmmoCaliber.UNKNOWN) {
                out.add(fallback);
            }
        }
        return out;
    }

    /** 用于 TaCZ 大 tooltip 弹药行左侧图标。 */
    public static ItemStack representativeAmmoStack(FirearmSpec spec) {
        if (spec == null) {
            return ItemStack.EMPTY;
        }
        Optional<AmmoCaliber> primary = WeaponCaliberOverrides.primaryCaliber(spec.weaponKey());
        if (primary.isPresent()) {
            Item fromList = pickDisplayAmmoForCaliber(spec.acceptedAmmo(), primary.get());
            if (fromList != null) {
                return new ItemStack(fromList);
            }
            Item fromRegistry = woodenAmmoForCaliber(primary.get());
            if (fromRegistry != null) {
                return new ItemStack(fromRegistry);
            }
        }
        if (spec.acceptedAmmo().isEmpty()) {
            return ItemStack.EMPTY;
        }
        Item preferred = pickDisplayAmmoItem(spec.acceptedAmmo());
        return preferred != null ? new ItemStack(preferred) : ItemStack.EMPTY;
    }

    /** TaCZ 大 tooltip 弹药行：口径分类 + 材料弹（不显示 TaCZ 原版弹种名）。 */
    public static Component materialAmmoTooltipLine(FirearmSpec spec) {
        return inventoryHintLine(spec);
    }

    public static Component tooltipAmmoName(FirearmSpec spec) {
        return materialAmmoTooltipLine(spec);
    }

    public static Component inventoryHintLine(FirearmSpec spec) {
        String calibers = caliberPlainText(spec);
        if (calibers.isEmpty()) {
            return Component.translatable("gunsrpg.gun.no_ammo_reserve");
        }
        // 勿在语言包内写 § 色码：FancyMenu 平滑字体测量会 ArrayIndexOutOfBounds
        return Component.translatable("gunsrpg.tacz.tooltip.ammo_row", calibers)
                .withStyle(ChatFormatting.GRAY);
    }

    /** TaCZ 大 tooltip 弹量行：弹匣 + 背包材料弹备弹。 */
    public static Component taczAmmoCountLine(
            ItemStack gun, FirearmSpec spec, int currentMag, int maxMag, int backpackRounds) {
        if (spec == null) {
            return Component.literal("%d/%d".formatted(currentMag, maxMag))
                    .withStyle(ChatFormatting.GRAY);
        }
        return Component.translatable(
                        "gunsrpg.tacz.tooltip.mag_and_reserve",
                        currentMag,
                        maxMag,
                        backpackRounds)
                .withStyle(ChatFormatting.GRAY);
    }

    /** 口径展示纯文本（避免嵌套样式组件触发 FancyMenu 崩溃）。 */
    public static String caliberPlainText(FirearmSpec spec) {
        Set<AmmoCaliber> calibers = calibersFor(spec);
        if (calibers.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (AmmoCaliber c : calibers) {
            if (i++ > 0) {
                sb.append(" / ");
            }
            sb.append(Component.translatable("gunsrpg.caliber." + c.suffix()).getString());
        }
        return sb.toString();
    }

    public static boolean isPackWeapon(ItemStack gun) {
        return resolveWeaponKey(gun)
                .filter(key -> com.wf.firearms.config.TaczBackendConfig.weaponMap().containsKey(key))
                .isPresent();
    }

    private static MutableComponent caliberListComponent(FirearmSpec spec) {
        Set<AmmoCaliber> calibers = calibersFor(spec);
        MutableComponent out = Component.empty();
        int i = 0;
        for (AmmoCaliber c : calibers) {
            if (i++ > 0) {
                out.append(Component.literal(" / "));
            }
            out.append(Component.translatable("gunsrpg.caliber." + c.suffix()));
        }
        return out;
    }

    private static Item woodenAmmoForCaliber(AmmoCaliber caliber) {
        var reg = ModItems.ammo("wooden_" + caliber.suffix());
        return reg != null && reg.isPresent() ? reg.get() : null;
    }

    private static Item pickDisplayAmmoForCaliber(List<Item> accepted, AmmoCaliber caliber) {
        Item wooden = null;
        Item any = null;
        for (Item item : accepted) {
            if (!(item instanceof AmmoItem ammoItem) || ammoItem.getCaliber() != caliber) {
                continue;
            }
            any = item;
            var key = net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(item);
            if (key != null && key.getPath().startsWith("wooden_")) {
                wooden = item;
                break;
            }
        }
        return wooden != null ? wooden : any;
    }

    private static Item pickDisplayAmmoItem(List<Item> accepted) {
        Item wooden = null;
        Item any = null;
        for (Item item : accepted) {
            if (!(item instanceof AmmoItem)) {
                continue;
            }
            any = item;
            var key = net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(item);
            if (key != null && key.getPath().startsWith("wooden_")) {
                wooden = item;
                break;
            }
        }
        if (wooden != null) {
            return wooden;
        }
        if (any != null) {
            return any;
        }
        for (Item item : accepted) {
            if (item != ModItems.SHOTGUN_SHELL.get()) {
                return item;
            }
        }
        return accepted.isEmpty() ? null : accepted.get(0);
    }
}
