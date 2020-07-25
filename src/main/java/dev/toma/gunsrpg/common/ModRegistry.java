package dev.toma.gunsrpg.common;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.client.animation.Animation;
import dev.toma.gunsrpg.client.animation.Animations;
import dev.toma.gunsrpg.client.baked.*;
import dev.toma.gunsrpg.client.render.item.*;
import dev.toma.gunsrpg.common.block.BlockAirdrop;
import dev.toma.gunsrpg.common.block.BlockBlastFurnace;
import dev.toma.gunsrpg.common.block.BlockSmithingTable;
import dev.toma.gunsrpg.common.block.GRPGOre;
import dev.toma.gunsrpg.common.entity.*;
import dev.toma.gunsrpg.common.item.DebuffHeal;
import dev.toma.gunsrpg.common.item.GRPGItem;
import dev.toma.gunsrpg.common.item.ItemGrenade;
import dev.toma.gunsrpg.common.item.ItemSkillBook;
import dev.toma.gunsrpg.common.item.guns.*;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterial;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.guns.ammo.ItemAmmo;
import dev.toma.gunsrpg.common.skills.*;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.criteria.CriteriaTypes;
import dev.toma.gunsrpg.config.GRPGConfig;
import dev.toma.gunsrpg.util.ModUtils;
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
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import toma.config.util.RegisterConfigEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModRegistry {

    public static IForgeRegistry<SkillType<?>> SKILLS;

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
        public static final GunItem CROSSBOW = null;
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
        public static final ItemAmmo WOODEN_AMMO_CROSSBOW_BOLT = null;
        public static final ItemAmmo STONE_AMMO_9MM = null;
        public static final ItemAmmo STONE_AMMO_45ACP = null;
        public static final ItemAmmo STONE_AMMO_556MM = null;
        public static final ItemAmmo STONE_AMMO_762MM = null;
        public static final ItemAmmo STONE_AMMO_12G = null;
        public static final ItemAmmo STONE_AMMO_CROSSBOW_BOLT = null;
        public static final ItemAmmo IRON_AMMO_9MM = null;
        public static final ItemAmmo IRON_AMMO_45ACP = null;
        public static final ItemAmmo IRON_AMMO_556MM = null;
        public static final ItemAmmo IRON_AMMO_762MM = null;
        public static final ItemAmmo IRON_AMMO_12G = null;
        public static final ItemAmmo IRON_AMMO_CROSSBOW_BOLT = null;
        public static final ItemAmmo GOLD_AMMO_9MM = null;
        public static final ItemAmmo GOLD_AMMO_45ACP = null;
        public static final ItemAmmo GOLD_AMMO_556MM = null;
        public static final ItemAmmo GOLD_AMMO_762MM = null;
        public static final ItemAmmo GOLD_AMMO_12G = null;
        public static final ItemAmmo GOLD_AMMO_CROSSBOW_BOLT = null;
        public static final ItemAmmo DIAMOND_AMMO_9MM = null;
        public static final ItemAmmo DIAMOND_AMMO_45ACP = null;
        public static final ItemAmmo DIAMOND_AMMO_556MM = null;
        public static final ItemAmmo DIAMOND_AMMO_762MM = null;
        public static final ItemAmmo DIAMOND_AMMO_12G = null;
        public static final ItemAmmo DIAMOND_AMMO_CROSSBOW_BOLT = null;
        public static final ItemAmmo EMERALD_AMMO_9MM = null;
        public static final ItemAmmo EMERALD_AMMO_45ACP = null;
        public static final ItemAmmo EMERALD_AMMO_556MM = null;
        public static final ItemAmmo EMERALD_AMMO_762MM = null;
        public static final ItemAmmo EMERALD_AMMO_12G = null;
        public static final ItemAmmo EMERALD_AMMO_CROSSBOW_BOLT = null;
        public static final ItemAmmo AMETHYST_AMMO_9MM = null;
        public static final ItemAmmo AMETHYST_AMMO_45ACP = null;
        public static final ItemAmmo AMETHYST_AMMO_556MM = null;
        public static final ItemAmmo AMETHYST_AMMO_762MM = null;
        public static final ItemAmmo AMETHYST_AMMO_12G = null;
        public static final ItemAmmo AMETHYST_AMMO_CROSSBOW_BOLT = null;
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
        public static final GRPGItem BOLT_FLETCHING = null;
        public static final ItemGrenade GRENADE = null;
        public static final ItemGrenade MASSIVE_GRENADE = null;
        public static final ItemGrenade IMPACT_GRENADE = null;
        public static final GRPGItem IRON_ORE_CHUNK = null;
        public static final GRPGItem GOLD_ORE_CHUNK = null;
        public static final ItemSkillBook SKILLPOINT_BOOK = null;
    }

    @GameRegistry.ObjectHolder(GunsRPG.MODID)
    public static final class GRPGBlocks {
        public static final GRPGOre AMETHYST_ORE = null;
        public static final BlockBlastFurnace BLAST_FURNACE = null;
        public static final BlockAirdrop AIRDROP = null;
        public static final BlockSmithingTable SMITHING_TABLE = null;
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
        public static final SoundEvent P92 = null;
        public static final SoundEvent MP5 = null;
        public static final SoundEvent SLR = null;
        public static final SoundEvent M24 = null;
        public static final SoundEvent WIN94 = null;
        public static final SoundEvent KAR98K_RELOAD_CLIP = null;
        public static final SoundEvent CROSSBOW_SHOOT = null;
        public static final SoundEvent CROSSBOW_RELOAD = null;
        public static final SoundEvent CROSSBOW_RELOAD_FAST = null;
        public static final SoundEvent FLARE_SHOOT = null;
        public static final SoundEvent SECOND_CHANCE_USE = null;
    }

    @GameRegistry.ObjectHolder(GunsRPG.MODID)
    public static final class Skills {
        public static final SkillType<CraftingSkill> GUNPOWDER_NOVICE = null;
        public static final SkillType<CraftingSkill> GUNPOWDER_EXPERT = null;
        public static final SkillType<CraftingSkill> GUNPOWDER_MASTER = null;
        public static final SkillType<CraftingSkill> BONE_GRINDER_I = null;
        public static final SkillType<CraftingSkill> BONE_GRINDER_II = null;
        public static final SkillType<CraftingSkill> BONE_GRINDER_III = null;
        public static final SkillType<BasicSkill> GUN_PARTS_SMITH = null;
        public static final SkillType<BasicSkill> PISTOL_ASSEMBLY = null;
        public static final SkillType<BasicSkill> SMG_ASSEMBLY = null;
        public static final SkillType<BasicSkill> CROSSBOW_ASSEMBLY = null;
        public static final SkillType<BasicSkill> SHOTGUN_ASSEMBLY = null;
        public static final SkillType<BasicSkill> ASSAULT_RIFLE_ASSEMBLY = null;
        public static final SkillType<BasicSkill> SNIPER_RIFLE_ASSEMBLY = null;
        public static final SkillType<BasicSkill> WOODEN_AMMO_SMITH = null;
        public static final SkillType<BasicSkill> STONE_AMMO_SMITH = null;
        public static final SkillType<BasicSkill> IRON_AMMO_SMITH = null;
        public static final SkillType<BasicSkill> GOLD_AMMO_SMITH = null;
        public static final SkillType<BasicSkill> DIAMOND_AMMO_SMITH = null;
        public static final SkillType<BasicSkill> EMERALD_AMMO_SMITH = null;
        public static final SkillType<BasicSkill> AMETHYST_AMMO_SMITH = null;
        public static final SkillType<BasicSkill> AMMO_SMITHING_MASTERY = null;
        public static final SkillType<BasicSkill> GRENADES = null; // 5s fuse
        public static final SkillType<BasicSkill> MASSIVE_GRENADES = null; // +2 power
        public static final SkillType<BasicSkill> IMPACT_GRENADES = null;
        public static final SkillType<BasicSkill> MEDIC = null;
        public static final SkillType<BasicSkill> DOCTOR = null;
        public static final SkillType<BasicSkill> EFFICIENT_MEDS = null;
        public static final SkillType<DataChangeSkill> POISON_RESISTANCE_I = null;
        public static final SkillType<DataChangeSkill> POISON_RESISTANCE_II = null;
        public static final SkillType<DataChangeSkill> POISON_RESISTANCE_III = null;
        public static final SkillType<DataChangeSkill> INFECTION_RESISTANCE_I = null;
        public static final SkillType<DataChangeSkill> INFECTION_RESISTANCE_II = null;
        public static final SkillType<DataChangeSkill> INFECTION_RESISTANCE_III = null;
        public static final SkillType<DataChangeSkill> BROKEN_BONE_RESISTANCE_I = null;
        public static final SkillType<DataChangeSkill> BROKEN_BONE_RESISTANCE_II = null;
        public static final SkillType<DataChangeSkill> BROKEN_BONE_RESISTANCE_III = null;
        public static final SkillType<DataChangeSkill> BLEEDING_RESISTANCE_I = null;
        public static final SkillType<DataChangeSkill> BLEEDING_RESISTANCE_II = null;
        public static final SkillType<DataChangeSkill> BLEEDING_RESISTANCE_III = null;
        public static final SkillType<DataChangeSkill> ACROBATICS_I = null;
        public static final SkillType<DataChangeSkill> ACROBATICS_II = null;
        public static final SkillType<DataChangeSkill> ACROBATICS_III = null;
        public static final SkillType<DataChangeSkill> SHARP_AXE_I = null;
        public static final SkillType<DataChangeSkill> SHARP_AXE_II = null;
        public static final SkillType<DataChangeSkill> SHARP_AXE_III = null;
        public static final SkillType<DataChangeSkill> SHARP_AXE_IV = null;
        public static final SkillType<DataChangeSkill> SHARP_AXE_V = null;
        public static final SkillType<LumberjackSkill> LUMBERJACK_I = null;
        public static final SkillType<LumberjackSkill> LUMBERJACK_II = null;
        public static final SkillType<LumberjackSkill> LUMBERJACK_III = null;
        public static final SkillType<LumberjackSkill> LUMBERJACK_IV = null;
        public static final SkillType<LumberjackSkill> LUMBERJACK_V = null;
        public static final SkillType<DataChangeSkill> HEAVY_PICKAXE_I = null;
        public static final SkillType<DataChangeSkill> HEAVY_PICKAXE_II = null;
        public static final SkillType<DataChangeSkill> HEAVY_PICKAXE_III = null;
        public static final SkillType<DataChangeSkill> HEAVY_PICKAXE_IV = null;
        public static final SkillType<DataChangeSkill> HEAVY_PICKAXE_V = null;
        public static final SkillType<MotherlodeSkill> MOTHER_LODE_I = null;
        public static final SkillType<MotherlodeSkill> MOTHER_LODE_II = null;
        public static final SkillType<MotherlodeSkill> MOTHER_LODE_III = null;
        public static final SkillType<MotherlodeSkill> MOTHER_LODE_IV = null;
        public static final SkillType<MotherlodeSkill> MOTHER_LODE_V = null;
        public static final SkillType<BasicSkill> BLACKSMITH = null;
        public static final SkillType<BasicSkill> MINERALOGIST = null;
        public static final SkillType<DataChangeSkill> STRONG_MUSCLES_I = null;
        public static final SkillType<DataChangeSkill> STRONG_MUSCLES_II = null;
        public static final SkillType<DataChangeSkill> STRONG_MUSCLES_III = null;
        public static final SkillType<DataChangeSkill> AGILITY_I = null;
        public static final SkillType<DataChangeSkill> AGILITY_II = null;
        public static final SkillType<DataChangeSkill> AGILITY_III = null;
        public static final SkillType<WellFedSkill> WELL_FED_I = null;
        public static final SkillType<WellFedSkill> WELL_FED_II = null;
        public static final SkillType<WellFedSkill> WELL_FED_III = null;
        public static final SkillType<SecondChanceSkill> SECOND_CHANCE_I = null;
        public static final SkillType<SecondChanceSkill> SECOND_CHANCE_II = null;
        public static final SkillType<SecondChanceSkill> SECOND_CHANCE_III = null;
        public static final SkillType<AdrenalineRushSkill> ADRENALINE_RUSH_I = null;
        public static final SkillType<AdrenalineRushSkill> ADRENALINE_RUSH_II = null;
        public static final SkillType<AdrenalineRushSkill> ADRENALINE_RUSH_III = null;
        public static final SkillType<GodHelpUsSkill> GOD_HELP_US = null;
        public static final SkillType<DataChangeSkill> SKULL_CRUSHER_I = null;
        public static final SkillType<DataChangeSkill> SKULL_CRUSHER_II = null;
        public static final SkillType<DataChangeSkill> SKULL_CRUSHER_III = null;
        public static final SkillType<LightHunterSkill> LIGHT_HUNTER = null;
        public static final SkillType<BasicSkill> AVENGE_ME_FRIENDS = null;
        public static final SkillType<LikeACatSkill> LIKE_A_CAT_I = null;
        public static final SkillType<LikeACatSkill> LIKE_A_CAT_II = null;
        public static final SkillType<LikeACatSkill> LIKE_A_CAT_III = null;
        public static final SkillType<WarMachineSkill> WAR_MACHINE = null;
        public static final SkillType<BasicSkill> PISTOL_QUICKDRAW = null;
        public static final SkillType<BasicSkill> PISTOL_EXTENDED = null;
        public static final SkillType<BasicSkill> PISTOL_TOUGH_SPRING = null;
        public static final SkillType<BasicSkill> PISTOL_CARBON_BARREL = null;
        public static final SkillType<BasicSkill> PISTOL_SUPPRESSOR = null;
        public static final SkillType<BasicSkill> PISTOL_HEAVY_BULLETS = null;
        public static final SkillType<BasicSkill> PISTOL_DUAL_WIELD = null;
        public static final SkillType<BasicSkill> SMG_QUICKDRAW = null;
        public static final SkillType<BasicSkill> SMG_EXTENDED = null;
        public static final SkillType<BasicSkill> SMG_VERTICAL_GRIP = null;
        public static final SkillType<BasicSkill> SMG_TOUGH_SPRING = null;
        public static final SkillType<BasicSkill> SMG_RED_DOT = null;
        public static final SkillType<BasicSkill> SMG_SUPPRESSOR = null;
        public static final SkillType<BasicSkill> SMG_COMMANDO = null;
        public static final SkillType<BasicSkill> CROSSBOW_QUIVER = null;
        public static final SkillType<BasicSkill> CROSSBOW_POISONED_BOLTS = null;
        public static final SkillType<BasicSkill> CROSSBOW_HUNTER = null;
        public static final SkillType<BasicSkill> CROSSBOW_TOUGH_BOWSTRING = null;
        public static final SkillType<BasicSkill> CROSSBOW_PENETRATOR = null;
        public static final SkillType<BasicSkill> CROSSBOW_SCOPE = null;
        public static final SkillType<BasicSkill> CROSSBOW_REPEATER = null;
        public static final SkillType<BasicSkill> SHOTGUN_BULLET_LOOPS = null;
        public static final SkillType<BasicSkill> SHOTGUN_EXTENDED = null;
        public static final SkillType<BasicSkill> SHOTGUN_PUMP_IN_ACTION = null;
        public static final SkillType<BasicSkill> SHOTGUN_CHOKE = null;
        public static final SkillType<BasicSkill> SHOTGUN_NEVER_GIVE_UP = null;
        public static final SkillType<BasicSkill> SHOTGUN_EXTENDED_BARREL = null;
        public static final SkillType<BasicSkill> AR_TOUGH_SPRING = null;
        public static final SkillType<BasicSkill> AR_VERTICAL_GRIP = null;
        public static final SkillType<BasicSkill> AR_EXTENDED = null;
        public static final SkillType<BasicSkill> AR_RED_DOT = null;
        public static final SkillType<BasicSkill> AR_SUPPRESSOR = null;
        public static final SkillType<BasicSkill> AR_CHEEKPAD = null;
        public static final SkillType<BasicSkill> AR_ADAPTIVE_CHAMBERING = null;
        public static final SkillType<BasicSkill> SR_SCOPE = null;
        public static final SkillType<BasicSkill> SR_CHEEKPAD = null;
        public static final SkillType<BasicSkill> SR_EXTENDED = null;
        public static final SkillType<BasicSkill> SR_SUPPRESSOR = null;
        public static final SkillType<BasicSkill> SR_FAST_HANDS = null;
        public static final SkillType<BasicSkill> SR_PENETRATOR = null;
        public static final SkillType<BasicSkill> SR_DEAD_EYE = null;
    }

    @Mod.EventBusSubscriber(modid = GunsRPG.MODID)
    public static final class Handler {

        private static List<ItemBlock> queue = new ArrayList<>();
        private static int id = -1;

        @SuppressWarnings("unchecked")
        @SubscribeEvent
        public static void createRegistries(RegistryEvent.NewRegistry event) {
            ResourceLocation location = GunsRPG.makeResource("skill");
            createRegistry(location, SkillType.class).create();
            SKILLS = RegistryManager.ACTIVE.getRegistry(location);
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        @SubscribeEvent
        public static void onSkillRegister(RegistryEvent.Register event) {
            if(!event.getRegistry().getRegistrySuperType().equals(SkillType.class)) return;
            event.getRegistry().registerAll(
                    SkillType.Builder.create(type -> new CraftingSkill(type, 2, CraftingSkill::getGunpowderYield)).setGunCategory().setRegistryName("gunpowder_novice").setTreeStartPoint().requiredLevel(0).price(1).childs(() -> ModUtils.newList(Skills.GUNPOWDER_EXPERT)).build(),
                    SkillType.Builder.create(type -> new CraftingSkill(type, 4, CraftingSkill::getGunpowderYield)).setGunCategory().setRegistryName("gunpowder_expert").requiredLevel(15).price(2).childs(() -> ModUtils.newList(Skills.GUNPOWDER_MASTER)).build(),
                    SkillType.Builder.create(type -> new CraftingSkill(type, 6, CraftingSkill::getGunpowderYield)).setGunCategory().setRegistryName("gunpowder_master").requiredLevel(30).price(3).build(),
                    SkillType.Builder.create(type -> new CraftingSkill(type, 4, CraftingSkill::getBonemealYield)).setGunCategory().setRegistryName("bone_grinder_i").setTreeStartPoint().requiredLevel(0).price(1).childs(() -> Collections.singletonList(Skills.BONE_GRINDER_II)).build(),
                    SkillType.Builder.create(type -> new CraftingSkill(type, 5, CraftingSkill::getBonemealYield)).setGunCategory().setRegistryName("bone_grinder_ii").requiredLevel(15).price(2).childs(() -> Collections.singletonList(Skills.BONE_GRINDER_III)).build(),
                    SkillType.Builder.create(type -> new CraftingSkill(type, 6, CraftingSkill::getBonemealYield)).setGunCategory().setRegistryName("bone_grinder_iii").requiredLevel(30).price(3).build(),
                    SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("wooden_ammo_smith").setTreeStartPoint().requiredLevel(0).price(1).childs(() -> ModUtils.newList(Skills.STONE_AMMO_SMITH)).build(),
                    SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("stone_ammo_smith").requiredLevel(5).price(1).childs(() -> ModUtils.newList(Skills.IRON_AMMO_SMITH)).build(),
                    SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("iron_ammo_smith").requiredLevel(10).price(2).childs(() -> ModUtils.newList(Skills.GOLD_AMMO_SMITH)).build(),
                    SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("gold_ammo_smith").requiredLevel(15).price(2).childs(() -> ModUtils.newList(Skills.DIAMOND_AMMO_SMITH)).build(),
                    SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("diamond_ammo_smith").requiredLevel(20).price(3).childs(() -> ModUtils.newList(Skills.EMERALD_AMMO_SMITH)).build(),
                    SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("emerald_ammo_smith").requiredLevel(30).price(4).childs(() -> ModUtils.newList(Skills.AMETHYST_AMMO_SMITH)).build(),
                    SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("amethyst_ammo_smith").requiredLevel(45).price(6).build(),
                    SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("ammo_smithing_mastery").setTreeStartPoint().requiredLevel(60).price(10).build(),
                    SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("gun_parts_smith").setTreeStartPoint().requiredLevel(5).price(1).childs(() -> ModUtils.newList(Skills.PISTOL_ASSEMBLY, Skills.SMG_ASSEMBLY, Skills.CROSSBOW_ASSEMBLY, Skills.SHOTGUN_ASSEMBLY, Skills.ASSAULT_RIFLE_ASSEMBLY, Skills.SNIPER_RIFLE_ASSEMBLY)).build(),
                    SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("pistol_assembly").requiredLevel(5).price(2).setCustomDisplay().renderFactory(() -> GRPGItems.PISTOL).childs(() -> ModUtils.newList(Skills.PISTOL_QUICKDRAW, Skills.PISTOL_EXTENDED, Skills.PISTOL_TOUGH_SPRING, Skills.PISTOL_CARBON_BARREL, Skills.PISTOL_SUPPRESSOR, Skills.PISTOL_HEAVY_BULLETS, Skills.PISTOL_DUAL_WIELD)).build(),
                    SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("smg_assembly").requiredLevel(10).price(3).setCustomDisplay().renderFactory(() -> GRPGItems.SMG).childs(() -> ModUtils.newList(Skills.SMG_QUICKDRAW, Skills.SMG_EXTENDED, Skills.SMG_VERTICAL_GRIP, Skills.SMG_TOUGH_SPRING, Skills.SMG_RED_DOT, Skills.SMG_SUPPRESSOR, Skills.SMG_COMMANDO)).build(),
                    SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("crossbow_assembly").requiredLevel(15).price(3).setCustomDisplay().renderFactory(() -> GRPGItems.CROSSBOW).childs(() -> ModUtils.newList(Skills.CROSSBOW_QUIVER, Skills.CROSSBOW_POISONED_BOLTS, Skills.CROSSBOW_HUNTER, Skills.CROSSBOW_TOUGH_BOWSTRING, Skills.CROSSBOW_PENETRATOR, Skills.CROSSBOW_SCOPE, Skills.CROSSBOW_REPEATER)).build(),
                    SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("shotgun_assembly").requiredLevel(20).price(4).setCustomDisplay().renderFactory(() -> GRPGItems.SHOTGUN).childs(() -> ModUtils.newList(Skills.SHOTGUN_BULLET_LOOPS, Skills.SHOTGUN_EXTENDED, Skills.SHOTGUN_PUMP_IN_ACTION, Skills.SHOTGUN_CHOKE, Skills.SHOTGUN_NEVER_GIVE_UP, Skills.SHOTGUN_EXTENDED_BARREL)).build(),
                    SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("assault_rifle_assembly").requiredLevel(25).price(4).setCustomDisplay().renderFactory(() -> GRPGItems.ASSAULT_RIFLE).childs(() -> ModUtils.newList(Skills.AR_TOUGH_SPRING, Skills.AR_VERTICAL_GRIP, Skills.AR_EXTENDED, Skills.AR_RED_DOT, Skills.AR_SUPPRESSOR, Skills.AR_CHEEKPAD, Skills.AR_ADAPTIVE_CHAMBERING)).build(),
                    SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("sniper_rifle_assembly").requiredLevel(35).price(5).setCustomDisplay().renderFactory(() -> GRPGItems.SNIPER_RIFLE).childs(() -> ModUtils.newList(Skills.SR_SCOPE, Skills.SR_CHEEKPAD, Skills.SR_EXTENDED, Skills.SR_SUPPRESSOR, Skills.SR_FAST_HANDS, Skills.SR_PENETRATOR, Skills.SR_DEAD_EYE)).build(),
                    SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("grenades").setTreeStartPoint().requiredLevel(15).price(3).childs(() -> ModUtils.newList(Skills.MASSIVE_GRENADES, Skills.IMPACT_GRENADES)).build(),
                    SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("massive_grenades").requiredLevel(30).price(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("impact_grenades").requiredLevel(40).price(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setResistanceCategory().setRegistryName("medic").setTreeStartPoint().requiredLevel(10).price(3).childs(() -> ModUtils.newList(Skills.DOCTOR)).build(),
                    SkillType.Builder.create(BasicSkill::new).setResistanceCategory().setRegistryName("doctor").requiredLevel(25).price(3).build(),
                    SkillType.Builder.create(BasicSkill::new).setResistanceCategory().setRegistryName("efficient_meds").setTreeStartPoint().requiredLevel(30).price(2).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setPoisonResistance(20))).setResistanceCategory().setRegistryName("poison_resistance_i").setTreeStartPoint().requiredLevel(5).price(1).childAndOverride(() -> Skills.POISON_RESISTANCE_II).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setPoisonResistance(40))).setResistanceCategory().setRegistryName("poison_resistance_ii").requiredLevel(20).price(2).childAndOverride(() -> Skills.POISON_RESISTANCE_III).build(),
                    SkillType.Builder.create(type -> new DataChangeSkill(type, skills -> skills.setPoisonResistance(60))).setResistanceCategory().setRegistryName("poison_resistance_iii").requiredLevel(40).price(3).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setInfectionResistance(20))).setResistanceCategory().setRegistryName("infection_resistance_i").setTreeStartPoint().requiredLevel(5).price(1).childAndOverride(() -> Skills.INFECTION_RESISTANCE_II).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setInfectionResistance(40))).setResistanceCategory().setRegistryName("infection_resistance_ii").requiredLevel(20).price(2).childAndOverride(() -> Skills.INFECTION_RESISTANCE_III).build(),
                    SkillType.Builder.create(type -> new DataChangeSkill(type, skills -> skills.setInfectionResistance(60))).setResistanceCategory().setRegistryName("infection_resistance_iii").requiredLevel(40).price(3).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setBrokenBoneResistance(20))).setResistanceCategory().setRegistryName("broken_bone_resistance_i").setTreeStartPoint().requiredLevel(5).price(1).childAndOverride(() -> Skills.BROKEN_BONE_RESISTANCE_II).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setBrokenBoneResistance(40))).setResistanceCategory().setRegistryName("broken_bone_resistance_ii").requiredLevel(20).price(2).childAndOverride(() -> Skills.BROKEN_BONE_RESISTANCE_III).build(),
                    SkillType.Builder.create(type -> new DataChangeSkill(type, skills -> skills.setBrokenBoneResistance(60))).setResistanceCategory().setRegistryName("broken_bone_resistance_iii").requiredLevel(40).price(3).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setBleedResistance(20))).setResistanceCategory().setRegistryName("bleeding_resistance_i").setTreeStartPoint().requiredLevel(5).price(1).childAndOverride(() -> Skills.BLEEDING_RESISTANCE_II).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setBleedResistance(40))).setResistanceCategory().setRegistryName("bleeding_resistance_ii").requiredLevel(20).price(2).childAndOverride(() -> Skills.BLEEDING_RESISTANCE_III).build(),
                    SkillType.Builder.create(type -> new DataChangeSkill(type, skills -> skills.setBleedResistance(60))).setResistanceCategory().setRegistryName("bleeding_resistance_iii").requiredLevel(40).price(3).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> {
                        skills.setAcrobaticsFallResistance(0.20F);
                        skills.setAcrobaticsExplosionResistance(0.10F);
                    })).setResistanceCategory().setRegistryName("acrobatics_i").descriptionLength(3).setTreeStartPoint().requiredLevel(5).price(2).childAndOverride(() -> Skills.ACROBATICS_II).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> {
                        skills.setAcrobaticsFallResistance(0.45F);
                        skills.setAcrobaticsExplosionResistance(0.25F);
                    })).setResistanceCategory().setRegistryName("acrobatics_ii").descriptionLength(3).requiredLevel(15).price(3).childAndOverride(() -> Skills.ACROBATICS_III).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> {
                        skills.setAcrobaticsFallResistance(0.70F);
                        skills.setAcrobaticsExplosionResistance(0.40F);
                    })).setResistanceCategory().setRegistryName("acrobatics_iii").descriptionLength(3).requiredLevel(25).price(4).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setAxeMiningSpeed(0.15F))).setMiningCategory().setRegistryName("sharp_axe_i").setTreeStartPoint().requiredLevel(0).price(1).childAndOverride(() -> Skills.SHARP_AXE_II).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setAxeMiningSpeed(0.3F))).setMiningCategory().setRegistryName("sharp_axe_ii").requiredLevel(10).price(2).childAndOverride(() -> Skills.SHARP_AXE_III).build(),
                    SkillType.Builder.create(type -> new DataChangeSkill(type, skills -> skills.setAxeMiningSpeed(0.45F))).setMiningCategory().setRegistryName("sharp_axe_iii").requiredLevel(20).price(3).childs(() -> Collections.singletonList(Skills.SHARP_AXE_IV)).build(),
                    SkillType.Builder.create(type -> new DataChangeSkill(type, skills -> skills.setAxeMiningSpeed(0.6F))).setMiningCategory().setRegistryName("sharp_axe_iv").requiredLevel(35).price(4).childs(() -> Collections.singletonList(Skills.SHARP_AXE_V)).build(),
                    SkillType.Builder.create(type -> new DataChangeSkill(type, skills -> skills.setAxeMiningSpeed(0.8F))).setMiningCategory().setRegistryName("sharp_axe_v").requiredLevel(50).price(5).build(),
                    SkillType.Builder.<LumberjackSkill>create(type -> new LumberjackSkill(type, 1)).setMiningCategory().setRegistryName("lumberjack_i").descriptionLength(3).setTreeStartPoint().requiredLevel(5).price(2).childAndOverride(() -> Skills.LUMBERJACK_II).build(),
                    SkillType.Builder.<LumberjackSkill>create(type -> new LumberjackSkill(type, 2)).setMiningCategory().setRegistryName("lumberjack_ii").descriptionLength(3).requiredLevel(10).price(3).childAndOverride(() -> Skills.LUMBERJACK_III).build(),
                    SkillType.Builder.<LumberjackSkill>create(type -> new LumberjackSkill(type, 3)).setMiningCategory().setRegistryName("lumberjack_iii").descriptionLength(3).requiredLevel(25).price(4).childAndOverride(() -> Skills.LUMBERJACK_IV).build(),
                    SkillType.Builder.<LumberjackSkill>create(type -> new LumberjackSkill(type, 4)).setMiningCategory().setRegistryName("lumberjack_iv").descriptionLength(3).requiredLevel(40).price(5).childAndOverride(() -> Skills.LUMBERJACK_V).build(),
                    SkillType.Builder.<LumberjackSkill>create(type -> new LumberjackSkill(type, 5)).setMiningCategory().setRegistryName("lumberjack_v").descriptionLength(3).requiredLevel(55).price(6).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setPickaxeMiningSpeed(0.15F))).setMiningCategory().setRegistryName("heavy_pickaxe_i").setTreeStartPoint().requiredLevel(0).price(1).childAndOverride(() -> Skills.HEAVY_PICKAXE_II).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setPickaxeMiningSpeed(0.3F))).setMiningCategory().setRegistryName("heavy_pickaxe_ii").requiredLevel(10).price(2).childAndOverride(() -> Skills.HEAVY_PICKAXE_III).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setPickaxeMiningSpeed(0.45F))).setMiningCategory().setRegistryName("heavy_pickaxe_iii").requiredLevel(20).price(3).childAndOverride(() -> Skills.HEAVY_PICKAXE_IV).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setPickaxeMiningSpeed(0.60F))).setMiningCategory().setRegistryName("heavy_pickaxe_iv").requiredLevel(35).price(4).childAndOverride(() -> Skills.HEAVY_PICKAXE_V).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setPickaxeMiningSpeed(0.80F))).setMiningCategory().setRegistryName("heavy_pickaxe_v").requiredLevel(50).price(5).build(),
                    SkillType.Builder.<MotherlodeSkill>create(type -> new MotherlodeSkill(type, 1)).setMiningCategory().setRegistryName("mother_lode_i").setTreeStartPoint().requiredLevel(5).price(2).childAndOverride(() -> Skills.MOTHER_LODE_II).build(),
                    SkillType.Builder.<MotherlodeSkill>create(type -> new MotherlodeSkill(type, 2)).setMiningCategory().setRegistryName("mother_lode_ii").requiredLevel(15).price(3).childAndOverride(() -> Skills.MOTHER_LODE_III).build(),
                    SkillType.Builder.<MotherlodeSkill>create(type -> new MotherlodeSkill(type, 3)).setMiningCategory().setRegistryName("mother_lode_iii").requiredLevel(30).price(4).childAndOverride(() -> Skills.MOTHER_LODE_IV).build(),
                    SkillType.Builder.<MotherlodeSkill>create(type -> new MotherlodeSkill(type, 4)).setMiningCategory().setRegistryName("mother_lode_iv").descriptionLength(2).requiredLevel(45).price(6).childAndOverride(() -> Skills.MOTHER_LODE_V).build(),
                    SkillType.Builder.<MotherlodeSkill>create(type -> new MotherlodeSkill(type, 5)).setMiningCategory().setRegistryName("mother_lode_v").descriptionLength(2).requiredLevel(65).price(7).build(),
                    SkillType.Builder.create(BasicSkill::new).setMiningCategory().setRegistryName("blacksmith").setTreeStartPoint().requiredLevel(40).price(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setMiningCategory().setRegistryName("mineralogist").setTreeStartPoint().requiredLevel(50).price(6).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setExtraDamage(1))).setSurvivalCategory().setRegistryName("strong_muscles_i").setTreeStartPoint().requiredLevel(10).price(2).childAndOverride(() -> Skills.STRONG_MUSCLES_II).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setExtraDamage(2))).setSurvivalCategory().setRegistryName("strong_muscles_ii").requiredLevel(25).price(3).childAndOverride(() -> Skills.STRONG_MUSCLES_III).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setExtraDamage(4))).setSurvivalCategory().setRegistryName("strong_muscles_iii").requiredLevel(40).price(5).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setAgilitySpeed(0.005F))).setSurvivalCategory().setRegistryName("agility_i").setTreeStartPoint().requiredLevel(10).price(2).childAndOverride(() -> Skills.AGILITY_II).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setAgilitySpeed(0.010F))).setSurvivalCategory().setRegistryName("agility_ii").requiredLevel(20).price(3).childAndOverride(() -> Skills.AGILITY_III).build(),
                    SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setAgilitySpeed(0.020F))).setSurvivalCategory().setRegistryName("agility_iii").requiredLevel(35).price(4).build(),
                    SkillType.Builder.<AdrenalineRushSkill>create(type -> new AdrenalineRushSkill(type, 0, 0.05f)).setSurvivalCategory().setRegistryName("adrenaline_rush_i").descriptionLength(2).setTreeStartPoint().requiredLevel(15).price(2).childAndOverride(() -> Skills.ADRENALINE_RUSH_II).build(),
                    SkillType.Builder.<AdrenalineRushSkill>create(type -> new AdrenalineRushSkill(type, 1, 0.1f)).setSurvivalCategory().setRegistryName("adrenaline_rush_ii").descriptionLength(2).requiredLevel(25).price(3).childAndOverride(() -> Skills.ADRENALINE_RUSH_III).build(),
                    SkillType.Builder.<AdrenalineRushSkill>create(type -> new AdrenalineRushSkill(type, 2, 0.2f)).setSurvivalCategory().setRegistryName("adrenaline_rush_iii").descriptionLength(2).requiredLevel(40).price(5).build(),
                    SkillType.Builder.<WellFedSkill>create(type -> new WellFedSkill(type, 1, 0.3F)).setSurvivalCategory().setRegistryName("well_fed_i").descriptionLength(2).setTreeStartPoint().requiredLevel(20).price(2).childAndOverride(() -> Skills.WELL_FED_II).build(),
                    SkillType.Builder.<WellFedSkill>create(type -> new WellFedSkill(type, 2, 0.4F)).setSurvivalCategory().setRegistryName("well_fed_ii").descriptionLength(2).requiredLevel(35).price(3).childAndOverride(() -> Skills.WELL_FED_III).build(),
                    SkillType.Builder.<WellFedSkill>create(type -> new WellFedSkill(type, 3, 0.55F)).setSurvivalCategory().setRegistryName("well_fed_iii").descriptionLength(2).requiredLevel(55).price(5).build(),
                    SkillType.Builder.<SecondChanceSkill>create(type -> new SecondChanceSkill(type, 18000, 10)).setSurvivalCategory().setRegistryName("second_chance_i").descriptionLength(2).setTreeStartPoint().requiredLevel(50).price(7).childAndOverride(() -> Skills.SECOND_CHANCE_II).build(),
                    SkillType.Builder.<SecondChanceSkill>create(type -> new SecondChanceSkill(type, 14400, 15)).setSurvivalCategory().setRegistryName("second_chance_ii").descriptionLength(2).requiredLevel(75).price(9).childAndOverride(() -> Skills.SECOND_CHANCE_III).build(),
                    SkillType.Builder.create(type -> new SecondChanceSkill(type, 10800, 20)).setSurvivalCategory().setRegistryName("second_chance_iii").descriptionLength(2).requiredLevel(90).price(10).build(),
                    SkillType.Builder.create(GodHelpUsSkill::new).setSurvivalCategory().setRegistryName("god_help_us").descriptionLength(2).setTreeStartPoint().requiredLevel(60).price(8).build(),
                    SkillType.Builder.create(type -> new DataChangeSkill(type, skills -> skills.setInstantKillChance(0.01F))).setSurvivalCategory().setRegistryName("skull_crusher_i").setTreeStartPoint().requiredLevel(20).price(2).childs(() -> Collections.singletonList(Skills.SKULL_CRUSHER_II)).build(),
                    SkillType.Builder.create(type -> new DataChangeSkill(type, skills -> skills.setInstantKillChance(0.03F))).setSurvivalCategory().setRegistryName("skull_crusher_ii").requiredLevel(35).price(4).childs(() -> Collections.singletonList(Skills.SKULL_CRUSHER_III)).build(),
                    SkillType.Builder.create(type -> new DataChangeSkill(type, skills -> skills.setInstantKillChance(0.05F))).setSurvivalCategory().setRegistryName("skull_crusher_iii").requiredLevel(60).price(6).build(),
                    SkillType.Builder.create(LightHunterSkill::new).setSurvivalCategory().setRegistryName("light_hunter").descriptionLength(4).setTreeStartPoint().requiredLevel(35).price(7).build(),
                    SkillType.Builder.<LikeACatSkill>create(type -> new LikeACatSkill(type, 6000, 2400)).setSurvivalCategory().setRegistryName("like_a_cat_i").descriptionLength(2).setTreeStartPoint().requiredLevel(15).price(2).childAndOverride(() -> Skills.LIKE_A_CAT_II).build(),
                    SkillType.Builder.<LikeACatSkill>create(type -> new LikeACatSkill(type, 6000, 4800)).setSurvivalCategory().setRegistryName("like_a_cat_ii").descriptionLength(2).requiredLevel(30).price(3).childAndOverride(() -> Skills.LIKE_A_CAT_III).build(),
                    SkillType.Builder.<LikeACatSkill>create(type -> new LikeACatSkill(type, 6000, 8400)).setSurvivalCategory().setRegistryName("like_a_cat_iii").descriptionLength(2).requiredLevel(45).price(4).build(),
                    SkillType.Builder.create(BasicSkill::new).setSurvivalCategory().setRegistryName("avenge_me_friends").descriptionLength(3).setTreeStartPoint().requiredLevel(65).price(5).build(),
                    SkillType.Builder.create(WarMachineSkill::new).setSurvivalCategory().setRegistryName("war_machine").setTreeStartPoint().requiredLevel(100).price(15).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("pistol_quickdraw").iconPathNormal("quickdraw").criteria(CriteriaTypes.getPistolCriteria()).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("pistol_extended").iconPathNormal("extended").criteria(CriteriaTypes.getPistolCriteria()).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("pistol_tough_spring").iconPathNormal("tough_spring").criteria(CriteriaTypes.getPistolCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("pistol_carbon_barrel").criteria(CriteriaTypes.getPistolCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("pistol_suppressor").iconPathNormal("suppressor").criteria(CriteriaTypes.getPistolCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("pistol_heavy_bullets").descriptionLength(3).criteria(CriteriaTypes.getPistolCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("pistol_dual_wield").criteria(CriteriaTypes.getPistolCriteria()).requiredLevel(7).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("smg_quickdraw").iconPathNormal("quickdraw").criteria(CriteriaTypes.getSmgCriteria()).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("smg_extended").iconPathNormal("extended").criteria(CriteriaTypes.getSmgCriteria()).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("smg_vertical_grip").iconPathNormal("vertical_grip").criteria(CriteriaTypes.getSmgCriteria()).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("smg_tough_spring").iconPathNormal("tough_spring").criteria(CriteriaTypes.getSmgCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("smg_red_dot").iconPathNormal("red_dot").criteria(CriteriaTypes.getSmgCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("smg_suppressor").iconPathNormal("suppressor").criteria(CriteriaTypes.getSmgCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("smg_commando").descriptionLength(3).criteria(CriteriaTypes.getSmgCriteria()).requiredLevel(7).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_quiver").criteria(CriteriaTypes.getCrossbowCriteria()).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_poisoned_bolts").criteria(CriteriaTypes.getCrossbowCriteria()).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_hunter").criteria(CriteriaTypes.getCrossbowCriteria()).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_tough_bowstring").criteria(CriteriaTypes.getCrossbowCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_penetrator").iconPathNormal("penetrator").criteria(CriteriaTypes.getCrossbowCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_scope").iconPathNormal("scope").criteria(CriteriaTypes.getCrossbowCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_repeater").descriptionLength(2).criteria(CriteriaTypes.getCrossbowCriteria()).requiredLevel(7).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("shotgun_bullet_loops").criteria(CriteriaTypes.getShotgunCriteria()).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("shotgun_extended").iconPathNormal("extended").criteria(CriteriaTypes.getShotgunCriteria()).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("shotgun_pump_in_action").criteria(CriteriaTypes.getShotgunCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("shotgun_choke").criteria(CriteriaTypes.getShotgunCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("shotgun_never_give_up").criteria(CriteriaTypes.getShotgunCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("shotgun_extended_barrel").criteria(CriteriaTypes.getShotgunCriteria()).requiredLevel(7).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ar_tough_spring").iconPathNormal("tough_spring").criteria(CriteriaTypes.getAssaltRifleCriteria()).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ar_vertical_grip").iconPathNormal("vertical_grip").criteria(CriteriaTypes.getAssaltRifleCriteria()).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ar_extended").iconPathNormal("extended").criteria(CriteriaTypes.getAssaltRifleCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ar_red_dot").iconPathNormal("red_dot").criteria(CriteriaTypes.getAssaltRifleCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ar_suppressor").iconPathNormal("suppressor").criteria(CriteriaTypes.getAssaltRifleCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ar_cheekpad").iconPathNormal("cheekpad").criteria(CriteriaTypes.getAssaltRifleCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ar_adaptive_chambering").criteria(CriteriaTypes.getAssaltRifleCriteria()).requiredLevel(7).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sr_scope").iconPathNormal("scope").criteria(CriteriaTypes.getSniperRifleCriteria()).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sr_cheekpad").iconPathNormal("cheekpad").criteria(CriteriaTypes.getSniperRifleCriteria()).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sr_extended").iconPathNormal("extended").criteria(CriteriaTypes.getSniperRifleCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sr_suppressor").iconPathNormal("suppressor").criteria(CriteriaTypes.getSniperRifleCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sr_fast_hands").criteria(CriteriaTypes.getSniperRifleCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sr_penetrator").iconPathNormal("penetrator").criteria(CriteriaTypes.getSniperRifleCriteria()).requiredLevel(5).build(),
                    SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sr_dead_eye").criteria(CriteriaTypes.getSniperRifleCriteria()).requiredLevel(7).build()
            );
        }

        @SubscribeEvent
        public static void onBlockRegister(RegistryEvent.Register<Block> event) {
            event.getRegistry().registerAll(
                    new GRPGOre("amethyst_ore", () -> GRPGItems.AMETHYST),
                    new BlockBlastFurnace("blast_furnace"),
                    new BlockAirdrop("airdrop"),
                    new BlockSmithingTable("smithing_table")
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
                    new ItemAmmo("wooden_ammo_9mm", AmmoType._9MM, AmmoMaterial.WOOD, () -> GRPGItems.PISTOL),
                    new ItemAmmo("wooden_ammo_45acp", AmmoType._45ACP, AmmoMaterial.WOOD, () -> GRPGItems.SMG),
                    new ItemAmmo("wooden_ammo_556mm", AmmoType._556MM, AmmoMaterial.WOOD, () -> GRPGItems.ASSAULT_RIFLE),
                    new ItemAmmo("wooden_ammo_762mm", AmmoType._762MM, AmmoMaterial.WOOD, () -> GRPGItems.SNIPER_RIFLE),
                    new ItemAmmo("wooden_ammo_12g", AmmoType._12G, AmmoMaterial.WOOD, () -> GRPGItems.SHOTGUN),
                    new ItemAmmo("wooden_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterial.WOOD, () -> GRPGItems.CROSSBOW),
                    new ItemAmmo("stone_ammo_9mm", AmmoType._9MM, AmmoMaterial.STONE, () -> GRPGItems.PISTOL),
                    new ItemAmmo("stone_ammo_45acp", AmmoType._45ACP, AmmoMaterial.STONE, () -> GRPGItems.SMG),
                    new ItemAmmo("stone_ammo_556mm", AmmoType._556MM, AmmoMaterial.STONE, () -> GRPGItems.ASSAULT_RIFLE),
                    new ItemAmmo("stone_ammo_762mm", AmmoType._762MM, AmmoMaterial.STONE, () -> GRPGItems.SNIPER_RIFLE),
                    new ItemAmmo("stone_ammo_12g", AmmoType._12G, AmmoMaterial.STONE, () -> GRPGItems.SHOTGUN),
                    new ItemAmmo("stone_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterial.STONE, () -> GRPGItems.CROSSBOW),
                    new ItemAmmo("iron_ammo_9mm", AmmoType._9MM, AmmoMaterial.IRON, () -> GRPGItems.PISTOL),
                    new ItemAmmo("iron_ammo_45acp", AmmoType._45ACP, AmmoMaterial.IRON, () -> GRPGItems.SMG),
                    new ItemAmmo("iron_ammo_556mm", AmmoType._556MM, AmmoMaterial.IRON, () -> GRPGItems.ASSAULT_RIFLE),
                    new ItemAmmo("iron_ammo_762mm", AmmoType._762MM, AmmoMaterial.IRON, () -> GRPGItems.SNIPER_RIFLE),
                    new ItemAmmo("iron_ammo_12g", AmmoType._12G, AmmoMaterial.IRON, () -> GRPGItems.SHOTGUN),
                    new ItemAmmo("iron_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterial.IRON, () -> GRPGItems.CROSSBOW),
                    new ItemAmmo("gold_ammo_9mm", AmmoType._9MM, AmmoMaterial.GOLD, () -> GRPGItems.PISTOL),
                    new ItemAmmo("gold_ammo_45acp", AmmoType._45ACP, AmmoMaterial.GOLD, () -> GRPGItems.SMG),
                    new ItemAmmo("gold_ammo_556mm", AmmoType._556MM, AmmoMaterial.GOLD, () -> GRPGItems.ASSAULT_RIFLE),
                    new ItemAmmo("gold_ammo_762mm", AmmoType._762MM, AmmoMaterial.GOLD, () -> GRPGItems.SNIPER_RIFLE),
                    new ItemAmmo("gold_ammo_12g", AmmoType._12G, AmmoMaterial.GOLD, () -> GRPGItems.SHOTGUN),
                    new ItemAmmo("gold_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterial.GOLD, () -> GRPGItems.CROSSBOW),
                    new ItemAmmo("diamond_ammo_9mm", AmmoType._9MM, AmmoMaterial.DIAMOND, () -> GRPGItems.PISTOL),
                    new ItemAmmo("diamond_ammo_45acp", AmmoType._45ACP, AmmoMaterial.DIAMOND, () -> GRPGItems.SMG),
                    new ItemAmmo("diamond_ammo_556mm", AmmoType._556MM, AmmoMaterial.DIAMOND, () -> GRPGItems.ASSAULT_RIFLE),
                    new ItemAmmo("diamond_ammo_762mm", AmmoType._762MM, AmmoMaterial.DIAMOND, () -> GRPGItems.SNIPER_RIFLE),
                    new ItemAmmo("diamond_ammo_12g", AmmoType._12G, AmmoMaterial.DIAMOND, () -> GRPGItems.SHOTGUN),
                    new ItemAmmo("diamond_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterial.DIAMOND, () -> GRPGItems.CROSSBOW),
                    new ItemAmmo("emerald_ammo_9mm", AmmoType._9MM, AmmoMaterial.EMERALD, () -> GRPGItems.PISTOL),
                    new ItemAmmo("emerald_ammo_45acp", AmmoType._45ACP, AmmoMaterial.EMERALD, () -> GRPGItems.SMG),
                    new ItemAmmo("emerald_ammo_556mm", AmmoType._556MM, AmmoMaterial.EMERALD, () -> GRPGItems.ASSAULT_RIFLE),
                    new ItemAmmo("emerald_ammo_762mm", AmmoType._762MM, AmmoMaterial.EMERALD, () -> GRPGItems.SNIPER_RIFLE),
                    new ItemAmmo("emerald_ammo_12g", AmmoType._12G, AmmoMaterial.EMERALD, () -> GRPGItems.SHOTGUN),
                    new ItemAmmo("emerald_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterial.EMERALD, () -> GRPGItems.CROSSBOW),
                    new ItemAmmo("amethyst_ammo_9mm", AmmoType._9MM, AmmoMaterial.AMETHYST, () -> GRPGItems.PISTOL),
                    new ItemAmmo("amethyst_ammo_45acp", AmmoType._45ACP, AmmoMaterial.AMETHYST, () -> GRPGItems.SMG),
                    new ItemAmmo("amethyst_ammo_556mm", AmmoType._556MM, AmmoMaterial.AMETHYST, () -> GRPGItems.ASSAULT_RIFLE),
                    new ItemAmmo("amethyst_ammo_762mm", AmmoType._762MM, AmmoMaterial.AMETHYST, () -> GRPGItems.SNIPER_RIFLE),
                    new ItemAmmo("amethyst_ammo_12g", AmmoType._12G, AmmoMaterial.AMETHYST, () -> GRPGItems.SHOTGUN),
                    new ItemAmmo("amethyst_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterial.AMETHYST, () -> GRPGItems.CROSSBOW),
                    new PistolItem("pistol"),
                    new SMGItem("smg"),
                    new ARItem("assault_rifle"),
                    new SRItem("sniper_rifle"),
                    new SGItem("shotgun"),
                    new CrossbowItem("crossbow"),
                    new GRPGItem("small_bullet_casing"),
                    new GRPGItem("large_bullet_casing"),
                    new GRPGItem("shotgun_shell"),
                    new GRPGItem("barrel"),
                    new GRPGItem("long_barrel"),
                    new GRPGItem("iron_stock"),
                    new GRPGItem("small_iron_stock"),
                    new GRPGItem("wooden_stock"),
                    new GRPGItem("magazine"),
                    new GRPGItem("gun_parts"),
                    new GRPGItem("bolt_fletching"),
                    new ItemGrenade("grenade", 3, false),
                    new ItemGrenade("massive_grenade", 5, false),
                    new ItemGrenade("impact_grenade", 3, true),
                    new GRPGItem("iron_ore_chunk"),
                    new GRPGItem("gold_ore_chunk"),
                    new ItemSkillBook("skillpoint_book")
            );
            queue.forEach(registry::register);
            queue = null;
        }

        @SubscribeEvent
        public static void onEntityRegister(RegistryEvent.Register<EntityEntry> event) {
            event.getRegistry().registerAll(
                    makeBuilder("bullet", EntityBullet.class).tracker(256, 1, true).build(),
                    makeBuilder("sg_pellet", EntityShotgunPellet.class).tracker(128, 1, true).build(),
                    makeBuilder("crossbow_bolt", EntityCrossbowBolt.class).tracker(256, 1, true).build(),
                    makeBuilder("airdrop", EntityAirdrop.class).tracker(256, 1, true).build(),
                    makeBuilder("explosive_skeleton", EntityExplosiveSkeleton.class).tracker(80, 3, true).egg(0xB46F67, 0x494949).spawn(EnumCreatureType.MONSTER, 15, 1, 3, ForgeRegistries.BIOMES).build(),
                    makeBuilder("explosive_arrow", EntityExplosiveArrow.class).tracker(64, 20, true).build(),
                    makeBuilder("zombie_gunner", EntityZombieGunner.class).tracker(80, 3, true).egg(0x00aa00, 0xdbdb00).spawn(EnumCreatureType.MONSTER, 35, 2, 5, ForgeRegistries.BIOMES).build(),
                    makeBuilder("bloodmoon_golem", EntityBloodmoonGolem.class).tracker(80, 3, true).egg(0x444444, 0x990000).build(),
                    makeBuilder("grenade", EntityGrenade.class).tracker(64, 1, true).build(),
                    makeBuilder("flare", EntityFlare.class).tracker(256, 1, true).build()
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
                    sound("sg_reload_short"),
                    sound("p92"),
                    sound("mp5"),
                    sound("slr"),
                    sound("m24"),
                    sound("win94"),
                    sound("kar98k_reload_clip"),
                    sound("crossbow_shoot"),
                    sound("crossbow_reload"),
                    sound("crossbow_reload_fast"),
                    sound("flare_shoot"),
                    sound("second_chance_use")
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

        protected static <V extends IForgeRegistryEntry<V>> RegistryBuilder<V> createRegistry(ResourceLocation location, Class<V> type) {
            return new RegistryBuilder<V>().setName(location).setType(type).setMaxID(Integer.MAX_VALUE - 1);
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
}
