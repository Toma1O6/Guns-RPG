package com.wf.firearms.gameplay;

import com.wf.firearms.data.PlayerFirearmsData;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

/** 造弹大师 I–V：标准弹与马格南分开计产量，避免 3 合 1 配方失衡。 */
public final class AmmoCraftBonusService {
    private AmmoCraftBonusService() {}

    public static int bonusOutput(Player player, ItemStack result) {
        if (player == null || result.isEmpty() || !isGunsRpgAmmo(result)) {
            return 0;
        }
        if (isMagnum(result)) {
            return magnumBonus(player, player.getRandom());
        }
        return standardBonus(player);
    }

    /** 手枪/步枪/霰弹：I、III、V 各 +1（满级 +3）。 */
    public static int standardBonus(Player player) {
        int bonus = 0;
        if (PlayerFirearmsData.isUnlocked(player, "ammo_smithing_mastery_i")) {
            bonus++;
        }
        if (PlayerFirearmsData.isUnlocked(player, "ammo_smithing_mastery_iii")) {
            bonus++;
        }
        if (PlayerFirearmsData.isUnlocked(player, "ammo_smithing_mastery_v")) {
            bonus++;
        }
        return bonus;
    }

    /** 马格南：II/IV 概率 +1，V 必 +1（满级稳定 2 发）。 */
    public static int magnumBonus(Player player, RandomSource random) {
        if (PlayerFirearmsData.isUnlocked(player, "ammo_smithing_mastery_v")) {
            return 1;
        }
        float chance = 0f;
        if (PlayerFirearmsData.isUnlocked(player, "ammo_smithing_mastery_iv")) {
            chance = 0.75f;
        } else if (PlayerFirearmsData.isUnlocked(player, "ammo_smithing_mastery_ii")) {
            chance = 0.25f;
        }
        return random.nextFloat() < chance ? 1 : 0;
    }

    public static boolean isGunsRpgAmmo(ItemStack stack) {
        var key = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (key == null || !"gunsrpg".equals(key.getNamespace())) {
            return false;
        }
        String path = key.getPath();
        return path.endsWith("_9mm")
                || path.endsWith("_45acp")
                || path.endsWith("_556mm")
                || path.endsWith("_762mm")
                || path.endsWith("_magnum")
                || path.endsWith("_12g");
    }

    public static boolean isMagnum(ItemStack stack) {
        var key = ForgeRegistries.ITEMS.getKey(stack.getItem());
        return key != null && key.getPath().endsWith("_magnum");
    }
}
