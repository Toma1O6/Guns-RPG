package com.wf.firearms.client.model;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/** 烘焙阶段缓存的枪械 2D 图标模型（创造栏用）。 */
@OnlyIn(Dist.CLIENT)
public final class FirearmGuiIconModels {
    private static final Map<String, BakedModel> BY_WEAPON_KEY = new HashMap<>();

    private FirearmGuiIconModels() {}

    public static void put(String weaponKey, BakedModel iconModel) {
        if (iconModel != null) {
            BY_WEAPON_KEY.put(weaponKey, iconModel);
        }
    }

    public static BakedModel get(String weaponKey) {
        return BY_WEAPON_KEY.get(weaponKey);
    }

    public static Map<String, BakedModel> all() {
        return Collections.unmodifiableMap(BY_WEAPON_KEY);
    }

    public static void clear() {
        BY_WEAPON_KEY.clear();
    }
}
