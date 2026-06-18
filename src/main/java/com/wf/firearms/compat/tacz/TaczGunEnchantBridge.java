package com.wf.firearms.compat.tacz;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.config.TaczBackendConfig;
import com.wf.firearms.registry.ModGunEnchantments;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

/**
 * gunsrpg 自有枪械附魔 → TaCZ 枪 NBT；不依赖 CGM mod。
 * 旧存档 {@link #LEGACY_TAG}、旧书 {@code cgm:*} 会在同步时迁移到 {@link #TAG}。
 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class TaczGunEnchantBridge {
    public static final String TAG = "gunsrpg_gun_enchants";
    public static final String LEGACY_TAG = "gunsrpg_cgm_enchants";

    private TaczGunEnchantBridge() {}

    public static int level(ItemStack gun, String shortId) {
        if (gun == null || gun.isEmpty() || shortId == null || !gun.hasTag()) {
            return 0;
        }
        CompoundTag root = gun.getTag();
        int lv = 0;
        if (root.contains(TAG)) {
            lv = root.getCompound(TAG).getInt(shortId);
        }
        if (lv <= 0 && root.contains(LEGACY_TAG)) {
            lv = root.getCompound(LEGACY_TAG).getInt(shortId);
        }
        return lv;
    }

    public static void refreshFromItemEnchantments(ItemStack gun) {
        if (!TaczBackendConfig.useTaczShooting() || !TaczBridge.isTaczGun(gun)) {
            return;
        }
        migrateLegacyTag(gun);
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(gun);
        if (enchants.isEmpty()) {
            return;
        }
        CompoundTag stored = gun.getOrCreateTag().getCompound(TAG);
        boolean changed = false;
        for (Map.Entry<Enchantment, Integer> e : enchants.entrySet()) {
            String shortId = resolveShortId(e.getKey());
            if (shortId == null) {
                continue;
            }
            int lv = Math.max(stored.getInt(shortId), e.getValue());
            if (lv > 0 && lv != stored.getInt(shortId)) {
                stored.putInt(shortId, lv);
                changed = true;
            }
        }
        if (changed) {
            gun.getOrCreateTag().put(TAG, stored);
        }
    }

    private static void migrateLegacyTag(ItemStack gun) {
        if (!gun.hasTag() || !gun.getTag().contains(LEGACY_TAG)) {
            return;
        }
        CompoundTag legacy = gun.getTag().getCompound(LEGACY_TAG);
        CompoundTag target = gun.getOrCreateTag().getCompound(TAG);
        boolean changed = false;
        for (String id : ModGunEnchantments.ALL_IDS) {
            int lv = legacy.getInt(id);
            if (lv > target.getInt(id)) {
                target.putInt(id, lv);
                changed = true;
            }
        }
        if (changed) {
            gun.getOrCreateTag().put(TAG, target);
        }
    }

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        if (!TaczBackendConfig.useTaczShooting()) {
            return;
        }
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        if (!TaczBridge.isTaczGun(left)) {
            return;
        }
        Map<Enchantment, Integer> fromBook = extractBookEnchants(right);
        if (fromBook.isEmpty()) {
            return;
        }
        ItemStack out = left.copy();
        CompoundTag stored = out.getOrCreateTag().getCompound(TAG);
        int cost = 2;
        boolean applied = false;
        Map<Enchantment, Integer> onGun = new HashMap<>(EnchantmentHelper.getEnchantments(out));

        for (Map.Entry<Enchantment, Integer> e : fromBook.entrySet()) {
            String shortId = resolveShortId(e.getKey());
            if (shortId == null) {
                continue;
            }
            Enchantment gunsrpgEnch = e.getKey();
            ResourceLocation enchId = ForgeRegistries.ENCHANTMENTS.getKey(gunsrpgEnch);
            if (enchId == null) {
                continue;
            }
            if (!"gunsrpg".equals(enchId.getNamespace())) {
                Enchantment mapped = mapLegacyCgmEnchant(e.getKey());
                if (mapped == null) {
                    continue;
                }
                gunsrpgEnch = mapped;
                shortId = enchId.getPath();
            }
            int max = gunsrpgEnch.getMaxLevel();
            int newLv = Math.min(max, Math.max(stored.getInt(shortId), e.getValue()));
            if (newLv > stored.getInt(shortId)) {
                stored.putInt(shortId, newLv);
                onGun.put(gunsrpgEnch, newLv);
                cost += newLv * 3;
                applied = true;
            }
        }
        if (!applied) {
            return;
        }
        out.getOrCreateTag().put(TAG, stored);
        EnchantmentHelper.setEnchantments(onGun, out);
        event.setOutput(out);
        event.setCost(Math.min(40, cost));
        event.setMaterialCost(1);
    }

    private static Enchantment mapLegacyCgmEnchant(Enchantment from) {
        ResourceLocation id = ForgeRegistries.ENCHANTMENTS.getKey(from);
        if (id == null || !"cgm".equals(id.getNamespace()) || !ModGunEnchantments.isGunEnchant(id.getPath())) {
            return null;
        }
        return ForgeRegistries.ENCHANTMENTS.getValue(
                ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, id.getPath()));
    }

    private static String resolveShortId(Enchantment enchantment) {
        ResourceLocation id = ForgeRegistries.ENCHANTMENTS.getKey(enchantment);
        if (id == null || !ModGunEnchantments.isGunEnchant(id.getPath())) {
            return null;
        }
        if ("gunsrpg".equals(id.getNamespace()) || "cgm".equals(id.getNamespace())) {
            return id.getPath();
        }
        return null;
    }

    private static Map<Enchantment, Integer> extractBookEnchants(ItemStack stack) {
        if (stack.isEmpty()) {
            return Map.of();
        }
        if (stack.is(Items.ENCHANTED_BOOK)) {
            return EnchantmentHelper.getEnchantments(stack);
        }
        return Map.of();
    }

    static float rpmMultiplier(ItemStack gun) {
        int lv = level(gun, "trigger_finger");
        return lv <= 0 ? 1f : 1f + 0.1f * lv;
    }

    static float magBonus(ItemStack gun) {
        int lv = level(gun, "over_capacity");
        return lv <= 0 ? 0f : 2f * lv;
    }

    static float reloadSpeedMultiplier(ItemStack gun) {
        int lv = level(gun, "quick_hands");
        return lv <= 0 ? 1f : Math.max(0.5f, 1f - 0.12f * lv);
    }

    static float ammoSpeedMultiplier(ItemStack gun) {
        int lv = level(gun, "accelerator");
        return lv <= 0 ? 1f : 1f + 0.1f * lv;
    }

    static int pierceBonus(ItemStack gun) {
        int lv = level(gun, "collateral");
        return lv <= 0 ? 0 : Math.min(3, lv);
    }

    static float puncturingCritChance(ItemStack gun) {
        int lv = level(gun, "puncturing");
        return lv <= 0 ? 0f : 0.08f * lv;
    }

    static float reclaimedChance(ItemStack gun) {
        int lv = level(gun, "reclaimed");
        return lv <= 0 ? 0f : 0.12f * lv;
    }

    static boolean hasFireStarter(ItemStack gun) {
        return level(gun, "fire_starter") > 0;
    }

    static float lightweightAdsFactor(ItemStack gun) {
        int lv = level(gun, "lightweight");
        return lv <= 0 ? 1f : Math.max(0.6f, 1f - 0.08f * lv);
    }
}
