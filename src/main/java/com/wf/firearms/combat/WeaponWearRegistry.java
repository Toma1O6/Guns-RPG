package com.wf.firearms.combat;

import java.util.Map;

/** 各枪最大耐久（对齐原版 Guns RPG Item.Properties#durability）。 */
public final class WeaponWearRegistry {
    private static final Map<String, Integer> MAX_WEAR = Map.ofEntries(
            Map.entry("m1911", 550),
            Map.entry("r45", 350),
            Map.entry("desert_eagle", 450),
            Map.entry("thompson", 850),
            Map.entry("ump45", 900),
            Map.entry("vector", 1100),
            Map.entry("akm", 1000),
            Map.entry("hk416", 1050),
            Map.entry("aug", 1000),
            Map.entry("vss", 850),
            Map.entry("sks", 700),
            Map.entry("mk14ebr", 900),
            Map.entry("kar98k", 300),
            Map.entry("winchester", 400),
            Map.entry("awm", 350),
            Map.entry("s686", 230),
            Map.entry("s1897", 320),
            Map.entry("s12k", 370),
            Map.entry("pkm", 950),
            Map.entry("m249", 1100),
            Map.entry("gatling", 1200),
            Map.entry("minigun", 1200));

    private WeaponWearRegistry() {}

    public static int maxWear(String weaponKey) {
        return MAX_WEAR.getOrDefault(weaponKey, 500);
    }
}
