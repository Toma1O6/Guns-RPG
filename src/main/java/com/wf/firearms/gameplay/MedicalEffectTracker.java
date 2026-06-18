package com.wf.firearms.gameplay;

import com.wf.firearms.data.PlayerFirearmsData;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.player.Player;

/**
 * 记录由 Guns RPG 医疗品施加的药水效果，用于与药水/信标等外部来源区分叠层规则。
 */
public final class MedicalEffectTracker {
    private static final String KEY = "med_effect_sources";

    private MedicalEffectTracker() {}

    public static boolean isFromMedical(Player player, MobEffect effect) {
        ResourceLocation id = BuiltInRegistries.MOB_EFFECT.getKey(effect);
        if (id == null) {
            return false;
        }
        return root(player).getBoolean(id.toString());
    }

    public static void markFromMedical(Player player, MobEffect effect) {
        ResourceLocation id = BuiltInRegistries.MOB_EFFECT.getKey(effect);
        if (id != null) {
            root(player).putBoolean(id.toString(), true);
        }
    }

    public static void unmark(Player player, MobEffect effect) {
        ResourceLocation id = BuiltInRegistries.MOB_EFFECT.getKey(effect);
        if (id != null) {
            root(player).remove(id.toString());
        }
    }

    /** 移除已过期或玩家身上已无对应效果的标记。 */
    public static void pruneStale(Player player) {
        CompoundTag tag = root(player);
        for (String key : tag.getAllKeys().toArray(String[]::new)) {
            if (!key.contains(":")) {
                continue;
            }
            ResourceLocation effectId = ResourceLocation.tryParse(key);
            if (effectId == null) {
                tag.remove(key);
                continue;
            }
            MobEffect effect = BuiltInRegistries.MOB_EFFECT.get(effectId);
            if (effect == null || player.getEffect(effect) == null) {
                tag.remove(key);
            }
        }
    }

    private static CompoundTag root(Player player) {
        CompoundTag guns = PlayerFirearmsData.root(player);
        if (!guns.contains(KEY)) {
            guns.put(KEY, new CompoundTag());
        }
        return guns.getCompound(KEY);
    }
}
