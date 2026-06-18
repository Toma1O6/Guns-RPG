package com.wf.firearms.mob;

import com.wf.firearms.combat.AmmoMaterial;
import com.wf.firearms.combat.FirearmRegistry;
import com.wf.firearms.combat.FirearmSpec;
import com.wf.firearms.config.GunnerLoadoutConfig;
import com.wf.firearms.config.WeaponLevelConfig;
import com.wf.firearms.data.ActiveWeaponCatalog;
import com.wf.firearms.registry.ModItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class GunnerLoadoutPicker {
    public record PickedLoadout(String gunKey, AmmoMaterial ammo, float damageMultiplier, int fireIntervalTicks) {}

    private GunnerLoadoutPicker() {}

    public static PickedLoadout pick(RandomSource random, float combinedDifficulty, int gunLevelCap) {
        List<GunnerLoadoutConfig.LoadoutEntry> eligible = new ArrayList<>();
        for (GunnerLoadoutConfig.LoadoutEntry e : GunnerLoadoutConfig.loadouts()) {
            if (!ActiveWeaponCatalog.isActiveWeaponKey(e.gun())) {
                continue;
            }
            if (WeaponLevelConfig.levelForGun(e.gun()) > gunLevelCap) {
                continue;
            }
            if (combinedDifficulty + 0.01f < e.minDifficulty()) {
                continue;
            }
            eligible.add(e);
        }
        if (eligible.isEmpty()) {
            return fallback(random);
        }
        int total = 0;
        for (GunnerLoadoutConfig.LoadoutEntry e : eligible) {
            int gunLevel = WeaponLevelConfig.levelForGun(e.gun());
            int bias = Math.max(1, gunLevel / 5);
            total += e.weight() * bias;
        }
        int roll = random.nextInt(Math.max(1, total));
        int acc = 0;
        for (GunnerLoadoutConfig.LoadoutEntry e : eligible) {
            int gunLevel = WeaponLevelConfig.levelForGun(e.gun());
            int bias = Math.max(1, gunLevel / 5);
            acc += e.weight() * bias;
            if (roll < acc) {
                return toPicked(e, combinedDifficulty);
            }
        }
        return toPicked(eligible.get(eligible.size() - 1), combinedDifficulty);
    }

    private static PickedLoadout fallback(RandomSource random) {
        var list = GunnerLoadoutConfig.loadouts();
        if (list.isEmpty()) {
            return new PickedLoadout("m1911", AmmoMaterial.WOOD, 0.4f, 14);
        }
        var e = list.get(random.nextInt(list.size()));
        return toPicked(e, 0f);
    }

    private static PickedLoadout toPicked(GunnerLoadoutConfig.LoadoutEntry e, float combinedDifficulty) {
        AmmoMaterial mat = AmmoMaterial.fromId(e.ammo());
        float dmg =
                GunnerLoadoutConfig.damageMultiplier()
                        * (1f + combinedDifficulty * GunnerLoadoutConfig.difficultyDamageScale());
        FirearmSpec spec = FirearmRegistry.getOrDefault(e.gun());
        int interval =
                Math.max(
                        1,
                        Math.round(
                                spec.fireIntervalTicks()
                                        * com.wf.firearms.config.MobSpawnConfig.global().fireIntervalMultiplier()));
        return new PickedLoadout(e.gun(), mat, dmg, interval);
    }

    public static ItemStack gunStack(String gunKey) {
        Item item = ModItems.gunItem(gunKey);
        return item != null ? new ItemStack(item) : ItemStack.EMPTY;
    }
}
