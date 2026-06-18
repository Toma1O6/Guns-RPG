package com.wf.firearms.compat.tacz;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Optional;

/** wf 武器 key → TaCZ {@code textures/gun/slot/<id>.png} 路径名。 */
public final class TaczSlotIconPaths {
    private static final Map<String, String> SLOT_BY_WEAPON =
            Map.ofEntries(
                    Map.entry("glock", "glock_17"),
                    Map.entry("m1911", "m1911"),
                    Map.entry("r45", "taurus500"),
                    Map.entry("desert_eagle", "deagle"),
                    Map.entry("ump45", "ump45"),
                    Map.entry("vector", "vector45"),
                    Map.entry("uzi", "uzi"),
                    Map.entry("p90", "p90"),
                    Map.entry("thompson", "hk_mp5a5"),
                    Map.entry("akm", "ak47"),
                    Map.entry("hk416", "hk416d"),
                    Map.entry("type_81", "type_81"),
                    Map.entry("aug", "aug"),
                    Map.entry("vss", "g36k"),
                    Map.entry("fn_fal", "fn_fal"),
                    Map.entry("sks", "sks_tactical"),
                    Map.entry("spr15", "spr15hb"),
                    Map.entry("mk14ebr", "mk14"),
                    Map.entry("kar98k", "kar98"),
                    Map.entry("winchester", "m700"),
                    Map.entry("awm", "ai_awp"),
                    Map.entry("m95", "m95"),
                    Map.entry("db2", "db_short"),
                    Map.entry("s686", "db_long"),
                    Map.entry("s1897", "m870"),
                    Map.entry("s12k", "spas_12"),
                    Map.entry("pkm", "rpk"),
                    Map.entry("m249", "m249"),
                    Map.entry("gatling", "minigun"),
                    Map.entry("grenade_launcher", "m320"),
                    Map.entry("rocket_launcher", "rpg7"));

    private TaczSlotIconPaths() {}

    public static Optional<ResourceLocation> slotTexture(String weaponKey) {
        if (weaponKey == null || weaponKey.isEmpty()) {
            return Optional.empty();
        }
        String slot = SLOT_BY_WEAPON.get(weaponKey);
        if (slot == null) {
            slot =
                    TaczWeaponCatalog.gunIdForWeapon(weaponKey)
                            .map(ResourceLocation::getPath)
                            .orElse(null);
        }
        if (slot == null || slot.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(ResourceLocation.fromNamespaceAndPath("tacz", "textures/gun/slot/" + slot + ".png"));
    }
}
