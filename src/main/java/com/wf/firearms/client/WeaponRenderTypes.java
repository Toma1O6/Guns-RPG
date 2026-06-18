package com.wf.firearms.client;

import com.wf.firearms.GunsRpg;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public final class WeaponRenderTypes {
    public static final ResourceLocation WEAPON_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(
                    GunsRpg.MOD_ID, "textures/item/weapon_texture_map.png");

    private static final Function<ResourceLocation, RenderType> CUTOUT =
            Util.memoize(loc -> RenderType.entityCutoutNoCull(loc));

    private WeaponRenderTypes() {}

    public static RenderType weaponCutout() {
        return CUTOUT.apply(WEAPON_TEXTURE);
    }
}
