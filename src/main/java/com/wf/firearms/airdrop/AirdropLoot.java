package com.wf.firearms.airdrop;

import com.wf.firearms.combat.AmmoMaterial;
import com.wf.firearms.compat.tacz.TaczGiveGun;
import com.wf.firearms.config.AirdropConfig;
import com.wf.firearms.config.TaczBackendConfig;
import com.wf.firearms.registry.ModItems;
import net.minecraft.core.NonNullList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** 空投箱战利品：按世界天数调节材质梯队权重；每名玩家首次打开时单独生成。 */
public final class AirdropLoot {
    private static final int BULLET_STACK = 64;
    private static final long MAGNUM_MIN_DAY = 70L;

    private static final AmmoMaterial[] VANILLA_MATERIALS = {
            AmmoMaterial.WOOD,
            AmmoMaterial.STONE,
            AmmoMaterial.COPPER,
            AmmoMaterial.IRON,
            AmmoMaterial.LAPIS,
            AmmoMaterial.GOLD,
            AmmoMaterial.REDSTONE,
            AmmoMaterial.EMERALD,
            AmmoMaterial.QUARTZ,
            AmmoMaterial.DIAMOND,
            AmmoMaterial.NETHERITE
    };
    private static final AmmoMaterial[] T3_MATERIALS = {
            AmmoMaterial.BRONZE, AmmoMaterial.BRASS, AmmoMaterial.INVAR, AmmoMaterial.STEEL
    };
    private static final AmmoMaterial[] T2_MATERIALS = {
            AmmoMaterial.NEPTUNIUM, AmmoMaterial.STARINIUM, AmmoMaterial.ULTIMATE
    };
    private static final AmmoMaterial[] T1_MATERIALS = {
            AmmoMaterial.VOID,
            AmmoMaterial.STELLAR,
            AmmoMaterial.STARLIGHT_MYTHRIL,
            AmmoMaterial.DARK_CRYOPLA
    };
    private static final AmmoMaterial[] T0_MATERIALS = {
            AmmoMaterial.COSMOS_AURORA, AmmoMaterial.ABIDING_ALLOY
    };

    private static final String[] SMALL_CALIBERS = {"9mm", "45acp"};
    private static final String[] LARGE_CALIBERS = {"556mm", "762mm"};

    private static final Item[] FOODS = {
            ModItems.BACON_BURGER.get(),
            ModItems.FISH_AND_CHIPS.get(),
            ModItems.GARDEN_SOUP.get(),
            ModItems.CHICKEN_DINNER.get(),
            ModItems.FRIES.get(),
            ModItems.FRIED_EGG.get(),
            ModItems.CHICKEN_NUGGETS.get(),
            ModItems.SCHNITZEL.get(),
            ModItems.DOUGHNUT.get(),
            ModItems.FRUIT_SALAD.get(),
            ModItems.EGG_SALAD.get()
    };

    private static final Item[] MEDS = {
            ModItems.BANDAGE.get(),
            ModItems.HEMOSTAT.get(),
            ModItems.FIELD_BANDAGE.get(),
            ModItems.ANALGETICS.get(),
            ModItems.PAINKILLERS.get(),
            ModItems.ANTIDOTUM_PILLS.get(),
            ModItems.PLASTER_CAST.get()
    };

    private AirdropLoot() {}

    public static void fill(Container container, RandomSource random, long worldDay) {
        NonNullList<ItemStack> scratch = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
        fillList(scratch, random, worldDay);
        for (int i = 0; i < container.getContainerSize(); i++) {
            container.setItem(i, scratch.get(i));
        }
    }

    public static void fillList(NonNullList<ItemStack> slots, RandomSource random, long worldDay) {
        for (int i = 0; i < slots.size(); i++) {
            slots.set(i, ItemStack.EMPTY);
        }

        addToList(slots, ammoStack(random, worldDay, pick(random, SMALL_CALIBERS), BULLET_STACK));
        addToList(slots, ammoStack(random, worldDay, pick(random, LARGE_CALIBERS), BULLET_STACK));
        addToList(slots, ammoStack(random, worldDay, "12g", BULLET_STACK));

        if (tryRollMagnum(random, worldDay)) {
            addToList(slots, ammoStack(random, worldDay, "magnum", BULLET_STACK));
        }

        addToList(slots, new ItemStack(ModItems.WEAPON_REPAIR_KIT.get(), 1));

        int foodKinds = 1 + random.nextInt(3);
        for (Item food : pickDistinct(random, FOODS, foodKinds)) {
            addToList(slots, new ItemStack(food, 8 + random.nextInt(25)));
        }

        int medKinds = 1 + random.nextInt(3);
        for (Item med : pickDistinct(random, MEDS, medKinds)) {
            addToList(slots, new ItemStack(med, 2 + random.nextInt(7)));
        }

        AirdropMineralLoot.addToList(slots, random, worldDay);

        if (random.nextFloat() < AirdropConfig.grenadeDropChance(worldDay)) {
            addToList(
                    slots,
                    new ItemStack(
                            ModItems.GRENADE.get(),
                            AirdropConfig.rollGrenadeCount(random)));
        }

        if (random.nextFloat() < AirdropConfig.gunDropChance()) {
            ItemStack gun = rollAirdropGun(random);
            if (!gun.isEmpty()) {
                addToList(slots, gun);
            }
        }
    }

