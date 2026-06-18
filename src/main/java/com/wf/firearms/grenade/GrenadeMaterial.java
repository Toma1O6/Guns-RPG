package com.wf.firearms.grenade;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

/** 手雷可用金属/合金（不含宝石等非金属）。 */
public final class GrenadeMaterial {
    private final String id;
    private final float baseBlastRadius;
    /** 普通手雷对中心目标的基础伤害（破片/重型再乘种类倍率）。 */
    private final float baseDamage;
    private final Item ingot;
    private final Item plate;
    private final Item nugget;

    public GrenadeMaterial(
            String id,
            float baseBlastRadius,
            float baseDamage,
            Item ingot,
            Item plate,
            Item nugget) {
        this.id = id;
        this.baseBlastRadius = baseBlastRadius;
        this.baseDamage = baseDamage;
        this.ingot = ingot;
        this.plate = plate;
        this.nugget = nugget;
    }

    public String id() {
        return id;
    }

    public float baseBlastRadius() {
        return baseBlastRadius;
    }

    public float baseDamage() {
        return baseDamage;
    }

    public Item ingot() {
        return ingot;
    }

    public Item plate() {
        return plate;
    }

    public Item nugget() {
        return nugget;
    }

    public ResourceLocation ingotId() {
        return ForgeRegistries.ITEMS.getKey(ingot);
    }

    public ResourceLocation plateId() {
        return ForgeRegistries.ITEMS.getKey(plate);
    }

    public ResourceLocation nuggetId() {
        return ForgeRegistries.ITEMS.getKey(nugget);
    }

    public static GrenadeMaterial ironDefaults() {
        return new GrenadeMaterial(
                "iron", 2.5f, 20f, Items.IRON_INGOT, Items.IRON_INGOT, Items.IRON_NUGGET);
    }

    public static GrenadeMaterial goldDefaults() {
        return new GrenadeMaterial(
                "gold", 2.8f, 25f, Items.GOLD_INGOT, Items.GOLD_INGOT, Items.GOLD_NUGGET);
    }

    public static GrenadeMaterial netheriteDefaults() {
        return new GrenadeMaterial(
                "netherite",
                3.2f,
                50f,
                Items.NETHERITE_INGOT,
                Items.NETHERITE_INGOT,
                Items.NETHERITE_SCRAP);
    }
}
