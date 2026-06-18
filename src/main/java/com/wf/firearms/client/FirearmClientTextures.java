package com.wf.firearms.client;

import com.wf.firearms.GunsRpg;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

/** 枪械贴图编入方块图集（见 assets/minecraft/atlases/blocks.json），供 BEWLR / 模型 UV 采样。 */
public final class FirearmClientTextures {
    public static final ResourceLocation WEAPON_SPRITE =
            ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "item/weapon_texture_map");

    private static TextureAtlasSprite weaponSprite;

    private FirearmClientTextures() {}

    public static TextureAtlasSprite getWeaponSprite() {
        if (weaponSprite == null) {
            weaponSprite =
                    Minecraft.getInstance()
                            .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                            .apply(WEAPON_SPRITE);
        }
        return weaponSprite;
    }

    public static void clearCache() {
        weaponSprite = null;
    }
}
