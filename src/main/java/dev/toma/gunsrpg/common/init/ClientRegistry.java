package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.baked.GunBakedModel;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = GunsRPG.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistry {

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        Map<ResourceLocation, IBakedModel> registry = event.getModelRegistry();
        IBakedModel model = new GunBakedModel();
        registry.put(getGunModelResourceLocation(ModItems.M1911), model);
        registry.put(getGunModelResourceLocation(ModItems.UMP45), model);
        registry.put(getGunModelResourceLocation(ModItems.SKS), model);
        registry.put(getGunModelResourceLocation(ModItems.KAR98K), model);
        registry.put(getGunModelResourceLocation(ModItems.S1897), model);
        registry.put(getGunModelResourceLocation(ModItems.WOODEN_CROSSBOW), model);
    }

    protected static ModelResourceLocation getGunModelResourceLocation(GunItem gunItem) {
        return new ModelResourceLocation(gunItem.getRegistryName(), "inventory");
    }
}
