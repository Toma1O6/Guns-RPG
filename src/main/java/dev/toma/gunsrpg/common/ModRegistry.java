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
import dev.toma.gunsrpg.common.skills.NoOperationSkill;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skilltree.Ability;
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
        public static final SoundEvent P92 = null;
        public static final SoundEvent MP5 = null;
        public static final SoundEvent SLR = null;
        public static final SoundEvent M24 = null;
        public static final SoundEvent WIN94 = null;
        public static final SoundEvent KAR98K_RELOAD_CLIP = null;
        public static final SoundEvent CROSSBOW_SHOOT = null;
        public static final SoundEvent CROSSBOW_RELOAD = null;
        public static final SoundEvent CROSSBOW_RELOAD_FAST = null;
    }

    @GameRegistry.ObjectHolder(GunsRPG.MODID)
    public static final class Skills {
        public static final SkillType<?> GUNPOWDER_NOVICE = null;
        public static final SkillType<?> GUNPOWDER_EXPERT = null;
        public static final SkillType<?> GUNPOWDER_MASTER = null;
        public static final SkillType<?> GUN_PARTS_SMITH = null;
        public static final SkillType<?> PISTOL_ASSEMBLY = null;
        public static final SkillType<?> SMG_ASSEMBLY = null;
        public static final SkillType<?> CROSSBOW_ASSEMBLY = null;
        public static final SkillType<?> SHOTGUN_ASSEMBLY = null;
        public static final SkillType<?> ASSAULT_RIFLE_ASSEMBLY = null;
        public static final SkillType<?> SNIPER_RIFLE_ASSEMBLY = null;
        public static final SkillType<?> WOODEN_AMMO_SMITH = null;
        public static final SkillType<?> STONE_AMMO_SMITH = null;
        public static final SkillType<?> IRON_AMMO_SMITH = null;
        public static final SkillType<?> GOLD_AMMO_SMITH = null;
        public static final SkillType<?> DIAMOND_AMMO_SMITH = null;
        public static final SkillType<?> EMERALD_AMMO_SMITH = null;
        public static final SkillType<?> AMETHYST_AMMO_SMITH = null;
        public static final SkillType<?> AMMO_SMITHING_MASTERY = null;
        public static final SkillType<?> GRENADES = null; // 5s fuse
        public static final SkillType<?> MASSIVE_GRENADES = null; // +2 power
        public static final SkillType<?> IMPACT_GRENADES = null;
        public static final SkillType<?> MEDIC = null;
        public static final SkillType<?> DOCTOR = null;
        public static final SkillType<?> POISON_RESISTANCE_I = null;
        public static final SkillType<?> POISON_RESISTANCE_II = null;
        public static final SkillType<?> POISON_RESISTANCE_III = null;
        public static final SkillType<?> INFECTION_RESISTANCE_I = null;
        public static final SkillType<?> INFECTION_RESISTANCE_II = null;
        public static final SkillType<?> INFECTION_RESISTANCE_III = null;
        public static final SkillType<?> BROKEN_BONE_RESISTANCE_I = null;
        public static final SkillType<?> BROKEN_BONE_RESISTANCE_II = null;
        public static final SkillType<?> BROKEN_BONE_RESISTANCE_III = null;
        public static final SkillType<?> BLEEDING_RESISTANCE_I = null;
        public static final SkillType<?> BLEEDING_RESISTANCE_II = null;
        public static final SkillType<?> BLEEDING_RESISTANCE_III = null;
        public static final SkillType<?> SHARP_AXE_I = null;
        public static final SkillType<?> SHARP_AXE_II = null;
        public static final SkillType<?> SHARP_AXE_III = null;
        public static final SkillType<?> LUMBERJACK_I = null;
        public static final SkillType<?> LUMBERJACK_II = null;
        public static final SkillType<?> LUMBERJACK_III = null;
        public static final SkillType<?> HEAVY_PICKAXE_I = null;
        public static final SkillType<?> HEAVY_PICKAXE_II = null;
        public static final SkillType<?> HEAVY_PICKAXE_III = null;
        public static final SkillType<?> MOTHER_LOAD_I = null;
        public static final SkillType<?> MOTHER_LOAD_II = null;
        public static final SkillType<?> MOTHER_LOAD_III = null;
        public static final SkillType<?> BLACKSMITH = null;
        public static final SkillType<?> MINERALOGIST = null;
        public static final SkillType<?> ACROBATICS_I = null;
        public static final SkillType<?> ACROBATICS_II = null;
        public static final SkillType<?> ACROBATICS_III = null;
        public static final SkillType<?> STRONG_MUSCLES_I = null;
        public static final SkillType<?> STRONG_MUSCLES_II = null;
        public static final SkillType<?> STRONG_MUSCLES_III = null;
        public static final SkillType<?> AGILITY_I = null;
        public static final SkillType<?> AGILITY_II = null;
        public static final SkillType<?> AGILITY_III = null;
        public static final SkillType<?> WELL_FED_I = null;
        public static final SkillType<?> WELL_FED_II = null;
        public static final SkillType<?> WELL_FED_III = null;
        public static final SkillType<?> SECOND_CHANCE_I = null;
        public static final SkillType<?> SECOND_CHANCE_II = null;
        public static final SkillType<?> SECOND_CHANCE_III = null;
        public static final SkillType<?> ADRENALINE_RUSH_I = null;
        public static final SkillType<?> ADRENALINE_RUSH_II = null;
        public static final SkillType<?> ADRENALINE_RUSH_III = null;
        public static final SkillType<?> GOD_HELP_US = null;
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

        @SubscribeEvent
        public static void onSkillRegister(RegistryEvent.Register<SkillType<?>> event) {
            event.getRegistry().registerAll(
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("gunpowder_novice").requiredLevel(0).price(1).childs(() -> ModUtils.newList(Skills.GUNPOWDER_EXPERT)).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("gunpowder_expert").requiredLevel(15).price(2).childs(() -> ModUtils.newList(Skills.GUNPOWDER_MASTER)).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("gunpowder_master").requiredLevel(30).price(3).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("gun_parts_smith").requiredLevel(5).price(1).childs(() -> ModUtils.newList(Skills.PISTOL_ASSEMBLY, Skills.SMG_ASSEMBLY, Skills.CROSSBOW_ASSEMBLY, Skills.SHOTGUN_ASSEMBLY, Skills.ASSAULT_RIFLE_ASSEMBLY, Skills.SNIPER_RIFLE_ASSEMBLY)).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("pistol_assembly").requiredLevel(5).price(2).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("smg_assembly").requiredLevel(10).price(2).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("crossbow_assembly").requiredLevel(15).price(3).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("shotgun_assembly").requiredLevel(20).price(3).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("assault_rifle_assembly").requiredLevel(25).price(4).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("sniper_rifle_assembly").requiredLevel(35).price(4).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("wooden_ammo_smith").requiredLevel(0).price(1).childs(() -> ModUtils.newList(Skills.STONE_AMMO_SMITH)).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("stone_ammo_smith").requiredLevel(5).price(1).childs(() -> ModUtils.newList(Skills.IRON_AMMO_SMITH)).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("iron_ammo_smith").requiredLevel(10).price(2).childs(() -> ModUtils.newList(Skills.GOLD_AMMO_SMITH)).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("gold_ammo_smith").requiredLevel(15).price(2).childs(() -> ModUtils.newList(Skills.DIAMOND_AMMO_SMITH)).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("diamond_ammo_smith").requiredLevel(20).price(3).childs(() -> ModUtils.newList(Skills.EMERALD_AMMO_SMITH)).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("emerald_ammo_smith").requiredLevel(30).price(3).childs(() -> ModUtils.newList(Skills.AMETHYST_AMMO_SMITH)).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("amethyst_ammo_smith").requiredLevel(45).price(5).childs(() -> ModUtils.newList(Skills.AMMO_SMITHING_MASTERY)).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("ammo_smithing_mastery").requiredLevel(60).price(10).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("grenades").requiredLevel(15).price(3).childs(() -> ModUtils.newList(Skills.MASSIVE_GRENADES, Skills.IMPACT_GRENADES)).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("massive_grenades").requiredLevel(15).price(3).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setGunCategory().setRegistryName("impact_grenades").requiredLevel(15).price(3).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setDebuffCategory().setRegistryName("medic").requiredLevel(10).price(3).childs(() -> ModUtils.newList(Skills.DOCTOR)).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setDebuffCategory().setRegistryName("doctor").requiredLevel(25).price(3).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setDebuffCategory().setRegistryName("poison_resistance_i").requiredLevel(0).price(1).childAndOverride(() -> Skills.POISON_RESISTANCE_II).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setDebuffCategory().setRegistryName("poison_resistance_ii").requiredLevel(15).price(2).childAndOverride(() -> Skills.POISON_RESISTANCE_III).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setDebuffCategory().setRegistryName("poison_resistance_iii").requiredLevel(30).price(3).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setDebuffCategory().setRegistryName("infection_resistance_i").requiredLevel(0).price(1).childAndOverride(() -> Skills.INFECTION_RESISTANCE_II).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setDebuffCategory().setRegistryName("infection_resistance_ii").requiredLevel(15).price(2).childAndOverride(() -> Skills.INFECTION_RESISTANCE_III).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setDebuffCategory().setRegistryName("infection_resistance_iii").requiredLevel(30).price(3).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setDebuffCategory().setRegistryName("broken_bone_resistance_i").requiredLevel(0).price(1).childAndOverride(() -> Skills.BROKEN_BONE_RESISTANCE_II).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setDebuffCategory().setRegistryName("broken_bone_resistance_ii").requiredLevel(15).price(2).childAndOverride(() -> Skills.BROKEN_BONE_RESISTANCE_III).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setDebuffCategory().setRegistryName("broken_bone_resistance_iii").requiredLevel(30).price(3).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setDebuffCategory().setRegistryName("bleeding_resistance_i").requiredLevel(0).price(1).childAndOverride(() -> Skills.BLEEDING_RESISTANCE_II).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setDebuffCategory().setRegistryName("bleeding_resistance_ii").requiredLevel(15).price(2).childAndOverride(() -> Skills.BLEEDING_RESISTANCE_III).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setDebuffCategory().setRegistryName("bleeding_resistance_iii").requiredLevel(30).price(3).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setMiningCategory().setRegistryName("sharp_axe_i").requiredLevel(0).price(1).childAndOverride(() -> Skills.SHARP_AXE_II).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setMiningCategory().setRegistryName("sharp_axe_ii").requiredLevel(5).price(2).childAndOverride(() -> Skills.SHARP_AXE_III).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setMiningCategory().setRegistryName("sharp_axe_iii").requiredLevel(15).price(3).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setMiningCategory().setRegistryName("lumberjack_i").requiredLevel(0).price(2).childAndOverride(() -> Skills.LUMBERJACK_II).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setMiningCategory().setRegistryName("lumberjack_ii").requiredLevel(10).price(3).childAndOverride(() -> Skills.LUMBERJACK_III).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setMiningCategory().setRegistryName("lumberjack_iii").requiredLevel(25).price(4).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setMiningCategory().setRegistryName("heavy_pickaxe_i").requiredLevel(0).price(1).childAndOverride(() -> Skills.HEAVY_PICKAXE_II).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setMiningCategory().setRegistryName("heavy_pickaxe_ii").requiredLevel(5).price(2).childAndOverride(() -> Skills.HEAVY_PICKAXE_III).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setMiningCategory().setRegistryName("heavy_pickaxe_iii").requiredLevel(15).price(3).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setMiningCategory().setRegistryName("mother_load_i").requiredLevel(0).price(2).childAndOverride(() -> Skills.MOTHER_LOAD_II).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setMiningCategory().setRegistryName("mother_load_ii").requiredLevel(15).price(3).childAndOverride(() -> Skills.MOTHER_LOAD_III).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setMiningCategory().setRegistryName("mother_load_iii").requiredLevel(30).price(4).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setMiningCategory().setRegistryName("blacksmith").requiredLevel(45).price(5).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setMiningCategory().setRegistryName("mineralogist").requiredLevel(50).price(6).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setSurvivalCategory().setRegistryName("acrobatics_i").requiredLevel(5).price(2).childAndOverride(() -> Skills.ACROBATICS_II).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setSurvivalCategory().setRegistryName("acrobatics_ii").requiredLevel(15).price(3).childAndOverride(() -> Skills.ACROBATICS_III).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setSurvivalCategory().setRegistryName("acrobatics_iii").requiredLevel(25).price(4).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setSurvivalCategory().setRegistryName("strong_muscles_i").requiredLevel(10).price(2).childAndOverride(() -> Skills.STRONG_MUSCLES_II).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setSurvivalCategory().setRegistryName("strong_muscles_ii").requiredLevel(25).price(3).childAndOverride(() -> Skills.STRONG_MUSCLES_III).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setSurvivalCategory().setRegistryName("strong_muscles_iii").requiredLevel(40).price(5).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setSurvivalCategory().setRegistryName("agility_i").requiredLevel(10).price(2).childAndOverride(() -> Skills.AGILITY_II).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setSurvivalCategory().setRegistryName("agility_ii").requiredLevel(20).price(3).childAndOverride(() -> Skills.AGILITY_III).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setSurvivalCategory().setRegistryName("agility_iii").requiredLevel(35).price(4).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setSurvivalCategory().setRegistryName("well_fed_i").requiredLevel(20).price(2).childAndOverride(() -> Skills.WELL_FED_II).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setSurvivalCategory().setRegistryName("well_fed_ii").requiredLevel(35).price(3).childAndOverride(() -> Skills.WELL_FED_III).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setSurvivalCategory().setRegistryName("well_fed_iii").requiredLevel(55).price(5).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setSurvivalCategory().setRegistryName("second_chance_i").requiredLevel(50).price(7).childAndOverride(() -> Skills.SECOND_CHANCE_II).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setSurvivalCategory().setRegistryName("second_chance_ii").requiredLevel(75).price(9).childAndOverride(() -> Skills.SECOND_CHANCE_III).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setSurvivalCategory().setRegistryName("second_chance_iii").requiredLevel(90).price(10).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setSurvivalCategory().setRegistryName("adrenaline_rush_i").requiredLevel(15).price(2).childAndOverride(() -> Skills.ADRENALINE_RUSH_II).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setSurvivalCategory().setRegistryName("adrenaline_rush_ii").requiredLevel(25).price(3).childAndOverride(() -> Skills.ADRENALINE_RUSH_III).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setSurvivalCategory().setRegistryName("adrenaline_rush_iii").requiredLevel(40).price(5).build(),
                    SkillType.Builder.newBuilder(NoOperationSkill::new).setSurvivalCategory().setRegistryName("god_help_us").requiredLevel(60).price(8).build()
            );
        }

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
                    new ItemAmmo("wooden_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterial.WOOD, Ability.CROSSBOW_WOOD_AMMO, () -> GRPGItems.CROSSBOW),
                    new ItemAmmo("stone_ammo_9mm", AmmoType._9MM, AmmoMaterial.STONE, Ability.PISTOL_STONE_AMMO, () -> GRPGItems.PISTOL),
                    new ItemAmmo("stone_ammo_45acp", AmmoType._45ACP, AmmoMaterial.STONE, Ability.SMG_STONE_AMMO, () -> GRPGItems.SMG),
                    new ItemAmmo("stone_ammo_556mm", AmmoType._556MM, AmmoMaterial.STONE, Ability.AR_STONE_AMMO, () -> GRPGItems.ASSAULT_RIFLE),
                    new ItemAmmo("stone_ammo_762mm", AmmoType._762MM, AmmoMaterial.STONE, Ability.SR_STONE_AMMO, () -> GRPGItems.SNIPER_RIFLE),
                    new ItemAmmo("stone_ammo_12g", AmmoType._12G, AmmoMaterial.STONE, Ability.SG_STONE_AMMO, () -> GRPGItems.SHOTGUN),
                    new ItemAmmo("stone_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterial.STONE, Ability.CROSSBOW_STONE_AMMO, () -> GRPGItems.CROSSBOW),
                    new ItemAmmo("iron_ammo_9mm", AmmoType._9MM, AmmoMaterial.IRON, Ability.PISTOL_IRON_AMMO, () -> GRPGItems.PISTOL),
                    new ItemAmmo("iron_ammo_45acp", AmmoType._45ACP, AmmoMaterial.IRON, Ability.SMG_IRON_AMMO, () -> GRPGItems.SMG),
                    new ItemAmmo("iron_ammo_556mm", AmmoType._556MM, AmmoMaterial.IRON, Ability.AR_IRON_AMMO, () -> GRPGItems.ASSAULT_RIFLE),
                    new ItemAmmo("iron_ammo_762mm", AmmoType._762MM, AmmoMaterial.IRON, Ability.SR_IRON_AMMO, () -> GRPGItems.SNIPER_RIFLE),
                    new ItemAmmo("iron_ammo_12g", AmmoType._12G, AmmoMaterial.IRON, Ability.SG_IRON_AMMO, () -> GRPGItems.SHOTGUN),
                    new ItemAmmo("iron_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterial.IRON, Ability.CROSSBOW_IRON_AMMO, () -> GRPGItems.CROSSBOW),
                    new ItemAmmo("gold_ammo_9mm", AmmoType._9MM, AmmoMaterial.GOLD, Ability.PISTOL_GOLD_AMMO, () -> GRPGItems.PISTOL),
                    new ItemAmmo("gold_ammo_45acp", AmmoType._45ACP, AmmoMaterial.GOLD, Ability.SMG_GOLD_AMMO, () -> GRPGItems.SMG),
                    new ItemAmmo("gold_ammo_556mm", AmmoType._556MM, AmmoMaterial.GOLD, Ability.AR_GOLD_AMMO, () -> GRPGItems.ASSAULT_RIFLE),
                    new ItemAmmo("gold_ammo_762mm", AmmoType._762MM, AmmoMaterial.GOLD, Ability.SR_GOLD_AMMO, () -> GRPGItems.SNIPER_RIFLE),
                    new ItemAmmo("gold_ammo_12g", AmmoType._12G, AmmoMaterial.GOLD, Ability.SG_GOLD_AMMO, () -> GRPGItems.SHOTGUN),
                    new ItemAmmo("gold_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterial.GOLD, Ability.CROSSBOW_GOLD_AMMO, () -> GRPGItems.CROSSBOW),
                    new ItemAmmo("diamond_ammo_9mm", AmmoType._9MM, AmmoMaterial.DIAMOND, Ability.PISTOL_DIAMOND_AMMO, () -> GRPGItems.PISTOL),
                    new ItemAmmo("diamond_ammo_45acp", AmmoType._45ACP, AmmoMaterial.DIAMOND, Ability.SMG_DIAMOND_AMMO, () -> GRPGItems.SMG),
                    new ItemAmmo("diamond_ammo_556mm", AmmoType._556MM, AmmoMaterial.DIAMOND, Ability.AR_DIAMOND_AMMO, () -> GRPGItems.ASSAULT_RIFLE),
                    new ItemAmmo("diamond_ammo_762mm", AmmoType._762MM, AmmoMaterial.DIAMOND, Ability.SR_DIAMOND_AMMO, () -> GRPGItems.SNIPER_RIFLE),
                    new ItemAmmo("diamond_ammo_12g", AmmoType._12G, AmmoMaterial.DIAMOND, Ability.SG_DIAMOND_AMMO, () -> GRPGItems.SHOTGUN),
                    new ItemAmmo("diamond_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterial.DIAMOND, Ability.CROSSBOW_DIAMOND_AMMO, () -> GRPGItems.CROSSBOW),
                    new ItemAmmo("emerald_ammo_9mm", AmmoType._9MM, AmmoMaterial.EMERALD, Ability.PISTOL_EMERALD_AMMO, () -> GRPGItems.PISTOL),
                    new ItemAmmo("emerald_ammo_45acp", AmmoType._45ACP, AmmoMaterial.EMERALD, Ability.SMG_EMERALD_AMMO, () -> GRPGItems.SMG),
                    new ItemAmmo("emerald_ammo_556mm", AmmoType._556MM, AmmoMaterial.EMERALD, Ability.AR_EMERALD_AMMO, () -> GRPGItems.ASSAULT_RIFLE),
                    new ItemAmmo("emerald_ammo_762mm", AmmoType._762MM, AmmoMaterial.EMERALD, Ability.SR_EMERALD_AMMO, () -> GRPGItems.SNIPER_RIFLE),
                    new ItemAmmo("emerald_ammo_12g", AmmoType._12G, AmmoMaterial.EMERALD, Ability.SG_EMERALD_AMMO, () -> GRPGItems.SHOTGUN),
                    new ItemAmmo("emerald_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterial.EMERALD, Ability.CROSSBOW_EMERALD_AMMO, () -> GRPGItems.CROSSBOW),
                    new ItemAmmo("amethyst_ammo_9mm", AmmoType._9MM, AmmoMaterial.AMETHYST, Ability.PISTOL_AMETHYST_AMMO, () -> GRPGItems.PISTOL),
                    new ItemAmmo("amethyst_ammo_45acp", AmmoType._45ACP, AmmoMaterial.AMETHYST, Ability.SMG_AMETHYST_AMMO, () -> GRPGItems.SMG),
                    new ItemAmmo("amethyst_ammo_556mm", AmmoType._556MM, AmmoMaterial.AMETHYST, Ability.AR_AMETHYST_AMMO, () -> GRPGItems.ASSAULT_RIFLE),
                    new ItemAmmo("amethyst_ammo_762mm", AmmoType._762MM, AmmoMaterial.AMETHYST, Ability.SR_AMETHYST_AMMO, () -> GRPGItems.SNIPER_RIFLE),
                    new ItemAmmo("amethyst_ammo_12g", AmmoType._12G, AmmoMaterial.AMETHYST, Ability.SG_AMETHYST_AMMO, () -> GRPGItems.SHOTGUN),
                    new ItemAmmo("amethyst_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterial.AMETHYST, Ability.CROSSBOW_AMETHYST_AMMO, () -> GRPGItems.CROSSBOW),
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
                    new GRPGItem("bolt_fletching")
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
                    sound("sg_reload_short"),
                    sound("p92"),
                    sound("mp5"),
                    sound("slr"),
                    sound("m24"),
                    sound("win94"),
                    sound("kar98k_reload_clip"),
                    sound("crossbow_shoot"),
                    sound("crossbow_reload"),
                    sound("crossbow_reload_fast")
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
