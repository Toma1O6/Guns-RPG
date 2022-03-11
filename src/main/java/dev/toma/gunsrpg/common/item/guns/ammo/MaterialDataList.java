package dev.toma.gunsrpg.common.item.guns.ammo;

import dev.toma.gunsrpg.util.ModUtils;

public final class MaterialDataList {

    public static final IMaterialDataContainer CONTAINER_9MM = ModUtils.init(new MaterialDataContainer(), container -> {
        container.register(AmmoMaterials.WOOD,      new MaterialData(-0.1F, 0.2F, 0.0F));
        container.register(AmmoMaterials.STONE,     new MaterialData(0.1F, 0.0F, 0.15F));
        container.register(AmmoMaterials.IRON,      new MaterialData(0.0F, 0.0F, -0.15F));
        container.register(AmmoMaterials.LAPIS,     new MaterialData(-0.15F, -0.15F, 0.0F));
        container.register(AmmoMaterials.GOLD,      new MaterialData(0.15F, 0.1F, 0.05F));
        container.register(AmmoMaterials.REDSTONE,  new MaterialData(0.0F, 0.25F, 0.05F));
        container.register(AmmoMaterials.EMERALD,   new MaterialData(-0.05F, -0.15F, 0.0F));
        container.register(AmmoMaterials.QUARTZ,    new MaterialData(0.0F, 0.0F, 0.2F));
        container.register(AmmoMaterials.DIAMOND,   new MaterialData(0.0F, -0.1F, -0.05F));
        container.register(AmmoMaterials.AMETHYST,  new MaterialData(0.15F, -0.2F, 0.0F));
        container.register(AmmoMaterials.NETHERITE, new MaterialData(0.05F, -0.25F, 0.0F));
    });

    public static final IMaterialDataContainer CONTAINER_45ACP = ModUtils.init(new MaterialDataContainer(), container -> {
        container.register(AmmoMaterials.WOOD,      new MaterialData(0.0F, 0.15F, -0.05F));
        container.register(AmmoMaterials.STONE,     new MaterialData(0.0F, -0.05F, 0.0F));
        container.register(AmmoMaterials.LAPIS,     new MaterialData(0.0F, 0.20F, 0.15F));
        container.register(AmmoMaterials.GOLD,      new MaterialData(0.15F, 0.05F, 0.0F));
        container.register(AmmoMaterials.REDSTONE,  new MaterialData(-0.2F, -0.05F, 0.0F));
        container.register(AmmoMaterials.EMERALD,   new MaterialData(-0.1F, 0.0F, 0.2F));
        container.register(AmmoMaterials.QUARTZ,    new MaterialData(-0.05F, 0.15F, 0.0F));
        container.register(AmmoMaterials.DIAMOND,   new MaterialData(0.0F, -0.2F, -0.1F));
        container.register(AmmoMaterials.AMETHYST,  new MaterialData(0.25F, -0.15F, -0.05F));
        container.register(AmmoMaterials.NETHERITE, new MaterialData(0.1F, -0.45F, 0.0F));
    });

    public static final IMaterialDataContainer CONTAINER_556MM = ModUtils.init(new MaterialDataContainer(), container -> {
        container.register(AmmoMaterials.WOOD,      new MaterialData(-0.1F, 0.15F, 0.1F));
        container.register(AmmoMaterials.STONE,     new MaterialData(0.0F, 0.0F, 0.1F));
        container.register(AmmoMaterials.IRON,      new MaterialData(0.0F, -0.15F, 0.0F));
        container.register(AmmoMaterials.LAPIS,     new MaterialData(0.1F, 0.2F, -0.05F));
        container.register(AmmoMaterials.GOLD,      new MaterialData(0.0F, 0.0F, -0.15F));
        container.register(AmmoMaterials.REDSTONE,  new MaterialData(0.0F, 0.15F, 0.0F));
        container.register(AmmoMaterials.EMERALD,   new MaterialData(-0.1F, 0.0F, 0.2F));
        container.register(AmmoMaterials.QUARTZ,    new MaterialData(0.1F, 0.3F, 0.0F));
        container.register(AmmoMaterials.DIAMOND,   new MaterialData(0.05F, 0.0F, 0.0F));
        container.register(AmmoMaterials.AMETHYST,  new MaterialData(0.1F, -0.15F, 0.0F));
        container.register(AmmoMaterials.NETHERITE, new MaterialData(0.25F, -0.2F, -0.1F));
    });

