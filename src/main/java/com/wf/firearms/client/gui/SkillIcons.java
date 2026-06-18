package com.wf.firearms.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.wf.firearms.GunsRpg;
import com.wf.firearms.compat.tacz.TaczSlotIconPaths;
import com.wf.firearms.config.TaczBackendConfig;
import com.wf.firearms.data.WeaponMapping;
import com.wf.firearms.registry.ModBlocks;
import com.wf.firearms.registry.ModItems;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import javax.annotation.Nullable;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/** Guns RPG 技能格图标：{@code textures/icons/<id>.png} 或 2D 枪条图。 */
public final class SkillIcons {
    private static final ResourceLocation UNKNOWN =
            ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "textures/icons/unknown.png");

    private static final Map<String, ResourceLocation> CACHE = new HashMap<>();

    private SkillIcons() {}

    public static ResourceLocation textureFor(String skillId) {
        if (skillId == null || skillId.isEmpty()) {
            return UNKNOWN;
        }
        return CACHE.computeIfAbsent(skillId, SkillIcons::resolve);
    }

    private static ResourceLocation resolve(String skillId) {
        ResourceLocation direct = icon(skillId);
        if (resourceExists(direct)) {
            return direct;
        }
        ResourceLocation resistanceBase = resistanceBaseIcon(skillId);
        if (resistanceBase != null && resourceExists(resistanceBase)) {
            return resistanceBase;
        }
        if (skillId.startsWith("ammo_smithing_mastery_")) {
            ResourceLocation base = icon("ammo_smithing_mastery");
            if (resourceExists(base)) {
                return base;
            }
        }
        if (skillId.endsWith("_assembly")) {
            String gunKey = SkillIconAssets.gunIconKeyForAssembly(skillId);
            if (TaczBackendConfig.useTaczShooting()) {
                ResourceLocation taczSlot = TaczSlotIconPaths.slotTexture(gunKey).orElse(null);
                if (taczSlot != null && resourceExists(taczSlot)) {
                    return taczSlot;
                }
            }
            ResourceLocation gunIcon = gunIconTexture(gunKey);
            if (resourceExists(gunIcon)) {
                return gunIcon;
            }
            ResourceLocation baseIcon = icon(gunKey);
            if (resourceExists(baseIcon)) {
                return baseIcon;
            }
        }
        return UNKNOWN;
    }

    @Nullable
    private static ResourceLocation resistanceBaseIcon(String skillId) {
        if (skillId.startsWith("bleeding_resistance_")) {
            return icon("bleed");
        }
        if (skillId.startsWith("fracture_resistance_")) {
            return icon("fracture");
        }
        if (skillId.startsWith("poison_resistance_")) {
            return icon("poison");
        }
        if (skillId.startsWith("infection_resistance_")) {
            return icon("infection");
        }
        if (skillId.startsWith("pharmacist_")) {
            return icon("pharmacist_i");
        }
        if ("doctor".equals(skillId)) {
            return icon("medic");
        }
        return null;
    }

    private static ItemStack itemIconStack(String skillId) {
        if (skillId == null || skillId.isEmpty()) {
            return ItemStack.EMPTY;
        }
        if (skillId.endsWith("_assembly")) {
            ItemStack gun = WeaponMapping.iconForAssembly(skillId);
            return gun.isEmpty() ? ItemStack.EMPTY : gun;
        }
        if (skillId.endsWith("_ammo_smith") && !"wooden_ammo_smith".equals(skillId)) {
            String material = skillId.substring(0, skillId.length() - "_ammo_smith".length());
            if (!material.isEmpty()) {
                ResourceLocation itemId =
                        ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, material + "_9mm");
                Item item = ForgeRegistries.ITEMS.getValue(itemId);
                if (item != null) {
                    return new ItemStack(item);
                }
            }
        }
        return switch (skillId) {
            case "massive_grenades" -> stackOrEmpty(ModItems.MASSIVE_GRENADE);
            case "impact_grenades" -> stackOrEmpty(ModItems.IMPACT_GRENADE);
            case "grenades" -> stackOrEmpty(ModItems.GRENADE);
            default -> ItemStack.EMPTY;
        };
    }

    private static ItemStack stackOrEmpty(net.minecraftforge.registries.RegistryObject<Item> item) {
        return item != null && item.isPresent() ? new ItemStack(item.get()) : ItemStack.EMPTY;
    }

    private static ResourceLocation icon(String name) {
        return ResourceLocation.fromNamespaceAndPath(GunsRpg.MOD_ID, "textures/icons/" + name + ".png");
    }

    private static ResourceLocation gunIconTexture(String weaponKey) {
        return ResourceLocation.fromNamespaceAndPath(
                GunsRpg.MOD_ID, "textures/item/" + weaponKey + "_gun_icon.png");
    }

    private static boolean resourceExists(ResourceLocation loc) {
        ResourceManager mgr = net.minecraft.client.Minecraft.getInstance().getResourceManager();
        Optional<Resource> res = mgr.getResource(loc);
        return res.isPresent();
    }

    /** 22×22 节点内图标 */
    public static void blitNodeIcon(GuiGraphics gfx, String skillId, int x, int y, int size) {
        ItemStack itemIcon = itemIconStack(skillId);
        if (!itemIcon.isEmpty()) {
            int pad = Math.max(0, (size - 16) / 2);
            gfx.renderItem(itemIcon, x + pad, y + pad);
            return;
        }
        if ("repair_station".equals(skillId)) {
            int pad = Math.max(0, (size - 16) / 2);
            gfx.renderItem(new ItemStack(ModBlocks.REPAIR_STATION_ITEM.get()), x + pad, y + pad);
            return;
        }
        if ("medical_station".equals(skillId)) {
            int pad = Math.max(0, (size - 16) / 2);
            gfx.renderItem(new ItemStack(ModBlocks.MEDICAL_STATION_ITEM.get()), x + pad, y + pad);
            return;
        }
        ResourceLocation tex = textureFor(skillId);
        if (resourceExists(tex) && tex != UNKNOWN) {
            blitIcon(gfx, tex, x, y, size);
            return;
        }
        blitIcon(gfx, UNKNOWN, x, y, size);
    }

    private static void blitIcon(GuiGraphics gfx, ResourceLocation tex, int x, int y, int size) {
        int pad = Math.max(0, (size - 16) / 2);
        RenderSystem.enableBlend();
        gfx.blit(tex, x + pad, y + pad, 0, 0, 16, 16, 16, 16);
    }
}
