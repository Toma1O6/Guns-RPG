package com.wf.firearms.airdrop;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * 空投基础矿物：对齐子弹装配解锁天数（下界合金之前），按世界天数决定可用材质与数量 16–64。
 */
public final class AirdropMineralLoot {
    private static final int COUNT_MIN = 16;
    private static final int COUNT_MAX = 64;
    private static final long NETHERITE_RARE_MIN_DAY = 100L;

    /** unlockDay 对齐 config 中 *_ammo_smith 的 level（略调早期三金属为 7 天可出）。 */
    private static final MineralDef[] MINERALS = {
            MineralDef.early(7, "minecraft:copper_ingot"),
            MineralDef.early(10, "minecraft:iron_ingot"),
            MineralDef.early(15, "minecraft:gold_ingot"),
            new MineralDef(18, "minecraft:lapis_lazuli"),
            new MineralDef(20, "minecraft:redstone"),
            new MineralDef(22, "minecraft:emerald"),
            new MineralDef(25, "minecraft:quartz"),
            new MineralDef(32, "mmt:brass_ingot", "create:brass_ingot"),
            new MineralDef(36, "mmt:bronze_ingot"),
            new MineralDef(40, "minecraft:diamond"),
            new MineralDef(44, "mmt:steel_ingot"),
            new MineralDef(48, "minecraft:netherite_ingot", true),
    };

    private AirdropMineralLoot() {}

    public static void addToList(NonNullList<ItemStack> slots, RandomSource random, long worldDay) {
        int rolls = mineralRollCount(random, worldDay);
        for (int i = 0; i < rolls; i++) {
            ItemStack stack = rollStack(random, worldDay);
            if (!stack.isEmpty()) {
                addToList(slots, stack);
            }
        }
    }

    private static int mineralRollCount(RandomSource random, long worldDay) {
        if (worldDay < 7) {
            return 0;
        }
        if (worldDay < 15) {
            return random.nextFloat() < 0.65f ? 1 : 0;
        }
        if (worldDay < 40) {
            return 1;
        }
        if (worldDay < 100) {
            return 2;
        }
        return 3;
    }

    private static ItemStack rollStack(RandomSource random, long worldDay) {
        if (worldDay >= NETHERITE_RARE_MIN_DAY && random.nextFloat() < netheriteRareChance(worldDay)) {
            Item netherite = resolveItem(MINERALS[MINERALS.length - 1]);
            if (netherite != null) {
                return new ItemStack(netherite, rollCount(random, worldDay));
            }
        }

        float[] weights = new float[MINERALS.length];
        float total = 0f;
        for (int i = 0; i < MINERALS.length; i++) {
            MineralDef def = MINERALS[i];
            if (def.rareOnly) {
                continue;
            }
            if (worldDay < 15) {
                if (!def.earlyMetal) {
                    continue;
                }
                weights[i] = 1f;
                total += 1f;
                continue;
            }
            if (worldDay < def.unlockDay) {
                continue;
            }
            float w = 1f + (worldDay - def.unlockDay) * 0.04f;
            weights[i] = w;
            total += w;
        }
        if (total <= 0f) {
            return ItemStack.EMPTY;
        }

        float roll = random.nextFloat() * total;
        float acc = 0f;
        for (int i = 0; i < MINERALS.length; i++) {
            if (weights[i] <= 0f) {
                continue;
            }
            acc += weights[i];
            if (roll < acc) {
                Item item = resolveItem(MINERALS[i]);
                if (item == null) {
                    return ItemStack.EMPTY;
                }
                return new ItemStack(item, rollCount(random, worldDay));
            }
        }
        return ItemStack.EMPTY;
    }

    /** 第 100 天约 8%，第 140 天约 12%。 */
    private static float netheriteRareChance(long worldDay) {
        float day = Math.min(140f, (float) worldDay);
        return 0.08f + (day - NETHERITE_RARE_MIN_DAY) / 1000f;
    }

    private static int rollCount(RandomSource random, long worldDay) {
        if (worldDay < 15) {
            return COUNT_MIN;
        }
        float progress = Math.min(1f, worldDay / 140f);
        int base = COUNT_MIN + Math.round(progress * (COUNT_MAX - COUNT_MIN));
        int jitter = random.nextInt(9) - 4;
        return Math.max(COUNT_MIN, Math.min(COUNT_MAX, base + jitter));
    }

    private static Item resolveItem(MineralDef def) {
        Item item = itemFromId(def.primaryId);
        if (item != null) {
            return item;
        }
        if (def.fallbackId != null) {
            return itemFromId(def.fallbackId);
        }
        return null;
    }

    private static Item itemFromId(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }
        return ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(id));
    }

    private static void addToList(NonNullList<ItemStack> slots, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i).isEmpty()) {
                slots.set(i, stack);
                return;
            }
        }
    }

    private record MineralDef(int unlockDay, String primaryId, String fallbackId, boolean rareOnly, boolean earlyMetal) {
        MineralDef(int unlockDay, String primaryId) {
            this(unlockDay, primaryId, null, false, false);
        }

        MineralDef(int unlockDay, String primaryId, String fallbackId) {
            this(unlockDay, primaryId, fallbackId, false, false);
        }

        MineralDef(int unlockDay, String primaryId, boolean rareOnly) {
            this(unlockDay, primaryId, null, rareOnly, false);
        }

        MineralDef(int unlockDay, String primaryId, boolean rareOnly, boolean earlyMetal) {
            this(unlockDay, primaryId, null, rareOnly, earlyMetal);
        }

        static MineralDef early(int unlockDay, String primaryId) {
            return new MineralDef(unlockDay, primaryId, null, false, true);
        }
    }
}
