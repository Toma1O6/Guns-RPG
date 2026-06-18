package com.wf.firearms.gameplay;

import com.wf.firearms.data.PlayerFirearmsData;

import java.util.Map;

/** 武器 key 与扩展天赋 skill id 前缀（如 desert_eagle → deagle）。 */
public final class WeaponExtensionIds {
    private static final Map<String, String> SKILL_PREFIX = Map.of("desert_eagle", "deagle");

    private WeaponExtensionIds() {}

    public static String skillPrefix(String weaponKey) {
        if (weaponKey == null || weaponKey.isEmpty()) {
            return "";
        }
        return SKILL_PREFIX.getOrDefault(weaponKey, weaponKey);
    }

    public static String skillId(String weaponKey, String suffix) {
        return skillPrefix(weaponKey) + "_" + suffix;
    }

    public static boolean isUnlocked(net.minecraft.world.entity.player.Player player, String weaponKey, String suffix) {
        if (player == null || weaponKey == null || suffix == null || suffix.isEmpty()) {
            return false;
        }
        if (PlayerFirearmsData.isUnlocked(player, skillId(weaponKey, suffix))) {
            return true;
        }
        return PlayerFirearmsData.isUnlocked(player, weaponKey + "_" + suffix);
    }

    /** 从天赋 id 前缀（如 deagle、db2）解析武器 key（如 desert_eagle）。 */
    public static String weaponKeyFromSkillPrefix(String skillPrefix) {
        if (skillPrefix == null || skillPrefix.isEmpty()) {
            return "";
        }
        for (var entry : SKILL_PREFIX.entrySet()) {
            if (entry.getValue().equals(skillPrefix)) {
                return entry.getKey();
            }
        }
        return skillPrefix;
    }

    public static String weaponKeyFromSkillId(String skillId, String suffixMarker) {
        if (skillId == null || suffixMarker == null || !skillId.endsWith(suffixMarker)) {
            return "";
        }
        String prefix = skillId.substring(0, skillId.length() - suffixMarker.length());
        return weaponKeyFromSkillPrefix(prefix);
    }
}
