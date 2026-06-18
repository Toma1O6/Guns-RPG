package com.wf.firearms.gameplay;


import com.wf.firearms.data.LevelingStrategy;
import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.data.WeaponMapping;
import com.wf.firearms.network.FirearmsProgressSyncPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Locale;

public final class ProgressionService {
    private static final int SKILL_POINTS_PER_LEVEL = 2;
    private static final int PERK_POINTS_PER_LEVEL = 3;

    private static LevelingStrategy leveling = LevelingStrategy.load();

    private ProgressionService() {}

    public static void setLeveling(LevelingStrategy strategy) {
        leveling = strategy;
    }

    public static void recordGunKill(Player player, String weaponKeyOrItemId) {
        PlayerFirearmsData.migrateLegacyKills(player);

        int total = PlayerFirearmsData.getTotalKills(player) + 1;
        PlayerFirearmsData.setTotalKills(player, total);

        int oldPlayerLevel = PlayerFirearmsData.getPlayerLevel(player);
        int newPlayerLevel = leveling.playerLevel().levelForKills(total);
        if (newPlayerLevel > oldPlayerLevel) {
            PlayerFirearmsData.setPlayerLevel(player, newPlayerLevel);
            grantPlayerLevelRewards(player, oldPlayerLevel, newPlayerLevel);
        }

        String key = weaponKeyOrItemId;
        if (key.contains(":")) {
            key = WeaponMapping.weaponKeyForItem(key).orElse(key);
        }
        if (key.isEmpty()) {
            return;
        }
        if (!WeaponUseGate.hasAssemblyUnlocked(player, key)) {
            if (player instanceof ServerPlayer sp) {
                FirearmsProgressSyncPacket.sendTo(sp);
            }
            return;
        }
        int wKills = PlayerFirearmsData.getWeaponKills(player, key) + 1;
        PlayerFirearmsData.setWeaponKills(player, key, wKills);

        int oldW = leveling.weaponLevel().levelForKills(wKills - 1);
        int newW = leveling.weaponLevel().levelForKills(wKills);
        if (newW > oldW) {
            int ext = 0;
            for (int lv = oldW + 1; lv <= newW; lv++) {
                ext += leveling.weaponLevel().pointsGrantedAtLevel(lv);
            }
            if (ext <= 0) {
                ext = newW - oldW;
            }
            PlayerFirearmsData.addExtensionPoints(player, key, ext);
            player.sendSystemMessage(
                    Component.literal(
                            String.format(
                                    Locale.ROOT,
                                    "§6[火器]§r %s 武器等级 %d → %d，+%d 扩展点",
                                    key,
                                    oldW,
                                    newW,
                                    ext)));
        }

        if (player instanceof ServerPlayer sp) {
            FirearmsProgressSyncPacket.sendTo(sp);
        }
    }

    /** 火器等级每升 1 级：+2 技能点、+3 属性点。 */
    public static void grantPlayerLevelRewards(Player player, int oldLevel, int newLevel) {
        if (newLevel <= oldLevel) {
            return;
        }
        int gainedLevels = newLevel - oldLevel;
        int skillGain = gainedLevels * SKILL_POINTS_PER_LEVEL;
        int perkGain = gainedLevels * PERK_POINTS_PER_LEVEL;
        PlayerFirearmsData.addSkillPoints(player, skillGain);
        PlayerFirearmsData.addPerkPoints(player, perkGain);
        player.sendSystemMessage(
                Component.literal(
                        String.format(
                                Locale.ROOT,
                                "§6[火器]§r 火器等级 %d → %d，获得 %d 技能点、%d 属性点",
                                oldLevel,
                                newLevel,
                                skillGain,
                                perkGain)));
    }
}
