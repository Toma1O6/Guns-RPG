package dev.toma.gunsrpg.common;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.baked.PistolBakedModel;
import dev.toma.gunsrpg.client.render.item.PistolRenderer;
import dev.toma.gunsrpg.common.block.BlockBlastFurnace;
import dev.toma.gunsrpg.common.block.GRPGOre;
import dev.toma.gunsrpg.common.entity.EntityBullet;
import dev.toma.gunsrpg.common.item.GRPGItem;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.builder.GunBuilder;
import dev.toma.gunsrpg.common.item.util.DebuffHeal;
import dev.toma.gunsrpg.config.GRPGConfig;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;
import toma.config.util.RegisterConfigEvent;

import java.util.ArrayList;
import java.util.List;

public class ModRegistry {

    public static void registerItemBlock(Block block) {
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());
        Handler.queue.add(itemBlock);
    }

    @GameRegistry.ObjectHolder(GunsRPG.MODID)
    public static final class GRPGItems {

        public static final GunItem PISTOL = null;
        public static final GRPGItem AMETHYST = null;
        public static final DebuffHeal ANTIDOTUM_PILLS = null;
        public static final DebuffHeal VACCINE = null;
        public static final DebuffHeal PLASTER_CAST = null;
    }

    @GameRegistry.ObjectHolder(GunsRPG.MODID)
    public static final class GRPGBlocks {
        public static final GRPGOre AMETHYST_ORE = null;
        public static final BlockBlastFurnace BLAST_FURNACE = null;
    }

    @Mod.EventBusSubscriber(modid = GunsRPG.MODID)
    public static final class Handler {

        private static List<ItemBlock> queue = new ArrayList<>();
        private static int id = -1;

        @SubscribeEvent
        public static void onBlockRegister(RegistryEvent.Register<Block> event) {
            event.getRegistry().registerAll(
                    new GRPGOre("amethyst_ore", () -> GRPGItems.AMETHYST),
                    new BlockBlastFurnace("blast_furnace")
            );
        }

        @SubscribeEvent
        public static void onItemRegister(RegistryEvent.Register<Item> event) {
            IForgeRegistry<Item> registry = event.getRegistry();
            registry.registerAll(
                    new GRPGItem("amethyst"),
                    new DebuffHeal("antidotum_pills", 32, data -> data.getDebuffs()[0].isActive(), data -> data.getDebuffs()[0].heal(40)),
                    new DebuffHeal("vaccine", 32, data -> data.getDebuffs()[1].isActive(), data -> data.getDebuffs()[1].heal(50)),
                    new DebuffHeal("plaster_cast", 32, data -> data.getDebuffs()[2].isActive(), data -> data.getDebuffs()[2].heal(35)),
                    GunBuilder.create()
                            // TODO replace with shoot action
                            .makeBullet(EntityBullet::new)
                            .statBuilder()
                                .withDamage(3.0F)
                                .withVelocity(14.0F)
                                .withGravity(0.05F, 3)
                            .createStats()
                            .build("pistol")
            );
            queue.forEach(registry::register);
            queue = null;
        }

        @SubscribeEvent
        public static void onEntityRegister(RegistryEvent.Register<EntityEntry> event) {
            event.getRegistry().registerAll(
                    makeBuilder("bullet", EntityBullet.class).tracker(256, 1, true).build()
            );
        }

        @SubscribeEvent
        public static void configRegister(RegisterConfigEvent event) {
            event.register(GunsRPG.class, GRPGConfig::new);
        }

        protected static <T extends Entity> EntityEntryBuilder<T> makeBuilder(String name, Class<T> tClass) {
            EntityEntryBuilder<T> builder = EntityEntryBuilder.create();
            ResourceLocation location = GunsRPG.makeResource(name);
            return builder.id(location, ++id).name(location.toString()).entity(tClass);
        }
    }

    @Mod.EventBusSubscriber(value = Side.CLIENT, modid = GunsRPG.MODID)
    public static final class ClientHandler {

        @SubscribeEvent
        public static void onModelRegistry(ModelRegistryEvent event) {
            ForgeRegistries.BLOCKS.getValuesCollection().stream()
                    .filter(b -> b.getRegistryName().getResourceDomain().equals(GunsRPG.MODID))
                    .forEach(ClientHandler::register);
            ForgeRegistries.ITEMS.getValuesCollection().stream()
                    .filter(i -> i.getRegistryName().getResourceDomain().equals(GunsRPG.MODID))
                    .forEach(ClientHandler::register);

            GRPGItems.PISTOL.setTileEntityItemStackRenderer(new PistolRenderer());
        }

        @SubscribeEvent
        public static void onModelBake(ModelBakeEvent event) {
            IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();
            registry.putObject(getGunModelResourceLocation(GRPGItems.PISTOL), new PistolBakedModel());
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
}
