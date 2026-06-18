package com.wf.firearms.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/** 玩家火器进度（持久 NBT）。 */
public final class PlayerFirearmsData {
    private static final String ROOT = "gunsrpg";
    private static LevelingStrategy leveling = LevelingStrategy.load();

    private PlayerFirearmsData() {}

    public static void setLevelingStrategy(LevelingStrategy strategy) {
        leveling = strategy;
    }

    public static CompoundTag root(Player player) {
        CompoundTag persistent = player.getPersistentData();
        if (!persistent.contains(ROOT)) {
            persistent.put(ROOT, new CompoundTag());
        }
        return persistent.getCompound(ROOT);
    }

    /** 进入世界/连接服务器前清空客户端缓存，避免上一存档的 persistentData 残留。 */
    public static void clearClientProgress(Player player) {
        player.getPersistentData().remove(ROOT);
    }

    public static int getPlayerLevel(Player player) {
        return root(player).getInt("player_level");
    }

    public static void setPlayerLevel(Player player, int level) {
        root(player).putInt("player_level", level);
    }

    public static int getSkillPoints(Player player) {
        return root(player).getInt("skill_points");
    }

    public static void addSkillPoints(Player player, int delta) {
        root(player).putInt("skill_points", Math.max(0, getSkillPoints(player) + delta));
    }

    public static int getPerkPoints(Player player) {
        return root(player).getInt("perk_points");
    }

    public static void addPerkPoints(Player player, int delta) {
        root(player).putInt("perk_points", Math.max(0, getPerkPoints(player) + delta));
    }

    public static int getSharedWeaponPoints(Player player) {
        return root(player).getInt("shared_weapon_points");
    }

    public static void addSharedWeaponPoints(Player player, int delta) {
        root(player).putInt("shared_weapon_points", Math.max(0, getSharedWeaponPoints(player) + delta));
    }

    public static int getTotalKills(Player player) {
        return root(player).getInt("total_kills");
    }

    public static void setTotalKills(Player player, int kills) {
        root(player).putInt("total_kills", kills);
    }

    public static int getWeaponKills(Player player, String weaponKey) {
        return root(player).getCompound("weapon_kills").getInt(weaponKey);
    }

    public static void setWeaponKills(Player player, String weaponKey, int kills) {
        CompoundTag weapons = root(player).getCompound("weapon_kills");
        weapons.putInt(weaponKey, kills);
        root(player).put("weapon_kills", weapons);
    }

    public static int getWeaponLevel(Player player, String weaponKey) {
        return leveling.weaponLevel().levelForKills(getWeaponKills(player, weaponKey));
    }

    public static int getExtensionPoints(Player player, String weaponKey) {
        return root(player).getCompound("weapon_ext_points").getInt(weaponKey);
    }

    public static void addExtensionPoints(Player player, String weaponKey, int delta) {
        CompoundTag ext = root(player).getCompound("weapon_ext_points");
        ext.putInt(weaponKey, Math.max(0, ext.getInt(weaponKey) + delta));
        root(player).put("weapon_ext_points", ext);
    }

    /** 单枪扩展点 + 武器点数书共享池。 */
    public static int getTotalExtensionPoints(Player player, String weaponKey) {
        return getExtensionPoints(player, weaponKey) + getSharedWeaponPoints(player);
    }

    /** 先扣单枪扩展点，不足再扣共享池（对齐原版 GunKillData）。 */
    public static void spendExtensionPoints(Player player, String weaponKey, int amount) {
        if (amount <= 0) {
            return;
        }
        int weaponPts = getExtensionPoints(player, weaponKey);
        int fromWeapon = Math.min(amount, weaponPts);
        if (fromWeapon > 0) {
            addExtensionPoints(player, weaponKey, -fromWeapon);
            amount -= fromWeapon;
        }
        if (amount > 0) {
            addSharedWeaponPoints(player, -amount);
        }
    }

    public static boolean isUnlocked(Player player, String skillId) {
        ListTag list = root(player).getList("unlocked", Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++) {
            if (skillId.equals(list.getString(i))) {
                return true;
            }
        }
        return false;
    }

    public static void unlock(Player player, String skillId) {
        if (isUnlocked(player, skillId)) {
            return;
        }
        Set<String> next = new HashSet<>(unlockedSet(player));
        next.add(skillId);
        writeUnlockedSet(player, next);
    }

    /** 客户端同步：用服务端下发的列表覆盖已解锁技能。 */
    public static void replaceUnlocked(Player player, java.util.Collection<String> skillIds) {
        Set<String> set = new HashSet<>();
        for (String id : skillIds) {
            if (id != null && !id.isEmpty()) {
                set.add(id);
            }
        }
        writeUnlockedSet(player, set);
    }

