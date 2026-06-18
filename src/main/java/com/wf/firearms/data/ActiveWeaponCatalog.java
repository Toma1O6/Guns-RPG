package com.wf.firearms.data;

import com.wf.firearms.config.TaczBackendConfig;
import com.wf.firearms.registry.CreativeGunCatalog;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/** 整合包当前启用的枪械装配体（过滤旧版/未映射枪）。 */
public final class ActiveWeaponCatalog {
    private static final Set<String> ACTIVE_WEAPON_KEYS = new HashSet<>();

    static {
        ACTIVE_WEAPON_KEYS.addAll(TaczBackendConfig.weaponMap().keySet());
        ACTIVE_WEAPON_KEYS.addAll(Arrays.asList(CreativeGunCatalog.gunKeys()));
        ACTIVE_WEAPON_KEYS.add("grenade_launcher");
        ACTIVE_WEAPON_KEYS.add("rocket_launcher");
    }

    private ActiveWeaponCatalog() {}

    public static boolean isActiveWeaponKey(String weaponKey) {
        return weaponKey != null && !weaponKey.isEmpty() && ACTIVE_WEAPON_KEYS.contains(weaponKey);
    }

    public static boolean isActiveAssembly(String assemblyId) {
        if (assemblyId == null || !assemblyId.endsWith("_assembly")) {
            return false;
        }
        String key = assemblyId.substring(0, assemblyId.length() - "_assembly".length());
        return isActiveWeaponKey(key);
    }
}
