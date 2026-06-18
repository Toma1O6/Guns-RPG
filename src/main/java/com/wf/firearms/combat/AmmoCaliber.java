package com.wf.firearms.combat;

import com.wf.firearms.config.WeaponCaliberOverrides;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

/** 弹药物品口径（对标 Guns RPG AmmoType）。 */
public enum AmmoCaliber {
    MM_9("9mm"),
    ACP_45("45acp", ".45 ACP"),
    MM_556("556mm", "5.56mm"),
    MM_762("762mm", "7.62mm"),
    MAGNUM("magnum", ".338 Magnum"),
    G_12("12g", "12g"),
    BOLT("bolt", "bolt"),
    UNKNOWN("?", "?");

    private final String suffix;
    private final String display;

    AmmoCaliber(String suffix) {
        this(suffix, suffix);
    }

    AmmoCaliber(String suffix, String display) {
        this.suffix = suffix;
        this.display = display;
    }

    public String suffix() {
        return suffix;
    }

    public String display() {
        return display;
    }

    public static AmmoCaliber fromAmmoItem(Item item) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
        if (key == null) {
            return UNKNOWN;
        }
        String path = key.getPath();
        AmmoCaliber best = UNKNOWN;
        int bestLen = -1;
        for (AmmoCaliber c : values()) {
            if (c == UNKNOWN) {
                continue;
            }
            String tail = "_" + c.suffix;
            if (path.endsWith(tail) && tail.length() > bestLen) {
                best = c;
                bestLen = tail.length();
            }
        }
        return best;
    }

    public static AmmoCaliber forWeapon(FirearmSpec spec) {
        if (spec != null) {
            var primary = WeaponCaliberOverrides.primaryCaliber(spec.weaponKey());
            if (primary.isPresent()) {
                return primary.get();
            }
        }
        if (spec != null && !spec.acceptedAmmo().isEmpty()) {
            for (Item item : spec.acceptedAmmo()) {
                if (item instanceof com.wf.firearms.item.AmmoItem ammoItem) {
                    AmmoCaliber c = ammoItem.getCaliber();
                    if (c != UNKNOWN) {
                        return c;
                    }
                }
            }
        }
        return switch (spec != null ? spec.weaponKey() : "") {
            case "glock", "m1911" -> MM_9;
            case "ump45" -> ACP_45;
            case "desert_eagle" -> MM_762;
            case "vector", "uzi" -> MM_9;
            case "r45" -> MAGNUM;
            case "p90" -> MM_556;
            case "thompson", "vss" -> MM_9;
            case "akm", "type_81", "fn_fal", "mk14ebr", "kar98k", "winchester", "pkm" -> MM_762;
            case "sks", "spr15", "hk416", "aug", "m249", "gatling", "minigun" -> MM_556;
            case "m95" -> MAGNUM;
            case "awm" -> MAGNUM;
            case "db2", "s686", "s1897", "s12k" -> G_12;
            default -> UNKNOWN;
        };
    }
}
