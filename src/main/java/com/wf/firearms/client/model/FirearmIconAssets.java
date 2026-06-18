package com.wf.firearms.client.model;

import com.wf.firearms.GunsRpg;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

/** 枪械创造栏 2D 图标：贴图在 {@code textures/item/{key}_gun_icon.png}（与弹药同规则进图集）。 */
public final class FirearmIconAssets {
    private FirearmIconAssets() {}

    public static ResourceLocation textureId(String weaponKey) {
        return ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, weaponKey + "_gun_icon");
    }

    public static ResourceLocation modelId(String weaponKey) {
        return ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "item/icon/" + weaponKey);
    }

    public static ModelResourceLocation inventoryModel(String weaponKey) {
        return new ModelResourceLocation(modelId(weaponKey), "inventory");
    }
}
