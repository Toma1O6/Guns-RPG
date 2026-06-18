package com.wf.firearms.combat;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;

/** 弹药材质梯队（对标 Guns RPG {@link dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials}）。 */
public enum AmmoMaterial {
    WOOD("wood", "wooden", 0.4f, 0),
    STONE("stone", "stone", 0.6f, 1),
    COPPER("copper", "copper", 0.8f, 2),
    IRON("iron", "iron", 1.0f, 3),
    LAPIS("lapis", "lapis", 0.95f, 3),
    GOLD("gold", "gold", 1.15f, 4),
    REDSTONE("redstone", "redstone", 1.2f, 4),
    EMERALD("emerald", "emerald", 1.35f, 5),
    QUARTZ("quartz", "quartz", 1.4f, 5),
    DIAMOND("diamond", "diamond", 1.55f, 6),
    NETHERITE("netherite", "netherite", 2.0f, 8),
    BRONZE("bronze", "bronze", 1.40f, 7),
    BRASS("brass", "brass", 1.55f, 7),
    INVAR("invar", "invar", 1.65f, 7),
    STEEL("steel", "steel", 1.80f, 7),
    NEPTUNIUM("neptunium", "neptunium", 2.85f, 8),
    STARINIUM("starinium", "starinium", 3.20f, 8),
    ULTIMATE("ultimate", "ultimate", 3.60f, 8),
    VOID("void", "void", 4.50f, 9),
    STELLAR("stellar", "stellar", 5.00f, 9),
    STARLIGHT_MYTHRIL("starlight_mythril", "starlight_mythril", 5.35f, 9),
    DARK_CRYOPLA("dark_cryopla", "dark_cryopla", 5.70f, 9),
    COSMOS_AURORA("cosmos_aurora", "cosmos_aurora", 7.50f, 10),
    ABIDING_ALLOY("abiding_alloy", "abiding_alloy", 8.80f, 10),
    UNKNOWN("unknown", "", 0.5f, -1);

    private final String id;
    private final String itemPrefix;
    private final float damageMultiplier;
    private final int tier;

    AmmoMaterial(String id, String itemPrefix, float damageMultiplier, int tier) {
        this.id = id;
        this.itemPrefix = itemPrefix;
        this.damageMultiplier = damageMultiplier;
        this.tier = tier;
    }

    public String getId() {
        return id;
    }

    public String itemPrefix() {
        return itemPrefix;
    }

    public float damageMultiplier() {
        return damageMultiplier;
    }

    public int tier() {
        return tier;
    }

    public static AmmoMaterial fromItem(Item item) {
        ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
        if (key == null) {
            return UNKNOWN;
        }
        return fromItemId(key.getPath());
    }

    public static AmmoMaterial fromItemId(String path) {
        AmmoMaterial best = UNKNOWN;
        int bestLen = -1;
        for (AmmoMaterial material : values()) {
            if (material == UNKNOWN || material.itemPrefix.isEmpty()) {
                continue;
            }
            String prefix = material.itemPrefix + "_";
            if (path.startsWith(prefix) && prefix.length() > bestLen) {
                best = material;
                bestLen = prefix.length();
            }
        }
        if (best != UNKNOWN) {
            return best;
        }
        return AmmoStatsRegistry.materialFromCustomId(path).orElse(UNKNOWN);
    }

    public static AmmoMaterial fromId(String id) {
        if (id == null || id.isEmpty()) {
            return UNKNOWN;
        }
        for (AmmoMaterial m : values()) {
            if (m.id.equals(id)) {
                return m;
            }
        }
        return UNKNOWN;
    }

    public static java.util.stream.Stream<AmmoMaterial> streamCraftable() {
        return Arrays.stream(values()).filter(m -> m != UNKNOWN);
    }
}
