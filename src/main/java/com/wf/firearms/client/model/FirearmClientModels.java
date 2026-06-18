package com.wf.firearms.client.model;

import com.wf.firearms.GunsRpg;
import com.wf.firearms.item.FirearmItem;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

/**
 * 枪械物品模型烘焙：创造栏 / EMI 仍用平面 {@code item/generated} 图标；
 * 手持经 {@link FirearmDisplayBakedModel} 走空四边形 + BEWLR 3D，避免 2D 与 3D 叠影。
 */
@Mod.EventBusSubscriber(modid = GunsRpg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class FirearmClientModels {
    private static final Set<String> GUN_IDS =
            Set.of(
                    "m1911", "r45", "desert_eagle", "ump45", "thompson", "vector", "akm", "hk416", "aug",
                    "vss", "sks", "mk14ebr", "kar98k", "winchester", "awm", "s686", "s1897", "s12k", "minigun");

    private FirearmClientModels() {}

    @SubscribeEvent
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        FirearmGuiIconModels.clear();
        var registry = event.getModels();
        int ok = 0;
        for (Item item : ForgeRegistries.ITEMS) {
            if (!(item instanceof FirearmItem)) {
                continue;
            }
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
            if (id == null || !GunsRpg.MOD_ID.equals(id.getNamespace())) {
                continue;
            }
            ModelResourceLocation inv = new ModelResourceLocation(id, "inventory");
            BakedModel flat = registry.get(inv);
            if (flat != null && !flat.isCustomRenderer()) {
                FirearmGuiIconModels.put(id.getPath(), flat);
                registry.put(
                        inv,
                        FirearmDisplayBakedModel.create(flat, flat.getParticleIcon()));
                ok++;
            } else {
                GunsRpg.LOGGER.error(
                        "Gun {} missing flat item model — need models/item/{}.json and textures/item/{}_gun_icon.png",
                        id.getPath(),
                        id.getPath(),
                        id.getPath());
            }
        }
        GunsRpg.LOGGER.info("Firearm display models (flat GUI + BEWLR hand): {}/{}", ok, GUN_IDS.size());
    }

    public static void verifyGunCount() {
        long guns =
                ForgeRegistries.ITEMS.getValues().stream().filter(FirearmItem.class::isInstance).count();
        if (guns < 13) {
            GunsRpg.LOGGER.warn(
                    "Expected >=13 FirearmItem, found {} — check ModItems registration",
                    guns);
        }
    }
}
