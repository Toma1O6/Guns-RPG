package com.wf.firearms.compat.tacz;

import com.wf.firearms.config.TaczBackendConfig;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/** Guns RPG 武器 key → TaCZ GunId（{@code tacz:ak47} 等）。 */
public final class TaczWeaponCatalog {
    private static final Map<String, String> BUILTIN = new LinkedHashMap<>();

    static {
        BUILTIN.put("glock", "tacz:glock_17");
        BUILTIN.put("m1911", "tacz:m1911");
        BUILTIN.put("r45", "tacz:taurus500");
        BUILTIN.put("desert_eagle", "tacz:deagle");
        BUILTIN.put("ump45", "tacz:ump45");
        BUILTIN.put("vector", "tacz:vector45");
        BUILTIN.put("uzi", "tacz:uzi");
        BUILTIN.put("p90", "tacz:p90");
        BUILTIN.put("thompson", "tacz:hk_mp5a5");
        BUILTIN.put("akm", "tacz:ak47");
        BUILTIN.put("hk416", "tacz:hk416d");
        BUILTIN.put("type_81", "tacz:type_81");
        BUILTIN.put("aug", "tacz:aug");
        BUILTIN.put("fn_fal", "tacz:fn_fal");
        BUILTIN.put("sks", "tacz:sks_tactical");
        BUILTIN.put("spr15", "tacz:spr15hb");
        BUILTIN.put("mk14ebr", "tacz:mk14");
        BUILTIN.put("kar98k", "tacz:kar98");
        BUILTIN.put("winchester", "tacz:m700");
        BUILTIN.put("awm", "tacz:ai_awp");
        BUILTIN.put("m95", "tacz:m95");
        BUILTIN.put("db2", "tacz:db_short");
        BUILTIN.put("s686", "tacz:db_long");
        BUILTIN.put("s1897", "tacz:m870");
        BUILTIN.put("s12k", "tacz:spas_12");
        BUILTIN.put("pkm", "tacz:rpk");
        BUILTIN.put("m249", "tacz:m249");
        BUILTIN.put("gatling", "tacz:minigun");
        BUILTIN.put("grenade_launcher", "tacz:m320");
        BUILTIN.put("rocket_launcher", "tacz:rpg7");
    }

    private TaczWeaponCatalog() {}

    public static Map<String, String> builtinWeaponToGunMap() {
        return Map.copyOf(BUILTIN);
    }

    public static Map<String, String> activeWeaponToGunMap() {
        return TaczBackendConfig.weaponMap();
    }

    public static Optional<ResourceLocation> gunIdForWeapon(String weaponKey) {
        String raw = TaczBackendConfig.weaponMap().get(weaponKey);
        if (raw == null) {
            return Optional.empty();
        }
        ResourceLocation loc = ResourceLocation.tryParse(raw);
        return Optional.ofNullable(loc);
    }

    public static Optional<String> weaponKeyForGunId(ResourceLocation gunId) {
        if (gunId == null) {
            return Optional.empty();
        }
        String target = gunId.toString();
        for (Map.Entry<String, String> e : TaczBackendConfig.weaponMap().entrySet()) {
            if (target.equals(e.getValue())) {
                return Optional.of(e.getKey());
            }
        }
        return Optional.empty();
    }
}
