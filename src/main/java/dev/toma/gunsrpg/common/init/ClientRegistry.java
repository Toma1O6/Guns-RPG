package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.baked.*;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = GunsRPG.MODID, value = Dist.CLIENT)
public class ClientRegistry {

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        Map<ResourceLocation, IBakedModel> registry = event.getModelRegistry();
        registry.put(getGunModelResourceLocation(GRPGItems.PISTOL), new PistolBakedModel());
        registry.put(getGunModelResourceLocation(GRPGItems.SMG), new SMGBakedModel());
        registry.put(getGunModelResourceLocation(GRPGItems.ASSAULT_RIFLE), new ARBakedModel());
        registry.put(getGunModelResourceLocation(GRPGItems.SNIPER_RIFLE), new SRBakedModel());
        registry.put(getGunModelResourceLocation(GRPGItems.SHOTGUN), new SGBakedModel());
        registry.put(getGunModelResourceLocation(GRPGItems.CROSSBOW), new CrossbowBakedModel());
    }

    protected static ModelResourceLocation getGunModelResourceLocation(GunItem gunItem) {
        return new ModelResourceLocation(gunItem.getRegistryName(), "inventory");
    }
}
