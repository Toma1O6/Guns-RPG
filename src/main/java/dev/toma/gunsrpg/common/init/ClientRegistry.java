package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.baked.SimpleBakedModel;
import dev.toma.gunsrpg.common.container.CrystalStationContainer;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

@Mod.EventBusSubscriber(modid = GunsRPG.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistry {

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        Map<ResourceLocation, IBakedModel> registry = event.getModelRegistry();
        IBakedModel model = new SimpleBakedModel();
        ForgeRegistries.ITEMS.getValues().stream()
                .filter(item -> item instanceof GunItem)
                .map(item -> (GunItem) item)
                .forEach(gun -> registry.put(getModelResourceLocation(gun), model));
        registry.put(getModelResourceLocation(ModItems.STASH_DETECTOR), model);
        registry.put(getModelResourceLocation(ModItems.SMG_TURRET), model);
        registry.put(getModelResourceLocation(ModItems.AR_TURRET), model);
        registry.put(getModelResourceLocation(ModItems.ROCKET_TURRET), model);
    }

    @SubscribeEvent
    public static void stitchTextures(TextureStitchEvent.Pre event) {
        AtlasTexture map = event.getMap();
        ResourceLocation mapPath = map.location();
        if (!mapPath.equals(PlayerContainer.BLOCK_ATLAS)) return;
        event.addSprite(CrystalStationContainer.EMPTY_CRYSTAL_SLOT);
        event.addSprite(CrystalStationContainer.EMPTY_TRANSMUTATION_SLOT);
        event.addSprite(CrystalStationContainer.EMPTY_PURITY_SLOT);
    }

    protected static ModelResourceLocation getModelResourceLocation(Item item) {
        return new ModelResourceLocation(item.getRegistryName(), "inventory");
    }
}
