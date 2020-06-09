package dev.toma.gunsrpg.common;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.animation.Animation;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.baked.*;
import dev.toma.gunsrpg.client.render.item.*;
import dev.toma.gunsrpg.common.block.BlockAirdrop;
import dev.toma.gunsrpg.common.block.BlockBlastFurnace;
import dev.toma.gunsrpg.common.block.GRPGOre;
import dev.toma.gunsrpg.common.entity.*;
import dev.toma.gunsrpg.common.item.GRPGItem;
import dev.toma.gunsrpg.common.item.guns.*;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.ammo.ItemAmmo;
import dev.toma.gunsrpg.common.item.util.DebuffHeal;
import dev.toma.gunsrpg.common.skilltree.Ability;
import dev.toma.gunsrpg.config.GRPGConfig;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.fml.relauncher.SideOnly;
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
        public static final GRPGItem SMALL_BULLET_CASING = null;
        public static final GRPGItem LARGE_BULLET_CASING = null;
        public static final GRPGItem SHOTGUN_SHELL = null;
        public static final GRPGItem BARREL = null;
        public static final GRPGItem LONG_BARREL = null;
        public static final GRPGItem IRON_STOCK = null;
        public static final GRPGItem SMALL_IRON_STOCK = null;
        public static final GRPGItem WOODEN_STOCK = null;
        public static final GRPGItem MAGAZINE = null;
        public static final GRPGItem GUN_PARTS = null;
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
        public static final SoundEvent P1911 = null;
        public static final SoundEvent P1911_SILENT = null;
        public static final SoundEvent UMP45 = null;
        public static final SoundEvent UMP45_SILENT = null;
        public static final SoundEvent SKS = null;
        public static final SoundEvent SKS_SILENT = null;
        public static final SoundEvent KAR98K = null;
        public static final SoundEvent KAR98K_SILENT = null;
        public static final SoundEvent S1897 = null;
        public static final SoundEvent P1911_RELOAD = null;
        public static final SoundEvent P1911_RELOAD_SHORT = null;
        public static final SoundEvent SMG_RELOAD = null;
        public static final SoundEvent SMG_RELOAD_SHORT = null;
        public static final SoundEvent AR_RELOAD = null;
        public static final SoundEvent SR_RELOAD = null;
        public static final SoundEvent SR_RELOAD_SHORT = null;
        public static final SoundEvent SR_BOLT = null;
        public static final SoundEvent SG_RELOAD = null;
        public static final SoundEvent SG_RELOAD_SHORT = null;
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
                    new DebuffHeal("antidotum_pills", 32, () -> GRPGSounds.USE_ANTIDOTUM_PILLS, "These pills heal 40% of poison", data -> data.getDebuffs()[0].isActive(), data -> data.getDebuffs()[0].heal(40)),
                    new DebuffHeal("vaccine", 32, () -> GRPGSounds.USE_VACCINE, "This vaccine heals 50% of infection", data -> data.getDebuffs()[1].isActive(), data -> data.getDebuffs()[1].heal(50)) {
                        @SideOnly(Side.CLIENT)
                        @Override
                        public Animation getUseAnimation(ItemStack stack) {
                            return new Animations.Vaccine(this.getMaxItemUseDuration(stack));
                        }
                    },
                    new DebuffHeal("plaster_cast", 32, () -> GRPGSounds.USE_PLASTER_CAST, "Plaster cast heals 35% of broken bones", data -> data.getDebuffs()[2].isActive(), data -> data.getDebuffs()[2].heal(35)) {
                        @SideOnly(Side.CLIENT)
                        @Override
                        public Animation getUseAnimation(ItemStack stack) {
                            return new Animations.Splint(this.getMaxItemUseDuration(stack));
                        }
                    },
                    new DebuffHeal("bandage", 50, () -> GRPGSounds.USE_BANDAGE, "Bandages can stop 25% of bleeding", data -> data.getDebuffs()[3].isActive(), data -> data.getDebuffs()[3].heal(25)) {
                        @SideOnly(Side.CLIENT)
                        @Override
                        public Animation getUseAnimation(ItemStack stack) {
                            return new Animations.Bandage(this.getMaxItemUseDuration(stack));
                        }
                    },
                    new ItemAmmo("wooden_ammo_9mm", AmmoType._9MM, AmmoMaterial.WOOD, Ability.PISTOL_WOOD_AMMO, () -> GRPGItems.PISTOL),
                    new ItemAmmo("wooden_ammo_45acp", AmmoType._45ACP, AmmoMaterial.WOOD, Ability.SMG_WOOD_AMMO, () -> GRPGItems.SMG),
                    new ItemAmmo("wooden_ammo_556mm", AmmoType._556MM, AmmoMaterial.WOOD, Ability.AR_WOOD_AMMO, () -> GRPGItems.ASSAULT_RIFLE),
                    new ItemAmmo("wooden_ammo_762mm", AmmoType._762MM, AmmoMaterial.WOOD, Ability.SR_WOOD_AMMO, () -> GRPGItems.SNIPER_RIFLE),
                    new ItemAmmo("wooden_ammo_12g", AmmoType._12G, AmmoMaterial.WOOD, Ability.SG_WOOD_AMMO, () -> GRPGItems.SHOTGUN),
                    new ItemAmmo("stone_ammo_9mm", AmmoType._9MM, AmmoMaterial.STONE, Ability.PISTOL_STONE_AMMO, () -> GRPGItems.PISTOL),
                    new ItemAmmo("stone_ammo_45acp", AmmoType._45ACP, AmmoMaterial.STONE, Ability.SMG_STONE_AMMO, () -> GRPGItems.SMG),
                    new ItemAmmo("stone_ammo_556mm", AmmoType._556MM, AmmoMaterial.STONE, Ability.AR_STONE_AMMO, () -> GRPGItems.ASSAULT_RIFLE),
                    new ItemAmmo("stone_ammo_762mm", AmmoType._762MM, AmmoMaterial.STONE, Ability.SR_STONE_AMMO, () -> GRPGItems.SNIPER_RIFLE),
                    new ItemAmmo("stone_ammo_12g", AmmoType._12G, AmmoMaterial.STONE, Ability.SG_STONE_AMMO, () -> GRPGItems.SHOTGUN),
                    new ItemAmmo("iron_ammo_9mm", AmmoType._9MM, AmmoMaterial.IRON, Ability.PISTOL_IRON_AMMO, () -> GRPGItems.PISTOL),
                    new ItemAmmo("iron_ammo_45acp", AmmoType._45ACP, AmmoMaterial.IRON, Ability.SMG_IRON_AMMO, () -> GRPGItems.SMG),
                    new ItemAmmo("iron_ammo_556mm", AmmoType._556MM, AmmoMaterial.IRON, Ability.AR_IRON_AMMO, () -> GRPGItems.ASSAULT_RIFLE),
                    new ItemAmmo("iron_ammo_762mm", AmmoType._762MM, AmmoMaterial.IRON, Ability.SR_IRON_AMMO, () -> GRPGItems.SNIPER_RIFLE),
                    new ItemAmmo("iron_ammo_12g", AmmoType._12G, AmmoMaterial.IRON, Ability.SG_IRON_AMMO, () -> GRPGItems.SHOTGUN),
                    new ItemAmmo("gold_ammo_9mm", AmmoType._9MM, AmmoMaterial.GOLD, Ability.PISTOL_GOLD_AMMO, () -> GRPGItems.PISTOL),
                    new ItemAmmo("gold_ammo_45acp", AmmoType._45ACP, AmmoMaterial.GOLD, Ability.SMG_GOLD_AMMO, () -> GRPGItems.SMG),
                    new ItemAmmo("gold_ammo_556mm", AmmoType._556MM, AmmoMaterial.GOLD, Ability.AR_GOLD_AMMO, () -> GRPGItems.ASSAULT_RIFLE),
                    new ItemAmmo("gold_ammo_762mm", AmmoType._762MM, AmmoMaterial.GOLD, Ability.SR_GOLD_AMMO, () -> GRPGItems.SNIPER_RIFLE),
                    new ItemAmmo("gold_ammo_12g", AmmoType._12G, AmmoMaterial.GOLD, Ability.SG_GOLD_AMMO, () -> GRPGItems.SHOTGUN),
                    new ItemAmmo("diamond_ammo_9mm", AmmoType._9MM, AmmoMaterial.DIAMOND, Ability.PISTOL_DIAMOND_AMMO, () -> GRPGItems.PISTOL),
                    new ItemAmmo("diamond_ammo_45acp", AmmoType._45ACP, AmmoMaterial.DIAMOND, Ability.SMG_DIAMOND_AMMO, () -> GRPGItems.SMG),
                    new ItemAmmo("diamond_ammo_556mm", AmmoType._556MM, AmmoMaterial.DIAMOND, Ability.AR_DIAMOND_AMMO, () -> GRPGItems.ASSAULT_RIFLE),
                    new ItemAmmo("diamond_ammo_762mm", AmmoType._762MM, AmmoMaterial.DIAMOND, Ability.SR_DIAMOND_AMMO, () -> GRPGItems.SNIPER_RIFLE),
                    new ItemAmmo("diamond_ammo_12g", AmmoType._12G, AmmoMaterial.DIAMOND, Ability.SG_DIAMOND_AMMO, () -> GRPGItems.SHOTGUN),
                    new ItemAmmo("emerald_ammo_9mm", AmmoType._9MM, AmmoMaterial.EMERALD, Ability.PISTOL_EMERALD_AMMO, () -> GRPGItems.PISTOL),
                    new ItemAmmo("emerald_ammo_45acp", AmmoType._45ACP, AmmoMaterial.EMERALD, Ability.SMG_EMERALD_AMMO, () -> GRPGItems.SMG),
                    new ItemAmmo("emerald_ammo_556mm", AmmoType._556MM, AmmoMaterial.EMERALD, Ability.AR_EMERALD_AMMO, () -> GRPGItems.ASSAULT_RIFLE),
                    new ItemAmmo("emerald_ammo_762mm", AmmoType._762MM, AmmoMaterial.EMERALD, Ability.SR_EMERALD_AMMO, () -> GRPGItems.SNIPER_RIFLE),
                    new ItemAmmo("emerald_ammo_12g", AmmoType._12G, AmmoMaterial.EMERALD, Ability.SG_EMERALD_AMMO, () -> GRPGItems.SHOTGUN),
                    new ItemAmmo("amethyst_ammo_9mm", AmmoType._9MM, AmmoMaterial.AMETHYST, Ability.PISTOL_AMETHYST_AMMO, () -> GRPGItems.PISTOL),
                    new ItemAmmo("amethyst_ammo_45acp", AmmoType._45ACP, AmmoMaterial.AMETHYST, Ability.SMG_AMETHYST_AMMO, () -> GRPGItems.SMG),
                    new ItemAmmo("amethyst_ammo_556mm", AmmoType._556MM, AmmoMaterial.AMETHYST, Ability.AR_AMETHYST_AMMO, () -> GRPGItems.ASSAULT_RIFLE),
                    new ItemAmmo("amethyst_ammo_762mm", AmmoType._762MM, AmmoMaterial.AMETHYST, Ability.SR_AMETHYST_AMMO, () -> GRPGItems.SNIPER_RIFLE),
                    new ItemAmmo("amethyst_ammo_12g", AmmoType._12G, AmmoMaterial.AMETHYST, Ability.SG_AMETHYST_AMMO, () -> GRPGItems.SHOTGUN),
                    new PistolItem("pistol"),
                    new SMGItem("smg"),
                    new ARItem("assault_rifle"),
                    new SRItem("sniper_rifle"),
                    new SGItem("shotgun"),
                    new GRPGItem("small_bullet_casing"),
                    new GRPGItem("large_bullet_casing"),
                    new GRPGItem("shotgun_shell"),
                    new GRPGItem("barrel"),
                    new GRPGItem("long_barrel"),
                    new GRPGItem("iron_stock"),
                    new GRPGItem("small_iron_stock"),
                    new GRPGItem("wooden_stock"),
                    new GRPGItem("magazine"),
                    new GRPGItem("gun_parts")
            );
            queue.forEach(registry::register);
            queue = null;
        }

        @SubscribeEvent
        public static void onEntityRegister(RegistryEvent.Register<EntityEntry> event) {
            event.getRegistry().registerAll(
                    makeBuilder("bullet", EntityBullet.class).tracker(256, 1, true).build(),
                    makeBuilder("airdrop", EntityAirdrop.class).tracker(256, 1, true).build(),
                    makeBuilder("explosive_skeleton", EntityExplosiveSkeleton.class).tracker(80, 3, true).egg(0xB46F67, 0x494949).spawn(EnumCreatureType.MONSTER, 15, 1, 3, ForgeRegistries.BIOMES).build(),
                    makeBuilder("explosive_arrow", EntityExplosiveArrow.class).tracker(64, 20, true).build(),
                    makeBuilder("zombie_gunner", EntityZombieGunner.class).tracker(80, 3, true).egg(0x00aa00, 0xdbdb00).spawn(EnumCreatureType.MONSTER, 35, 2, 5, ForgeRegistries.BIOMES).build(),
                    makeBuilder("bloodmoon_golem", EntityBloodmoonGolem.class).tracker(80, 3, true).egg(0x444444, 0x990000).build()
            );
        }

        @SubscribeEvent
        public static void onSoundRegister(RegistryEvent.Register<SoundEvent> event) {
            event.getRegistry().registerAll(
                    sound("use_antidotum_pills"),
                    sound("use_vaccine"),
                    sound("use_plaster_cast"),
                    sound("use_bandage"),
                    sound("plane_fly_by"),
                    sound("p1911"),
                    sound("p1911_silent"),
                    sound("ump45"),
                    sound("ump45_silent"),
                    sound("sks"),
                    sound("sks_silent"),
                    sound("kar98k"),
                    sound("kar98k_silent"),
                    sound("s1897"),
                    sound("p1911_reload"),
                    sound("p1911_reload_short"),
                    sound("smg_reload"),
                    sound("smg_reload_short"),
                    sound("ar_reload"),
                    sound("sr_reload"),
                    sound("sr_reload_short"),
                    sound("sr_bolt"),
                    sound("sg_reload"),
                    sound("sg_reload_short")
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
            GRPGItems.SHOTGUN.setTileEntityItemStackRenderer(new SGRenderer());
        }

        @SubscribeEvent
        public static void onModelBake(ModelBakeEvent event) {
            IRegistry<ModelResourceLocation, IBakedModel> registry = event.getModelRegistry();
            registry.putObject(getGunModelResourceLocation(GRPGItems.PISTOL), new PistolBakedModel());
            registry.putObject(getGunModelResourceLocation(GRPGItems.SMG), new SMGBakedModel());
            registry.putObject(getGunModelResourceLocation(GRPGItems.ASSAULT_RIFLE), new ARBakedModel());
            registry.putObject(getGunModelResourceLocation(GRPGItems.SNIPER_RIFLE), new SRBakedModel());
            registry.putObject(getGunModelResourceLocation(GRPGItems.SHOTGUN), new SGBakedModel());
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