    /** 合并解锁列表（防止旧同步包覆盖掉较新的解锁）。 */
    public static void mergeUnlocked(Player player, java.util.Collection<String> skillIds) {
        Set<String> merged = new HashSet<>(unlockedSet(player));
        for (String id : skillIds) {
            if (id != null && !id.isEmpty()) {
                merged.add(id);
            }
        }
        writeUnlockedSet(player, merged);
    }

    public static void writeUnlockedSet(Player player, Set<String> skillIds) {
        ListTag list = new ListTag();
        for (String id : skillIds) {
            list.add(StringTag.valueOf(id));
        }
        root(player).put("unlocked", list);
    }

    public static int getSyncRevision(Player player) {
        return root(player).getInt("sync_rev");
    }

    public static void setSyncRevision(Player player, int revision) {
        root(player).putInt("sync_rev", revision);
    }

    /** 每次向客户端推送进度前递增，用于丢弃乱序的旧包。 */
    public static int bumpSyncRevision(Player player) {
        int next = getSyncRevision(player) + 1;
        setSyncRevision(player, next);
        return next;
    }

    /** 死亡/维度切换时把进度复制到新玩家实体。 */
    public static void copyAllProgress(Player from, Player to) {
        root(to).merge(root(from).copy());
    }

    /** 单人游戏：把服务端玩家进度复制到本地客户端（供 UI 立即刷新）。 */
    public static void copyProgressFrom(Player source, Player target) {
        CompoundTag src = root(source);
        CompoundTag dst = root(target);
        dst.putInt("player_level", src.getInt("player_level"));
        dst.putInt("total_kills", src.getInt("total_kills"));
        dst.putInt("skill_points", src.getInt("skill_points"));
        dst.putInt("perk_points", src.getInt("perk_points"));
        dst.putInt("shared_weapon_points", src.getInt("shared_weapon_points"));
        dst.putInt("sync_rev", src.getInt("sync_rev"));
        writeUnlockedSet(target, unlockedSet(source));
        dst.put("weapon_kills", src.getCompound("weapon_kills").copy());
        dst.put("weapon_ext_points", src.getCompound("weapon_ext_points").copy());
        dst.put("perk_inv", src.getCompound("perk_inv").copy());
    }

    public static Set<String> unlockedSet(Player player) {
        Set<String> out = new HashSet<>();
        ListTag list = root(player).getList("unlocked", Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++) {
            out.add(list.getString(i));
        }
        return out;
    }

    /** Perk 投资步数（可为负，表示 debuff 侧）。 */
    public static int getPerkInvestment(Player player, String perkId) {
        return Math.max(0, root(player).getCompound("perk_inv").getInt(perkId));
    }

    public static void setPerkInvestment(Player player, String perkId, int steps) {
        CompoundTag perks = root(player).getCompound("perk_inv");
        perks.putInt(perkId, Math.max(0, steps));
        root(player).put("perk_inv", perks);
    }

    public static double getPerkMultiplier(Player player, String perkId) {
        return SkillDatabase.getPerk(perkId)
                .map(p -> {
                    int steps = Math.max(0, getPerkInvestment(player, perkId));
                    double raw = steps * p.getScaling();
                    double max = p.getBuffBound();
                    if (PerkCatalog.isReductionDisplay(perkId)) {
                        max = Math.min(max, PerkCatalog.REDUCTION_BUFF_CAP);
                    }
                    return Math.max(0.0, Math.min(max, raw));
                })
                .orElse(0.0);
    }

    public static int maxPerkSteps(PerkDef perk, boolean positive) {
        double bound = positive ? perk.getBuffBound() : perk.getDebuffBound();
        if (perk.getScaling() <= 0) {
            return 0;
        }
        return (int) Math.round(bound / perk.getScaling());
    }

    public static void migrateLegacyKills(Player player) {
        CompoundTag pd = player.getPersistentData();
        if (pd.contains("wf_gun_kills") && getTotalKills(player) == 0) {
            setTotalKills(player, pd.getInt("wf_gun_kills"));
        }
    }

    public static String weaponKeyFromAssembly(String assemblyId) {
        if (assemblyId == null || !assemblyId.endsWith("_assembly")) {
            return assemblyId == null ? "" : assemblyId;
        }
        return assemblyId.substring(0, assemblyId.length() - "_assembly".length());
    }

    public static Component playerLevelProgressText(Player player) {
        int level = getPlayerLevel(player);
        int kills = getTotalKills(player);
        int next = level + 1;
        int need = leveling.playerLevel().killsRequiredForLevel(next);
        if (need <= 0 || level >= leveling.playerLevel().maxLevel()) {
            return Component.literal(String.format(Locale.ROOT, "Lv%d · %d 击杀", level, kills));
        }
        return Component.literal(
                String.format(Locale.ROOT, "Lv%d · %d/%d 击杀 → Lv%d", level, kills, need, next));
    }
}
