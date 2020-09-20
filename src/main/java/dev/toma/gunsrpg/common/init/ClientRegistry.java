package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.baked.*;
import dev.toma.gunsrpg.client.render.item.*;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = GunsRPG.MODID, value = Side.CLIENT)
public class ClientRegistry {

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        ForgeRegistries.BLOCKS.getValuesCollection().stream()
                .filter(b -> b.getRegistryName().getResourceDomain().equals(GunsRPG.MODID))
                .forEach(ClientRegistry::register);
        ForgeRegistries.ITEMS.getValuesCollection().stream()
                .filter(i -> i.getRegistryName().getResourceDomain().equals(GunsRPG.MODID))
                .forEach(ClientRegistry::register);

        GRPGItems.PISTOL.setTileEntityItemStackRenderer(new PistolRenderer());
        GRPGItems.SMG.setTileEntityItemStackRenderer(new SMGRenderer());
        GRPGItems.ASSAULT_RIFLE.setTileEntityItemStackRenderer(new ARRenderer());
        GRPGItems.SNIPER_RIFLE.setTileEntityItemStackRenderer(new SRRenderer());
        GRPGItems.SHOTGUN.setTileEntityItemStackRenderer(new SGRenderer());
        GRPGItems.CROSSBOW.setTileEntityItemStackRenderer(new CrossbowRenderer());
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();
        registry.putObject(getGunModelResourceLocation(GRPGItems.PISTOL), new PistolBakedModel());
        registry.putObject(getGunModelResourceLocation(GRPGItems.SMG), new SMGBakedModel());
        registry.putObject(getGunModelResourceLocation(GRPGItems.ASSAULT_RIFLE), new ARBakedModel());
        registry.putObject(getGunModelResourceLocation(GRPGItems.SNIPER_RIFLE), new SRBakedModel());
        registry.putObject(getGunModelResourceLocation(GRPGItems.SHOTGUN), new SGBakedModel());
        registry.putObject(getGunModelResourceLocation(GRPGItems.CROSSBOW), new CrossbowBakedModel());
    }

    protected static ModelResourceLocation getGunModelResourceLocation(GunItem gunItem) {
        return new ModelResourceLocation(gunItem.getRegistryName(), "inventory");
    }

    protected static void register(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    protected static void register(Block block) {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "normal"));
    }
}