    private static ItemStack rollAirdropGun(RandomSource random) {
        String key = AirdropConfig.rollGunKey(random).orElse(null);
        if (key == null || key.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (TaczBackendConfig.useTaczShooting()) {
            ItemStack tacz = TaczGiveGun.create(key, true);
            if (!tacz.isEmpty()) {
                return tacz;
            }
        }
        return ModItems.ITEMS.getEntries().stream()
                .filter(ro -> ro.getId().getPath().equals(key))
                .findFirst()
                .map(ro -> new ItemStack(ro.get(), 1))
                .orElse(ItemStack.EMPTY);
    }

    private static ItemStack ammoStack(
            RandomSource random, long worldDay, String caliberSuffix, int count) {
        AmmoMaterial material = rollMaterial(random, worldDay);
        Item item = resolveAmmo(material, caliberSuffix);
        if (item == null) {
            item = ModItems.WOODEN_9MM.get();
        }
        return new ItemStack(item, count);
    }

    private static Item resolveAmmo(AmmoMaterial material, String caliberSuffix) {
        if ("magnum".equals(caliberSuffix)
                && (material == AmmoMaterial.WOOD || material == AmmoMaterial.STONE)) {
            return null;
        }
        var ro = ModItems.ammo(material.itemPrefix() + "_" + caliberSuffix);
        return ro != null && ro.isPresent() ? ro.get() : null;
    }

    private static AmmoMaterial rollMaterial(RandomSource random, long worldDay) {
        float[] weights = baseTierWeights(worldDay);
        applyTierCaps(weights);
        int tier = weightedIndex(random, weights);
        return switch (tier) {
            case 0 -> pick(random, VANILLA_MATERIALS);
            case 1 -> pick(random, T3_MATERIALS);
            case 2 -> pick(random, T2_MATERIALS);
            case 3 -> pick(random, T1_MATERIALS);
            default -> pick(random, T0_MATERIALS);
        };
    }

    /** 索引 0=原版材质 … 4=T0；随天数略抬高端权重。 */
    private static float[] baseTierWeights(long worldDay) {
        float day = Math.min(120f, (float) worldDay);
        float scale = day / 120f;
        return new float[] {
            100f - scale * 18f,
            22f + scale * 8f,
            12f + scale * 10f,
            5f + scale * 8f,
            1.5f + scale * 6f
        };
    }

    /** T2≤20%、T1≤10%、T0≤5%（归一化前按份额封顶后重分配）。 */
    private static void applyTierCaps(float[] weights) {
        float sum = sum(weights);
        if (sum <= 0f) {
            return;
        }
        float[] share = new float[weights.length];
        for (int i = 0; i < weights.length; i++) {
            share[i] = weights[i] / sum;
        }
        float maxT2 = 0.20f;
        float maxT1 = 0.10f;
        float maxT0 = 0.05f;
        float overflow = 0f;
        if (share[4] > maxT0) {
            overflow += share[4] - maxT0;
            share[4] = maxT0;
        }
        if (share[3] > maxT1) {
            overflow += share[3] - maxT1;
            share[3] = maxT1;
        }
        if (share[2] > maxT2) {
            overflow += share[2] - maxT2;
            share[2] = maxT2;
        }
        share[0] += overflow;
        float newSum = sum(share);
        for (int i = 0; i < weights.length; i++) {
            weights[i] = share[i] / newSum * sum;
        }
    }

    private static float magnumRollChance(long worldDay) {
        if (worldDay < MAGNUM_MIN_DAY) {
            return 0f;
        }
        float day = Math.min(120f, (float) worldDay);
        float chance = 0.004f + (day - MAGNUM_MIN_DAY) / 120f * 0.046f;
        return Math.min(0.05f, chance);
    }

    private static boolean tryRollMagnum(RandomSource random, long worldDay) {
        return random.nextFloat() < magnumRollChance(worldDay);
    }

    private static int weightedIndex(RandomSource random, float[] weights) {
        float total = sum(weights);
        float roll = random.nextFloat() * total;
        float acc = 0f;
        for (int i = 0; i < weights.length; i++) {
            acc += weights[i];
            if (roll < acc) {
                return i;
            }
        }
        return 0;
    }

    private static float sum(float[] values) {
        float s = 0f;
        for (float v : values) {
            s += v;
        }
        return s;
    }

    private static <T> T pick(RandomSource random, T[] array) {
        return array[random.nextInt(array.length)];
    }

    private static List<Item> pickDistinct(RandomSource random, Item[] pool, int count) {
        List<Item> copy = new ArrayList<>(List.of(pool));
        List<Item> out = new ArrayList<>();
        int n = Math.min(count, copy.size());
        for (int i = 0; i < n; i++) {
            int idx = random.nextInt(copy.size());
            out.add(copy.remove(idx));
        }
        return out;
    }

    private static void add(Container container, ItemStack stack) {
        if (stack.isEmpty()) {
            return;
        }
        for (int i = 0; i < container.getContainerSize(); i++) {
            if (container.getItem(i).isEmpty()) {
                container.setItem(i, stack);
                return;
            }
        }
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
}