    public static final IMaterialDataContainer CONTAINER_762MM = ModUtils.init(new MaterialDataContainer(), container -> {
        container.register(AmmoMaterials.WOOD,      new MaterialData(-0.25F, 0.0F, -0.2F));
        container.register(AmmoMaterials.STONE,     new MaterialData(0.0F, 0.0F, 0.25F));
        container.register(AmmoMaterials.IRON,      new MaterialData(0.3F, 0.0F, -0.25F));
        container.register(AmmoMaterials.LAPIS,     new MaterialData(0.15F, 0.25F, -0.05F));
        container.register(AmmoMaterials.REDSTONE,  new MaterialData(-0.2F, -0.05F, 0.05F));
        container.register(AmmoMaterials.EMERALD,   new MaterialData(-0.05F, 0.05F, 0.0F));
        container.register(AmmoMaterials.QUARTZ,    new MaterialData(0.05F, 0.35F, 0.15F));
        container.register(AmmoMaterials.DIAMOND,   new MaterialData(-0.1F, -0.15F, 0.0F));
        container.register(AmmoMaterials.AMETHYST,  new MaterialData(0.2F, -0.15F, -0.1F));
        container.register(AmmoMaterials.NETHERITE, new MaterialData(0.25F, -0.3F, -0.05F));
    });

    public static final IMaterialDataContainer CONTAINER_12G = ModUtils.init(new MaterialDataContainer(), container -> {
        container.register(AmmoMaterials.WOOD,      new MaterialData(0.0F, 0.5F, 0.0F));
        container.register(AmmoMaterials.STONE,     new MaterialData(0.2F, -0.05F, 0.0F));
        container.register(AmmoMaterials.IRON,      new MaterialData(0.0F, 0.0F, -0.1F));
        container.register(AmmoMaterials.LAPIS,     new MaterialData(-0.05F, 0.1F, -0.05F));
        container.register(AmmoMaterials.GOLD,      new MaterialData(-0.3F, 0.0F, 0.2F));
        container.register(AmmoMaterials.REDSTONE,  new MaterialData(0.0F, 0.35F, 0.0F));
        container.register(AmmoMaterials.QUARTZ,    new MaterialData(-0.5F, 0.0F, 0.0F));
        container.register(AmmoMaterials.DIAMOND,   new MaterialData(-0.2F, -0.25F, 0.0F));
        container.register(AmmoMaterials.AMETHYST,  new MaterialData(0.0F, -0.4F, 0.15F));
        container.register(AmmoMaterials.NETHERITE, new MaterialData(0.3F, -0.35F, 0.1F));
    });

    public static final IMaterialDataContainer CONTAINER_BOLTS = ModUtils.init(new MaterialDataContainer(), container -> {
        container.register(AmmoMaterials.WOOD,      new MaterialData(-0.1F, 0.35F, 0.0F));
        container.register(AmmoMaterials.STONE,     new MaterialData(0.0F, 0.45F, 0.1F));
        container.register(AmmoMaterials.IRON,      new MaterialData(0.2F, 0.0F, -0.25F));
        container.register(AmmoMaterials.GOLD,      new MaterialData(0.25F, 0.0F, 0.15F));
        container.register(AmmoMaterials.REDSTONE,  new MaterialData(-0.3F, 0.1F, 0.05F));
        container.register(AmmoMaterials.EMERALD,   new MaterialData(0.0F, -0.15F, -0.1F));
        container.register(AmmoMaterials.QUARTZ,    new MaterialData(0.0F, 0.25F, 0.35F));
        container.register(AmmoMaterials.DIAMOND,   new MaterialData(0.05F, 0.0F, 0.0F));
        container.register(AmmoMaterials.AMETHYST,  new MaterialData(0.15F, -0.2F, -0.05F));
        container.register(AmmoMaterials.NETHERITE, new MaterialData(0.0F, -0.35F, 0.2F));
    });

    private MaterialDataList() {}
}
