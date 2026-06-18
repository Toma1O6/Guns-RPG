package com.wf.firearms.gameplay;

import com.wf.firearms.GunsRpg;

import com.wf.firearms.data.PlayerFirearmsData;
import com.wf.firearms.data.SkillDatabase;
import com.wf.firearms.data.SkillNode;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import com.wf.firearms.data.WeaponMapping;
import com.wf.firearms.network.FirearmsProgressSyncPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import java.util.Locale;
import java.util.Optional;

public final class SkillUnlockService {
    public record UnlockResult(boolean success, String message) {}

    private SkillUnlockService() {}

    public static UnlockResult tryUnlock(Player player, String skillId) {
        Optional<SkillNode> opt = SkillDatabase.getNode(skillId);
        if (opt.isEmpty()) {
            return new UnlockResult(false, "未知技能");
        }
        SkillNode node = opt.get();
        if (PlayerFirearmsData.isUnlocked(player, skillId)) {
            return new UnlockResult(false, "已解锁");
        }
        String block = prerequisiteBlock(player, node);
        if (block != null) {
            return new UnlockResult(false, block);
        }
        if (!meetsValidator(player, node)) {
            return new UnlockResult(false, validatorHint(node));
        }
        int price = node.getPrice();
        if (usesExtensionPoints(node)) {
            String weapon = extensionWeaponKey(node);
            if (PlayerFirearmsData.getTotalExtensionPoints(player, weapon) < price) {
                return new UnlockResult(false, "扩展点不足（需要 " + price + "）");
            }
            PlayerFirearmsData.spendExtensionPoints(player, weapon, price);
        } else {
            if (PlayerFirearmsData.getSkillPoints(player) < price) {
                return new UnlockResult(false, "技能点不足（需要 " + price + "）");
            }
            PlayerFirearmsData.addSkillPoints(player, -price);
        }
        PlayerFirearmsData.unlock(player, skillId);
        if (player instanceof ServerPlayer sp) {
            grantAssemblyWeaponIfNeeded(sp, skillId);
            MinecraftForge.EVENT_BUS.post(new SkillUnlockedEvent(sp, skillId));
            FirearmsProgressSyncPacket.sendTo(sp);
        }
        player.sendSystemMessage(
                Component.literal("§a[火器]§r 已解锁：")
                        .append(Component.literal(com.wf.firearms.data.SkillDisplayNames.resolveTitle(skillId))));
        return new UnlockResult(true, "已解锁");
    }

    public static boolean canUnlock(Player player, String skillId) {
        return tryUnlockPreview(player, skillId).isEmpty();
    }

    public static Optional<String> tryUnlockPreview(Player player, String skillId) {
        Optional<SkillNode> opt = SkillDatabase.getNode(skillId);
        if (opt.isEmpty()) {
            return Optional.of("未知技能");
        }
        SkillNode node = opt.get();
        if (PlayerFirearmsData.isUnlocked(player, skillId)) {
            return Optional.of("已解锁");
        }
        String block = prerequisiteBlock(player, node);
        if (block != null) {
            return Optional.of(block);
        }
        if (!meetsValidator(player, node)) {
            return Optional.of(validatorHint(node));
        }
        int price = node.getPrice();
        if (usesExtensionPoints(node)) {
            String weapon = extensionWeaponKey(node);
            if (PlayerFirearmsData.getTotalExtensionPoints(player, weapon) < price) {
                return Optional.of("需要 " + price + " 扩展点");
            }
        } else if (PlayerFirearmsData.getSkillPoints(player) < price) {
            return Optional.of("需要 " + price + " 技能点");
        }
        return Optional.empty();
    }

    private static String prerequisiteBlock(Player player, SkillNode node) {
        if (!node.isAssembly()) {
            String weapon = extensionWeaponKey(node);
            if (!weapon.isEmpty() && !WeaponUseGate.hasAssemblyUnlocked(player, weapon)) {
                return "需先解锁 "
                        + WeaponMapping.displayNameForWeaponKey(weapon)
                        + " 制作后，才能研发枪械天赋";
            }
        }
        String parent = node.getParent();
        if (parent.isEmpty()) {
            return null;
        }
        if (!PlayerFirearmsData.isUnlocked(player, parent)) {
            return "需先解锁上级节点";
        }
        return null;
    }

    private static boolean meetsValidator(Player player, SkillNode node) {
        String type = node.getValidatorType();
        int req = node.getLevel();
        if ("gunsrpg:level".equals(type) || type.isEmpty()) {
            return PlayerFirearmsData.getPlayerLevel(player) >= req;
        }
        if ("gunsrpg:weapon".equals(type)) {
            String weapon = node.getValidatorData();
            return PlayerFirearmsData.getWeaponLevel(player, weapon) >= req;
        }
        return true;
    }

    private static String validatorHint(SkillNode node) {
        String type = node.getValidatorType();
        int req = node.getLevel();
        if ("gunsrpg:weapon".equals(type)) {
            String name =
                    com.wf.firearms.data.SkillDisplayNames.resolveTitle(node.getValidatorData() + "_assembly");
            return String.format(Locale.ROOT, "需要 %s 熟练度 ≥ %d", name, req);
        }
        return String.format(Locale.ROOT, "需要火器等级 ≥ %d", req);
    }

    public static boolean usesExtensionPoints(SkillNode node) {
        if ("gunsrpg:weapon".equals(node.getValidatorType())) {
            return true;
        }
        return !node.isAssembly()
                && !node.getParent().isEmpty()
                && node.getParent().endsWith("_assembly");
    }

    private static void grantAssemblyWeaponIfNeeded(ServerPlayer player, String skillId) {
        if (!skillId.endsWith("_assembly")) {
            return;
        }
        ItemStack gun;
        try {
            gun = WeaponMapping.craftResultForAssembly(skillId);
        } catch (Throwable ex) {
            GunsRpg.LOGGER.error("[gunsrpg] 装配解锁发枪失败: {}", skillId, ex);
            return;
        }
        if (gun.isEmpty()) {
            return;
        }
        String flag = "wf_gun_granted_" + skillId;
        if (PlayerFirearmsData.root(player).getBoolean(flag)) {
            return;
        }
        if (!player.getInventory().add(gun.copy())) {
            player.drop(gun.copy(), false);
        }
        PlayerFirearmsData.root(player).putBoolean(flag, true);
    }

    public static String extensionWeaponKey(SkillNode node) {
        if (!node.getValidatorData().isEmpty()) {
            return node.getValidatorData();
        }
        if (node.getParent().endsWith("_assembly")) {
            return node.getParent().substring(0, node.getParent().length() - "_assembly".length());
        }
        return "";
    }
}
