package dev.toma.gunsrpg.common;

import dev.toma.gunsrpg.GunsRPG;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
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

import java.util.ArrayList;
import java.util.List;

public class ModRegistry {

    @GameRegistry.ObjectHolder(GunsRPG.MODID)
    public static final class GRPGItems {

    }

    @GameRegistry.ObjectHolder(GunsRPG.MODID)
    public static final class GRPGBlocks {

    }

    public static void registerItemBlock(Block block) {
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());
        Handler.queue.add(itemBlock);
    }

    @Mod.EventBusSubscriber
    public static final class Handler {

        private static List<ItemBlock> queue = new ArrayList<>();

        @SubscribeEvent
        public static void onBlockRegister(RegistryEvent.Register<Block> event) {
            event.getRegistry().registerAll(
            );
        }

        @SubscribeEvent
        public static void onItemRegister(RegistryEvent.Register<Item> event) {
            IForgeRegistry<Item> registry = event.getRegistry();
            registry.registerAll(
            );
            queue.forEach(registry::register);
            queue = null;
        }

        @SubscribeEvent
        public static void onEntityRegister(RegistryEvent.Register<EntityEntry> event) {

        }

        private static int id = -1;

        protected static <T extends Entity> EntityEntryBuilder<T> makeBuilder(String name, Class<T> tClass) {
            EntityEntryBuilder<T> builder = EntityEntryBuilder.create();
            ResourceLocation location = GunsRPG.makeResource(name);
            return builder.id(location, ++id).name(location.toString()).entity(tClass);
        }
    }

    @Mod.EventBusSubscriber(Side.CLIENT)
    public static final class ClientHandler {

        @SubscribeEvent
        public static void onModelRegistry(ModelRegistryEvent event) {
            ForgeRegistries.BLOCKS.getValuesCollection().stream()
                    .filter(b -> b.getRegistryName().getResourceDomain().equals(GunsRPG.MODID))
                    .forEach(ClientHandler::register);
            ForgeRegistries.ITEMS.getValuesCollection().stream()
                    .filter(i -> i.getRegistryName().getResourceDomain().equals(GunsRPG.MODID))
                    .forEach(ClientHandler::register);
        }

        protected static void register(Item item) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }

        protected static void register(Block block) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "normal"));
        }
    }
}
