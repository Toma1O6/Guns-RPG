package dev.toma.gunsrpg.common;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.baked.ARBakedModel;
import dev.toma.gunsrpg.client.baked.PistolBakedModel;
import dev.toma.gunsrpg.client.baked.SMGBakedModel;
import dev.toma.gunsrpg.client.baked.SRBakedModel;
import dev.toma.gunsrpg.client.render.item.ARRenderer;
import dev.toma.gunsrpg.client.render.item.PistolRenderer;
import dev.toma.gunsrpg.client.render.item.SMGRenderer;
import dev.toma.gunsrpg.client.render.item.SRRenderer;
import dev.toma.gunsrpg.common.block.BlockAirdrop;
import dev.toma.gunsrpg.common.block.BlockBlastFurnace;
import dev.toma.gunsrpg.common.block.GRPGOre;
import dev.toma.gunsrpg.common.entity.EntityAirdrop;
import dev.toma.gunsrpg.common.entity.EntityBullet;
import dev.toma.gunsrpg.common.entity.EntityExplosiveArrow;
import dev.toma.gunsrpg.common.entity.EntityExplosiveSkeleton;
import dev.toma.gunsrpg.common.item.GRPGItem;
import dev.toma.gunsrpg.common.item.guns.AmmoType;
import dev.toma.gunsrpg.common.item.guns.GunItem;
import dev.toma.gunsrpg.common.item.guns.ItemAmmo;
import dev.toma.gunsrpg.common.item.util.DebuffHeal;
import dev.toma.gunsrpg.config.GRPGConfig;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
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
        public static final GunItem SMG = null;
        public static final GunItem ASSAULT_RIFLE = null;
        public static final GunItem SNIPER_RIFLE = null;
        public static final GunItem SHOTGUN = null;
        public static final GRPGItem AMETHYST = null;
        public static final DebuffHeal ANTIDOTUM_PILLS = null;
        public static final DebuffHeal VACCINE = null;
        public static final DebuffHeal PLASTER_CAST = null;
        public static final DebuffHeal BANDAGE = null;
        public static final ItemAmmo WOODEN_AMMO_9MM = null;
        public static final ItemAmmo WOODEN_AMMO_45ACP = null;
        public static final ItemAmmo WOODEN_AMMO_556MM = null;
        public static final ItemAmmo WOODEN_AMMO_762MM = null;
        public static final ItemAmmo WOODEN_AMMO_12G = null;
        public static final ItemAmmo STONE_AMMO_9MM = null;
        public static final ItemAmmo STONE_AMMO_45ACP = null;
        public static final ItemAmmo STONE_AMMO_556MM = null;
        public static final ItemAmmo STONE_AMMO_762MM = null;
        public static final ItemAmmo STONE_AMMO_12G = null;
        public static final ItemAmmo IRON_AMMO_9MM = null;
        public static final ItemAmmo IRON_AMMO_45ACP = null;
        public static final ItemAmmo IRON_AMMO_556MM = null;
        public static final ItemAmmo IRON_AMMO_762MM = null;
        public static final ItemAmmo IRON_AMMO_12G = null;
        public static final ItemAmmo GOLD_AMMO_9MM = null;
        public static final ItemAmmo GOLD_AMMO_45ACP = null;
        public static final ItemAmmo GOLD_AMMO_556MM = null;
        public static final ItemAmmo GOLD_AMMO_762MM = null;
        public static final ItemAmmo GOLD_AMMO_12G = null;
        public static final ItemAmmo DIAMOND_AMMO_9MM = null;
        public static final ItemAmmo DIAMOND_AMMO_45ACP = null;
        public static final ItemAmmo DIAMOND_AMMO_556MM = null;
        public static final ItemAmmo DIAMOND_AMMO_762MM = null;
        public static final ItemAmmo DIAMOND_AMMO_12G = null;
        public static final ItemAmmo EMERALD_AMMO_9MM = null;
        public static final ItemAmmo EMERALD_AMMO_45ACP = null;
        public static final ItemAmmo EMERALD_AMMO_556MM = null;
        public static final ItemAmmo EMERALD_AMMO_762MM = null;
        public static final ItemAmmo EMERALD_AMMO_12G = null;
        public static final ItemAmmo AMETHYST_AMMO_9MM = null;
        public static final ItemAmmo AMETHYST_AMMO_45ACP = null;
        public static final ItemAmmo AMETHYST_AMMO_556MM = null;
        public static final ItemAmmo AMETHYST_AMMO_762MM = null;
        public static final ItemAmmo AMETHYST_AMMO_12G = null;
    }

    @GameRegistry.ObjectHolder(GunsRPG.MODID)
    public static final class GRPGBlocks {
        public static final GRPGOre AMETHYST_ORE = null;
        public static final BlockBlastFurnace BLAST_FURNACE = null;
        public static final BlockAirdrop AIRDROP = null;
    }

    @GameRegistry.ObjectHolder(GunsRPG.MODID)
    public static final class GRPGSounds {
        public static final SoundEvent USE_ANTIDOTUM_PILLS = null;
        public static final SoundEvent USE_VACCINE = null;
        public static final SoundEvent USE_PLASTER_CAST = null;
        public static final SoundEvent USE_BANDAGE = null;
        public static final SoundEvent PLANE_FLY_BY = null;
    }

    @Mod.EventBusSubscriber(modid = GunsRPG.MODID)
    public static final class Handler {

        private static List<ItemBlock> queue = new ArrayList<>();
        private static int id = -1;

        @SubscribeEvent
        public static void onBlockRegister(RegistryEvent.Register<Block> event) {
            event.getRegistry().registerAll(
                    new GRPGOre("amethyst_ore", () -> GRPGItems.AMETHYST),
                    new BlockBlastFurnace("blast_furnace"),
                    new BlockAirdrop("airdrop")
            );
        }

        @SubscribeEvent
        public static void onItemRegister(RegistryEvent.Register<Item> event) {
            IForgeRegistry<Item> registry = event.getRegistry();
            registry.registerAll(
                    new GRPGItem("amethyst"),
                    new DebuffHeal("antidotum_pills", 32, () -> GRPGSounds.USE_ANTIDOTUM_PILLS,"These pills heal 40% of poison", data -> data.getDebuffs()[0].isActive(), data -> data.getDebuffs()[0].heal(40)),
                    new DebuffHeal("vaccine", 32, () -> GRPGSounds.USE_VACCINE, "This vaccine heals 50% of infection", data -> data.getDebuffs()[1].isActive(), data -> data.getDebuffs()[1].heal(50)),
                    new DebuffHeal("plaster_cast", 32, () -> GRPGSounds.USE_PLASTER_CAST, "Plaster cast heals 35% of broken bones", data -> data.getDebuffs()[2].isActive(), data -> data.getDebuffs()[2].heal(35)),
                    new DebuffHeal("bandage", 50, () -> GRPGSounds.USE_BANDAGE, "Bandages can stop 25% of bleeding", data -> data.getDebuffs()[3].isActive(), data -> data.getDebuffs()[3].heal(25)),
                    new ItemAmmo("wooden_ammo_9mm", AmmoType._9MM),
                    new ItemAmmo("wooden_ammo_45acp", AmmoType._45ACP),
                    new ItemAmmo("wooden_ammo_556mm", AmmoType._556MM),
                    new ItemAmmo("wooden_ammo_762mm", AmmoType._762MM),
                    new ItemAmmo("wooden_ammo_12g", AmmoType._12G),
                    new ItemAmmo("stone_ammo_9mm", AmmoType._9MM),
                    new ItemAmmo("stone_ammo_45acp", AmmoType._45ACP),
                    new ItemAmmo("stone_ammo_556mm", AmmoType._556MM),
                    new ItemAmmo("stone_ammo_762mm", AmmoType._762MM),
                    new ItemAmmo("stone_ammo_12g", AmmoType._12G),
                    new ItemAmmo("iron_ammo_9mm", AmmoType._9MM),
                    new ItemAmmo("iron_ammo_45acp", AmmoType._45ACP),
                    new ItemAmmo("iron_ammo_556mm", AmmoType._556MM),
                    new ItemAmmo("iron_ammo_762mm", AmmoType._762MM),
                    new ItemAmmo("iron_ammo_12g", AmmoType._12G),
                    new ItemAmmo("gold_ammo_9mm", AmmoType._9MM),
                    new ItemAmmo("gold_ammo_45acp", AmmoType._45ACP),
                    new ItemAmmo("gold_ammo_556mm", AmmoType._556MM),
                    new ItemAmmo("gold_ammo_762mm", AmmoType._762MM),
                    new ItemAmmo("gold_ammo_12g", AmmoType._12G),
                    new ItemAmmo("diamond_ammo_9mm", AmmoType._9MM),
                    new ItemAmmo("diamond_ammo_45acp", AmmoType._45ACP),
                    new ItemAmmo("diamond_ammo_556mm", AmmoType._556MM),
                    new ItemAmmo("diamond_ammo_762mm", AmmoType._762MM),
                    new ItemAmmo("diamond_ammo_12g", AmmoType._12G),
                    new ItemAmmo("emerald_ammo_9mm", AmmoType._9MM),
                    new ItemAmmo("emerald_ammo_45acp", AmmoType._45ACP),
                    new ItemAmmo("emerald_ammo_556mm", AmmoType._556MM),
                    new ItemAmmo("emerald_ammo_762mm", AmmoType._762MM),
                    new ItemAmmo("emerald_ammo_12g", AmmoType._12G),
                    new ItemAmmo("amethyst_ammo_9mm", AmmoType._9MM),
                    new ItemAmmo("amethyst_ammo_45acp", AmmoType._45ACP),
                    new ItemAmmo("amethyst_ammo_556mm", AmmoType._556MM),
                    new ItemAmmo("amethyst_ammo_762mm", AmmoType._762MM),
                    new ItemAmmo("amethyst_ammo_12g", AmmoType._12G),
                    GunItem.GunBuilder.create()
                            .stats(GRPGConfig.weapon.pistol)
                            .build("pistol"),
                    GunItem.GunBuilder.create()
                            .stats(GRPGConfig.weapon.smg)
                            .build("smg"),
                    GunItem.GunBuilder.create()
                            .stats(GRPGConfig.weapon.ar)
                            .build("assault_rifle"),
                    GunItem.GunBuilder.create()
                            .stats(GRPGConfig.weapon.sr)
                            .build("sniper_rifle"),
                    GunItem.GunBuilder.create()
                            .stats(GRPGConfig.weapon.shotgun)
                            .build("shotgun")
            );
            queue.forEach(registry::register);
            queue = null;
        }

        @SubscribeEvent
        public static void onEntityRegister(RegistryEvent.Register<EntityEntry> event) {
            event.getRegistry().registerAll(
                    makeBuilder("bullet", EntityBullet.class).tracker(256, 1, true).build(),
                    makeBuilder("airdrop", EntityAirdrop.class).tracker(256, 1, true).build(),
                    makeBuilder("explosive_skeleton", EntityExplosiveSkeleton.class).tracker(80, 3, true).egg(0xB46F67, 0x494949).spawn(GRPGConfig.world.explosiveSkeleton.type, GRPGConfig.world.explosiveSkeleton.weight, GRPGConfig.world.explosiveSkeleton.min, GRPGConfig.world.explosiveSkeleton.max, ForgeRegistries.BIOMES).build(),
                    makeBuilder("explosive_arrow", EntityExplosiveArrow.class).tracker(64, 20, true).build()
            );
        }

        @SubscribeEvent
        public static void onSoundRegister(RegistryEvent.Register<SoundEvent> event) {
            event.getRegistry().registerAll(
                    sound("use_antidotum_pills"),
                    sound("use_vaccine"),
                    sound("use_plaster_cast"),
                    sound("use_bandage"),
                    sound("plane_fly_by")
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

        protected static SoundEvent sound(String key) {
            ResourceLocation name = GunsRPG.makeResource(key);
            return new SoundEvent(name).setRegistryName(name);
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
            GRPGItems.SMG.setTileEntityItemStackRenderer(new SMGRenderer());
            GRPGItems.ASSAULT_RIFLE.setTileEntityItemStackRenderer(new ARRenderer());
            GRPGItems.SNIPER_RIFLE.setTileEntityItemStackRenderer(new SRRenderer());
        }

        @SubscribeEvent
        public static void onModelBake(ModelBakeEvent event) {
            IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();
            registry.putObject(getGunModelResourceLocation(GRPGItems.PISTOL), new PistolBakedModel());
            registry.putObject(getGunModelResourceLocation(GRPGItems.SMG), new SMGBakedModel());
            registry.putObject(getGunModelResourceLocation(GRPGItems.ASSAULT_RIFLE), new ARBakedModel());
            registry.putObject(getGunModelResourceLocation(GRPGItems.SNIPER_RIFLE), new SRBakedModel());
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
