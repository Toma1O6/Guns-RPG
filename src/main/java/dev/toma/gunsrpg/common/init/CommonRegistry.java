package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.common.AnimationPaths;
import dev.toma.gunsrpg.common.block.*;
import dev.toma.gunsrpg.common.debuffs.*;
import dev.toma.gunsrpg.common.entity.*;
import dev.toma.gunsrpg.common.item.*;
import dev.toma.gunsrpg.common.item.guns.*;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.skills.*;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.criteria.CriteriaTypes;
import dev.toma.gunsrpg.util.ModUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(modid = GunsRPG.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonRegistry {

    private static List<BlockItem> queue = new ArrayList<>();

    public static void registerItemBlock(Block block) {
        BlockItem itemBlock = new BlockItem(block, new Item.Properties().tab(ModTabs.BLOCK_TAB));
        itemBlock.setRegistryName(block.getRegistryName());
        queue.add(itemBlock);
    }

    @SubscribeEvent
    public static void createRegistries(RegistryEvent.NewRegistry event) {
        ModRegistries.SKILLS = createGenericRegistry("skill", SkillType.class);
        ModRegistries.DEBUFFS = createGenericRegistry("debuff", DebuffType.class);
    }

    @SubscribeEvent
    public static void onSkillRegister(RegistryEvent.Register<SkillType<?>> event) {
        event.getRegistry().registerAll(
                SkillType.Builder.create(type -> new CraftingSkill(type, 2, CraftingSkill::getGunpowderYield)).setGunCategory().setRegistryName("gunpowder_novice").requiredLevel(0).price(1).childs(() -> ModUtils.newList(Skills.GUNPOWDER_EXPERT)).build(),
                SkillType.Builder.create(type -> new CraftingSkill(type, 4, CraftingSkill::getGunpowderYield)).setGunCategory().setRegistryName("gunpowder_expert").requiredLevel(15).price(2).childs(() -> ModUtils.newList(Skills.GUNPOWDER_MASTER)).build(),
                SkillType.Builder.create(type -> new CraftingSkill(type, 6, CraftingSkill::getGunpowderYield)).setGunCategory().setRegistryName("gunpowder_master").requiredLevel(30).price(3).build(),
                SkillType.Builder.create(type -> new CraftingSkill(type, 4, CraftingSkill::getBonemealYield)).setGunCategory().setRegistryName("bone_grinder_i").requiredLevel(0).price(1).childs(() -> Collections.singletonList(Skills.BONE_GRINDER_II)).build(),
                SkillType.Builder.create(type -> new CraftingSkill(type, 5, CraftingSkill::getBonemealYield)).setGunCategory().setRegistryName("bone_grinder_ii").requiredLevel(15).price(2).childs(() -> Collections.singletonList(Skills.BONE_GRINDER_III)).build(),
                SkillType.Builder.create(type -> new CraftingSkill(type, 6, CraftingSkill::getBonemealYield)).setGunCategory().setRegistryName("bone_grinder_iii").requiredLevel(30).price(3).build(),
                SkillType.Builder.<CraftingSkill>create(type -> new CraftingSkill(type, 2, CraftingSkill::getBlazepowderYield)).setMiningCategory().setRegistryName("blaze_powder_i").requiredLevel(45).price(3).childs(() -> Collections.singletonList(Skills.BLAZE_POWDER_II)).build(),
                SkillType.Builder.<CraftingSkill>create(type -> new CraftingSkill(type, 3, CraftingSkill::getBlazepowderYield)).setMiningCategory().setRegistryName("blaze_powder_ii").requiredLevel(55).price(4).childs(() -> Collections.singletonList(Skills.BLAZE_POWDER_III)).build(),
                SkillType.Builder.<CraftingSkill>create(type -> new CraftingSkill(type, 4, CraftingSkill::getBlazepowderYield)).setMiningCategory().setRegistryName("blaze_powder_iii").requiredLevel(65).price(5).build(),
                SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("wooden_ammo_smith").requiredLevel(0).price(1).childs(() -> ModUtils.newList(Skills.STONE_AMMO_SMITH)).build(),
                SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("stone_ammo_smith").requiredLevel(5).price(1).childs(() -> ModUtils.newList(Skills.IRON_AMMO_SMITH)).build(),
                SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("iron_ammo_smith").requiredLevel(10).price(2).childs(() -> ModUtils.newList(Skills.GOLD_AMMO_SMITH)).build(),
                SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("gold_ammo_smith").requiredLevel(15).price(2).childs(() -> ModUtils.newList(Skills.DIAMOND_AMMO_SMITH)).build(),
                SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("diamond_ammo_smith").requiredLevel(20).price(3).childs(() -> ModUtils.newList(Skills.EMERALD_AMMO_SMITH)).build(),
                SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("emerald_ammo_smith").requiredLevel(30).price(4).childs(() -> ModUtils.newList(Skills.AMETHYST_AMMO_SMITH)).build(),
                SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("amethyst_ammo_smith").requiredLevel(45).price(6).build(),
                SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("ammo_smithing_mastery").requiredLevel(60).price(10).build(),
                SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("gun_parts_smith").requiredLevel(5).price(1).childs(() -> ModUtils.newList(Skills.M1911_ASSEMBLY, Skills.UMP45_ASSEMBLY, Skills.CROSSBOW_ASSEMBLY, Skills.S1897_ASSEMBLY, Skills.SKS_ASSEMBLY, Skills.KAR98K_ASSEMBLY)).build(),
                SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("m1911_assembly").requiredLevel(5).price(2).setCustomDisplay().renderFactory(() -> ModItems.M1911).childs(() -> ModUtils.newList(Skills.M1911_QUICKDRAW, Skills.M1911_EXTENDED, Skills.M1911_TOUGH_SPRING, Skills.M1911_CARBON_BARREL, Skills.M1911_SUPPRESSOR, Skills.M1911_HEAVY_BULLETS, Skills.M1911_DUAL_WIELD)).build(),
                SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("ump45_assembly").requiredLevel(10).price(3).setCustomDisplay().renderFactory(() -> ModItems.UMP45).childs(() -> ModUtils.newList(Skills.UMP45_QUICKDRAW, Skills.UMP45_EXTENDED, Skills.UMP45_VERTICAL_GRIP, Skills.UMP45_TOUGH_SPRING, Skills.UMP45_RED_DOT, Skills.UMP45_SUPPRESSOR, Skills.UMP45_COMMANDO)).build(),
                SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("crossbow_assembly").requiredLevel(15).price(3).setCustomDisplay().renderFactory(() -> ModItems.WOODEN_CROSSBOW).childs(() -> ModUtils.newList(Skills.CROSSBOW_QUIVER, Skills.CROSSBOW_POISONED_BOLTS, Skills.CROSSBOW_HUNTER, Skills.CROSSBOW_TOUGH_BOWSTRING, Skills.CROSSBOW_PENETRATOR, Skills.CROSSBOW_SCOPE, Skills.CROSSBOW_REPEATER)).build(),
                SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("s1897_assembly").requiredLevel(20).price(4).setCustomDisplay().renderFactory(() -> ModItems.S1897).childs(() -> ModUtils.newList(Skills.S1897_BULLET_LOOPS, Skills.S1897_EXTENDED, Skills.S1897_PUMP_IN_ACTION, Skills.S1897_CHOKE, Skills.S1897_NEVER_GIVE_UP, Skills.S1897_EXTENDED_BARREL)).build(),
                SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("sks_assembly").requiredLevel(25).price(4).setCustomDisplay().renderFactory(() -> ModItems.SKS).childs(() -> ModUtils.newList(Skills.SKS_TOUGH_SPRING, Skills.SKS_VERTICAL_GRIP, Skills.SKS_EXTENDED, Skills.SKS_RED_DOT, Skills.SKS_SUPPRESSOR, Skills.SKS_CHEEKPAD, Skills.SKS_ADAPTIVE_CHAMBERING)).build(),
                SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("kar98k_assembly").requiredLevel(35).price(5).setCustomDisplay().renderFactory(() -> ModItems.KAR98K).childs(() -> ModUtils.newList(Skills.KAR98K_SCOPE, Skills.KAR98K_CHEEKPAD, Skills.KAR98K_EXTENDED, Skills.KAR98K_SUPPRESSOR, Skills.KAR98K_FAST_HANDS, Skills.KAR98K_PENETRATOR, Skills.KAR98K_DEAD_EYE)).build(),
                SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("grenades").requiredLevel(15).price(3).childs(() -> ModUtils.newList(Skills.MASSIVE_GRENADES, Skills.IMPACT_GRENADES)).build(),
                SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("massive_grenades").requiredLevel(30).price(5).build(),
                SkillType.Builder.create(BasicSkill::new).setGunCategory().setRegistryName("impact_grenades").requiredLevel(40).price(5).build(),
                // TODO grenade launcher lvl 40, rocket launcher 60
                SkillType.Builder.create(BasicSkill::new).setResistanceCategory().setRegistryName("medic").requiredLevel(10).price(3).childs(() -> ModUtils.newList(Skills.DOCTOR)).build(),
                SkillType.Builder.create(BasicSkill::new).setResistanceCategory().setRegistryName("doctor").requiredLevel(25).price(3).build(),
                SkillType.Builder.create(BasicSkill::new).setResistanceCategory().setRegistryName("efficient_meds").requiredLevel(30).price(3).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> {
                    skills.setPoisonResistance(20);
                    skills.setPoisonChance(0.15F);
                })).setResistanceCategory().setRegistryName("poison_resistance_i").requiredLevel(5).price(2).descriptionLength(2).childAndOverride(() -> Skills.POISON_RESISTANCE_II).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> {
                    skills.setPoisonResistance(40);
                    skills.setPoisonChance(0.30F);
                })).setResistanceCategory().setRegistryName("poison_resistance_ii").requiredLevel(20).price(3).descriptionLength(2).childAndOverride(() -> Skills.POISON_RESISTANCE_III).build(),
                SkillType.Builder.create(type -> new DataChangeSkill(type, skills -> {
                    skills.setPoisonResistance(60);
                    skills.setPoisonChance(0.45F);
                })).setResistanceCategory().setRegistryName("poison_resistance_iii").requiredLevel(40).price(4).descriptionLength(2).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> {
                    skills.setInfectionResistance(20);
                    skills.setInfectionChance(0.05F);
                })).setResistanceCategory().setRegistryName("infection_resistance_i").requiredLevel(5).price(2).descriptionLength(2).childAndOverride(() -> Skills.INFECTION_RESISTANCE_II).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> {
                    skills.setInfectionResistance(40);
                    skills.setInfectionChance(0.15F);
                })).setResistanceCategory().setRegistryName("infection_resistance_ii").requiredLevel(20).price(3).descriptionLength(2).childAndOverride(() -> Skills.INFECTION_RESISTANCE_III).build(),
                SkillType.Builder.create(type -> new DataChangeSkill(type, skills -> {
                    skills.setInfectionResistance(60);
                    skills.setInfectionChance(0.30F);
                })).setResistanceCategory().setRegistryName("infection_resistance_iii").requiredLevel(40).descriptionLength(2).price(4).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> {
                    skills.setBrokenBoneResistance(20);
                    skills.setBrokenBoneChance(0.10F);
                })).setResistanceCategory().setRegistryName("fracture_resistance_i").requiredLevel(5).price(2).descriptionLength(2).childAndOverride(() -> Skills.FRACTURE_RESISTANCE_II).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> {
                    skills.setBrokenBoneResistance(40);
                    skills.setBrokenBoneChance(0.20F);
                })).setResistanceCategory().setRegistryName("fracture_resistance_ii").requiredLevel(20).price(3).descriptionLength(2).childAndOverride(() -> Skills.FRACTURE_RESISTANCE_III).build(),
                SkillType.Builder.create(type -> new DataChangeSkill(type, skills -> {
                    skills.setBrokenBoneResistance(60);
                    skills.setBrokenBoneChance(0.35F);
                })).setResistanceCategory().setRegistryName("fracture_resistance_iii").requiredLevel(40).price(4).descriptionLength(2).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> {
                    skills.setBleedResistance(20);
                    skills.setBleedChance(0.15F);
                })).setResistanceCategory().setRegistryName("bleeding_resistance_i").requiredLevel(5).price(2).descriptionLength(2).childAndOverride(() -> Skills.BLEEDING_RESISTANCE_II).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> {
                    skills.setBleedResistance(40);
                    skills.setBleedChance(0.30F);
                })).setResistanceCategory().setRegistryName("bleeding_resistance_ii").requiredLevel(20).price(3).descriptionLength(2).childAndOverride(() -> Skills.BLEEDING_RESISTANCE_III).build(),
                SkillType.Builder.create(type -> new DataChangeSkill(type, skills -> {
                    skills.setBleedResistance(60);
                    skills.setBleedChance(0.45F);
                })).setResistanceCategory().setRegistryName("bleeding_resistance_iii").descriptionLength(2).requiredLevel(40).price(4).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> {
                    skills.setAcrobaticsFallResistance(0.20F);
                    skills.setAcrobaticsExplosionResistance(0.10F);
                })).setResistanceCategory().setRegistryName("acrobatics_i").descriptionLength(3).requiredLevel(5).price(2).childAndOverride(() -> Skills.ACROBATICS_II).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> {
                    skills.setAcrobaticsFallResistance(0.45F);
                    skills.setAcrobaticsExplosionResistance(0.25F);
                })).setResistanceCategory().setRegistryName("acrobatics_ii").descriptionLength(3).requiredLevel(15).price(3).childAndOverride(() -> Skills.ACROBATICS_III).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> {
                    skills.setAcrobaticsFallResistance(0.70F);
                    skills.setAcrobaticsExplosionResistance(0.40F);
                })).setResistanceCategory().setRegistryName("acrobatics_iii").descriptionLength(3).requiredLevel(35).price(4).build(),
                SkillType.Builder.create(BasicSkill::new).setResistanceCategory().setRegistryName("pharmacist_i").requiredLevel(5).price(2).childAndOverride(() -> Skills.PHARMACIST_II).build(),
                SkillType.Builder.create(BasicSkill::new).setResistanceCategory().setRegistryName("pharmacist_ii").requiredLevel(20).price(3).childAndOverride(() -> Skills.PHARMACIST_III).build(),
                SkillType.Builder.create(BasicSkill::new).setResistanceCategory().setRegistryName("pharmacist_iii").requiredLevel(35).price(4).childAndOverride(() -> Skills.PHARMACIST_IV).build(),
                SkillType.Builder.create(BasicSkill::new).setResistanceCategory().setRegistryName("pharmacist_iv").requiredLevel(45).price(5).childAndOverride(() -> Skills.PHARMACIST_V).build(),
                SkillType.Builder.create(BasicSkill::new).setResistanceCategory().setRegistryName("pharmacist_v").requiredLevel(65).price(7).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setAxeMiningSpeed(0.20F))).setMiningCategory().setRegistryName("sharp_axe_i").requiredLevel(0).price(1).childAndOverride(() -> Skills.SHARP_AXE_II).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setAxeMiningSpeed(0.40F))).setMiningCategory().setRegistryName("sharp_axe_ii").requiredLevel(10).price(2).childAndOverride(() -> Skills.SHARP_AXE_III).build(),
                SkillType.Builder.create(type -> new DataChangeSkill(type, skills -> skills.setAxeMiningSpeed(0.60F))).setMiningCategory().setRegistryName("sharp_axe_iii").requiredLevel(20).price(3).childs(() -> Collections.singletonList(Skills.SHARP_AXE_IV)).build(),
                SkillType.Builder.create(type -> new DataChangeSkill(type, skills -> skills.setAxeMiningSpeed(0.80F))).setMiningCategory().setRegistryName("sharp_axe_iv").requiredLevel(35).price(4).childs(() -> Collections.singletonList(Skills.SHARP_AXE_V)).build(),
                SkillType.Builder.create(type -> new DataChangeSkill(type, skills -> skills.setAxeMiningSpeed(1.00F))).setMiningCategory().setRegistryName("sharp_axe_v").requiredLevel(50).price(5).build(),
                SkillType.Builder.<LumberjackSkill>create(type -> new LumberjackSkill(type, 1)).setMiningCategory().setRegistryName("lumberjack_i").descriptionLength(3).requiredLevel(5).price(2).childAndOverride(() -> Skills.LUMBERJACK_II).build(),
                SkillType.Builder.<LumberjackSkill>create(type -> new LumberjackSkill(type, 2)).setMiningCategory().setRegistryName("lumberjack_ii").descriptionLength(3).requiredLevel(10).price(3).childAndOverride(() -> Skills.LUMBERJACK_III).build(),
                SkillType.Builder.<LumberjackSkill>create(type -> new LumberjackSkill(type, 3)).setMiningCategory().setRegistryName("lumberjack_iii").descriptionLength(3).requiredLevel(25).price(4).childAndOverride(() -> Skills.LUMBERJACK_IV).build(),
                SkillType.Builder.<LumberjackSkill>create(type -> new LumberjackSkill(type, 4)).setMiningCategory().setRegistryName("lumberjack_iv").descriptionLength(3).requiredLevel(40).price(5).childAndOverride(() -> Skills.LUMBERJACK_V).build(),
                SkillType.Builder.<LumberjackSkill>create(type -> new LumberjackSkill(type, 5)).setMiningCategory().setRegistryName("lumberjack_v").descriptionLength(3).requiredLevel(55).price(6).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setShovelMiningSpeed(0.15F))).setMiningCategory().setRegistryName("grave_digger_i").price(1).childAndOverride(() -> Skills.GRAVE_DIGGER_II).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setShovelMiningSpeed(0.30F))).setMiningCategory().setRegistryName("grave_digger_ii").requiredLevel(10).price(2).childAndOverride(() -> Skills.GRAVE_DIGGER_III).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setShovelMiningSpeed(0.45F))).setMiningCategory().setRegistryName("grave_digger_iii").requiredLevel(15).price(3).childAndOverride(() -> Skills.GRAVE_DIGGER_IV).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setShovelMiningSpeed(0.60F))).setMiningCategory().setRegistryName("grave_digger_iv").requiredLevel(25).price(3).childAndOverride(() -> Skills.GRAVE_DIGGER_V).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setShovelMiningSpeed(0.80F))).setMiningCategory().setRegistryName("grave_digger_v").requiredLevel(40).price(4).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setPickaxeMiningSpeed(0.20F))).setMiningCategory().setRegistryName("heavy_pickaxe_i").requiredLevel(0).price(1).childAndOverride(() -> Skills.HEAVY_PICKAXE_II).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setPickaxeMiningSpeed(0.40F))).setMiningCategory().setRegistryName("heavy_pickaxe_ii").requiredLevel(10).price(2).childAndOverride(() -> Skills.HEAVY_PICKAXE_III).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setPickaxeMiningSpeed(0.60F))).setMiningCategory().setRegistryName("heavy_pickaxe_iii").requiredLevel(20).price(3).childAndOverride(() -> Skills.HEAVY_PICKAXE_IV).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setPickaxeMiningSpeed(0.80F))).setMiningCategory().setRegistryName("heavy_pickaxe_iv").requiredLevel(35).price(4).childAndOverride(() -> Skills.HEAVY_PICKAXE_V).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setPickaxeMiningSpeed(1.00F))).setMiningCategory().setRegistryName("heavy_pickaxe_v").requiredLevel(50).price(5).build(),
                SkillType.Builder.<MotherlodeSkill>create(type -> new MotherlodeSkill(type, 1)).setMiningCategory().setRegistryName("mother_lode_i").requiredLevel(5).price(2).childAndOverride(() -> Skills.MOTHER_LODE_II).build(),
                SkillType.Builder.<MotherlodeSkill>create(type -> new MotherlodeSkill(type, 2)).setMiningCategory().setRegistryName("mother_lode_ii").requiredLevel(15).price(3).childAndOverride(() -> Skills.MOTHER_LODE_III).build(),
                SkillType.Builder.<MotherlodeSkill>create(type -> new MotherlodeSkill(type, 3)).setMiningCategory().setRegistryName("mother_lode_iii").requiredLevel(30).price(4).childAndOverride(() -> Skills.MOTHER_LODE_IV).build(),
                SkillType.Builder.<MotherlodeSkill>create(type -> new MotherlodeSkill(type, 4)).setMiningCategory().setRegistryName("mother_lode_iv").descriptionLength(2).requiredLevel(45).price(6).childAndOverride(() -> Skills.MOTHER_LODE_V).build(),
                SkillType.Builder.<MotherlodeSkill>create(type -> new MotherlodeSkill(type, 5)).setMiningCategory().setRegistryName("mother_lode_v").descriptionLength(2).requiredLevel(65).price(7).build(),
                SkillType.Builder.create(BasicSkill::new).setMiningCategory().setRegistryName("hammer_i").descriptionLength(4).requiredLevel(15).price(2).childs(() -> Collections.singletonList(Skills.HAMMER_II)).build(),
                SkillType.Builder.create(BasicSkill::new).setMiningCategory().setRegistryName("hammer_ii").descriptionLength(4).requiredLevel(30).price(3).childs(() -> Collections.singletonList(Skills.HAMMER_III)).build(),
                SkillType.Builder.create(BasicSkill::new).setMiningCategory().setRegistryName("hammer_iii").descriptionLength(4).requiredLevel(45).price(4).build(),
                SkillType.Builder.create(BasicSkill::new).setMiningCategory().setRegistryName("blacksmith").requiredLevel(40).price(5).childs(() -> Collections.singletonList(Skills.BLAZE_POWDER_I)).build(),
                SkillType.Builder.create(BasicSkill::new).setMiningCategory().setRegistryName("mineralogist").requiredLevel(50).price(6).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setExtraDamage(1))).setSurvivalCategory().setRegistryName("strong_muscles_i").requiredLevel(10).price(2).childAndOverride(() -> Skills.STRONG_MUSCLES_II).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setExtraDamage(2))).setSurvivalCategory().setRegistryName("strong_muscles_ii").requiredLevel(25).price(3).childAndOverride(() -> Skills.STRONG_MUSCLES_III).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setExtraDamage(4))).setSurvivalCategory().setRegistryName("strong_muscles_iii").requiredLevel(40).price(5).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setAgilitySpeed(0.005F))).setSurvivalCategory().setRegistryName("agility_i").requiredLevel(10).price(2).childAndOverride(() -> Skills.AGILITY_II).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setAgilitySpeed(0.010F))).setSurvivalCategory().setRegistryName("agility_ii").requiredLevel(20).price(3).childAndOverride(() -> Skills.AGILITY_III).build(),
                SkillType.Builder.<DataChangeSkill>create(type -> new DataChangeSkill(type, skills -> skills.setAgilitySpeed(0.020F))).setSurvivalCategory().setRegistryName("agility_iii").requiredLevel(35).price(4).build(),
                SkillType.Builder.<AdrenalineRushSkill>create(type -> new AdrenalineRushSkill(type, 0, 0.05f)).setSurvivalCategory().setRegistryName("adrenaline_rush_i").descriptionLength(3).requiredLevel(15).price(2).childAndOverride(() -> Skills.ADRENALINE_RUSH_II).build(),
                SkillType.Builder.<AdrenalineRushSkill>create(type -> new AdrenalineRushSkill(type, 1, 0.1f)).setSurvivalCategory().setRegistryName("adrenaline_rush_ii").descriptionLength(3).requiredLevel(25).price(3).childAndOverride(() -> Skills.ADRENALINE_RUSH_III).build(),
                SkillType.Builder.<AdrenalineRushSkill>create(type -> new AdrenalineRushSkill(type, 2, 0.2f)).setSurvivalCategory().setRegistryName("adrenaline_rush_iii").descriptionLength(3).requiredLevel(40).price(5).build(),
                SkillType.Builder.<WellFedSkill>create(type -> new WellFedSkill(type, 1, 0.40F)).setSurvivalCategory().setRegistryName("well_fed_i").descriptionLength(2).requiredLevel(20).price(2).childAndOverride(() -> Skills.WELL_FED_II).build(),
                SkillType.Builder.<WellFedSkill>create(type -> new WellFedSkill(type, 2, 0.55F)).setSurvivalCategory().setRegistryName("well_fed_ii").descriptionLength(2).requiredLevel(35).price(3).childAndOverride(() -> Skills.WELL_FED_III).build(),
                SkillType.Builder.<WellFedSkill>create(type -> new WellFedSkill(type, 3, 0.70F)).setSurvivalCategory().setRegistryName("well_fed_iii").descriptionLength(2).requiredLevel(55).price(5).build(),
                SkillType.Builder.create(BasicSkill::new).setSurvivalCategory().setRegistryName("local_chef").requiredLevel(20).price(2).childAndOverride(() -> Skills.MASTER_CHEF).build(),
                SkillType.Builder.create(BasicSkill::new).setSurvivalCategory().setRegistryName("master_chef").requiredLevel(50).price(5).build(),
                SkillType.Builder.<SecondChanceSkill>create(type -> new SecondChanceSkill(type, 18000, 10, () -> new EffectInstance(Effects.REGENERATION, 200, 0))).setSurvivalCategory().setRegistryName("second_chance_i").descriptionLength(3).requiredLevel(50).price(7).childAndOverride(() -> Skills.SECOND_CHANCE_II).build(),
                SkillType.Builder.<SecondChanceSkill>create(type -> new SecondChanceSkill(type, 14400, 15, () -> new EffectInstance(Effects.REGENERATION, 200, 1))).setSurvivalCategory().setRegistryName("second_chance_ii").descriptionLength(3).requiredLevel(75).price(9).childAndOverride(() -> Skills.SECOND_CHANCE_III).build(),
                SkillType.Builder.<SecondChanceSkill>create(type -> new SecondChanceSkill(type, 10800, 20, () -> new EffectInstance(Effects.REGENERATION, 200, 2))).setSurvivalCategory().setRegistryName("second_chance_iii").descriptionLength(3).requiredLevel(90).price(10).build(),
                SkillType.Builder.create(GodHelpUsSkill::new).setSurvivalCategory().setRegistryName("god_help_us").descriptionLength(2).requiredLevel(60).price(8).build(),
                SkillType.Builder.create(type -> new DataChangeSkill(type, skills -> skills.setInstantKillChance(0.01F))).setSurvivalCategory().setRegistryName("skull_crusher_i").requiredLevel(20).price(2).childs(() -> Collections.singletonList(Skills.SKULL_CRUSHER_II)).build(),
                SkillType.Builder.create(type -> new DataChangeSkill(type, skills -> skills.setInstantKillChance(0.03F))).setSurvivalCategory().setRegistryName("skull_crusher_ii").requiredLevel(35).price(4).childs(() -> Collections.singletonList(Skills.SKULL_CRUSHER_III)).build(),
                SkillType.Builder.create(type -> new DataChangeSkill(type, skills -> skills.setInstantKillChance(0.05F))).setSurvivalCategory().setRegistryName("skull_crusher_iii").requiredLevel(60).price(6).build(),
                SkillType.Builder.create(LightHunterSkill::new).setSurvivalCategory().setRegistryName("light_hunter").descriptionLength(4).requiredLevel(35).price(7).build(),
                SkillType.Builder.<LikeACatSkill>create(type -> new LikeACatSkill(type, 6000, 2400)).setSurvivalCategory().setRegistryName("like_a_cat_i").descriptionLength(2).requiredLevel(15).price(2).childAndOverride(() -> Skills.LIKE_A_CAT_II).build(),
                SkillType.Builder.<LikeACatSkill>create(type -> new LikeACatSkill(type, 6000, 4800)).setSurvivalCategory().setRegistryName("like_a_cat_ii").descriptionLength(2).requiredLevel(30).price(3).childAndOverride(() -> Skills.LIKE_A_CAT_III).build(),
                SkillType.Builder.<LikeACatSkill>create(type -> new LikeACatSkill(type, 6000, 8400)).setSurvivalCategory().setRegistryName("like_a_cat_iii").descriptionLength(2).requiredLevel(45).price(4).build(),
                SkillType.Builder.create(BasicSkill::new).setSurvivalCategory().setRegistryName("avenge_me_friends").descriptionLength(3).requiredLevel(65).price(5).build(),
                SkillType.Builder.create(WarMachineSkill::new).setSurvivalCategory().setRegistryName("war_machine").requiredLevel(100).price(15).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("m1911_quickdraw").iconPathNormal("quickdraw").criteria(CriteriaTypes.getPistolCriteria()).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("m1911_extended").iconPathNormal("extended").criteria(CriteriaTypes.getPistolCriteria()).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("m1911_tough_spring").iconPathNormal("tough_spring").criteria(CriteriaTypes.getPistolCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("m1911_carbon_barrel").criteria(CriteriaTypes.getPistolCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("m1911_suppressor").iconPathNormal("suppressor").criteria(CriteriaTypes.getPistolCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("m1911_heavy_bullets").descriptionLength(3).criteria(CriteriaTypes.getPistolCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("m1911_dual_wield").criteria(CriteriaTypes.getPistolCriteria()).requiredLevel(7).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ump45_quickdraw").iconPathNormal("quickdraw").criteria(CriteriaTypes.getSmgCriteria()).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ump45_extended").iconPathNormal("extended").criteria(CriteriaTypes.getSmgCriteria()).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ump45_vertical_grip").iconPathNormal("vertical_grip").criteria(CriteriaTypes.getSmgCriteria()).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ump45_tough_spring").iconPathNormal("tough_spring").criteria(CriteriaTypes.getSmgCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ump45_red_dot").iconPathNormal("red_dot").criteria(CriteriaTypes.getSmgCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ump45_suppressor").iconPathNormal("suppressor").criteria(CriteriaTypes.getSmgCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ump45_commando").descriptionLength(3).criteria(CriteriaTypes.getSmgCriteria()).requiredLevel(7).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_quiver").criteria(CriteriaTypes.getCrossbowCriteria()).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_poisoned_bolts").criteria(CriteriaTypes.getCrossbowCriteria()).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_hunter").criteria(CriteriaTypes.getCrossbowCriteria()).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_tough_bowstring").criteria(CriteriaTypes.getCrossbowCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_penetrator").iconPathNormal("penetrator").criteria(CriteriaTypes.getCrossbowCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_scope").iconPathNormal("scope").criteria(CriteriaTypes.getCrossbowCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_repeater").descriptionLength(2).criteria(CriteriaTypes.getCrossbowCriteria()).requiredLevel(7).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("s1897_bullet_loops").criteria(CriteriaTypes.getShotgunCriteria()).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("s1897_extended").iconPathNormal("extended").criteria(CriteriaTypes.getShotgunCriteria()).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("s1897_pump_in_action").criteria(CriteriaTypes.getShotgunCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("s1897_choke").criteria(CriteriaTypes.getShotgunCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("s1897_never_give_up").criteria(CriteriaTypes.getShotgunCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("s1897_extended_barrel").criteria(CriteriaTypes.getShotgunCriteria()).requiredLevel(7).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sks_tough_spring").iconPathNormal("tough_spring").criteria(CriteriaTypes.getAssaltRifleCriteria()).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sks_vertical_grip").iconPathNormal("vertical_grip").criteria(CriteriaTypes.getAssaltRifleCriteria()).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sks_extended").iconPathNormal("extended").criteria(CriteriaTypes.getAssaltRifleCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sks_red_dot").iconPathNormal("red_dot").criteria(CriteriaTypes.getAssaltRifleCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sks_suppressor").iconPathNormal("suppressor").criteria(CriteriaTypes.getAssaltRifleCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sks_cheekpad").iconPathNormal("cheekpad").criteria(CriteriaTypes.getAssaltRifleCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sks_adaptive_chambering").descriptionLength(2).criteria(CriteriaTypes.getAssaltRifleCriteria()).requiredLevel(7).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("kar98k_scope").iconPathNormal("scope").criteria(CriteriaTypes.getSniperRifleCriteria()).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("kar98k_cheekpad").iconPathNormal("cheekpad").criteria(CriteriaTypes.getSniperRifleCriteria()).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("kar98k_extended").iconPathNormal("extended").criteria(CriteriaTypes.getSniperRifleCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("kar98k_suppressor").iconPathNormal("suppressor").criteria(CriteriaTypes.getSniperRifleCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("kar98k_fast_hands").criteria(CriteriaTypes.getSniperRifleCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("kar98k_penetrator").iconPathNormal("penetrator").criteria(CriteriaTypes.getSniperRifleCriteria()).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("kar98k_dead_eye").criteria(CriteriaTypes.getSniperRifleCriteria()).requiredLevel(7).build()
        );
    }

    @SubscribeEvent
    public static void onDebuffRegister(RegistryEvent.Register<DebuffType<?>> event) {
        event.getRegistry().registerAll(
                DebuffRegistration.createPoisonType(),
                DebuffRegistration.createInfectionType(),
                DebuffRegistration.createFractureType(),
                DebuffRegistration.createBleedType(),
                DebuffRegistration.createRespawnType()
        );
    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                new BaseOreBlock("amethyst_ore", AbstractBlock.Properties.of(Material.STONE).strength(2.5F, 10.0F).harvestTool(ToolType.PICKAXE).harvestLevel(2).requiresCorrectToolForDrops()),
                new BlastFurnaceBlock("blast_furnace"),
                new AirdropBlock("airdrop"),
                new SmithingTableBlock("smithing_table"),
                new GoldDragonEggBlock("gold_dragon_egg"),
                new DeathCrateBlock("death_crate"),
                new SpikeTrapBlock("spikes_wooden", 30, 1.0F, AbstractBlock.Properties.of(Material.WOOD).strength(1.2F).noOcclusion()),
                new SpikeTrapBlock("spikes_iron", 40, 2.0F, SpikeTrapBlock::ironSpikesInteract, AbstractBlock.Properties.of(Material.METAL).strength(1.8F).noOcclusion()),
                new SpikeTrapBlock("spikes_diamond", 50, 3.0F, SpikeTrapBlock::diamondSpikesInteract, AbstractBlock.Properties.of(Material.METAL).strength(2.2F).noOcclusion()),
                new TrapBlock("landmine", AbstractBlock.Properties.of(Material.METAL).noOcclusion(), new TrapBlock.MineReaction(3.0F)),
                new TrapBlock("large_landmine", AbstractBlock.Properties.of(Material.METAL).noOcclusion(), new TrapBlock.MineReaction(5.0F)),
                new TrapBlock("hidden_landmine", AbstractBlock.Properties.of(Material.METAL).noOcclusion(), new TrapBlock.MineReaction(3.0F))
        );
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.registerAll(
                BaseItem.basic("amethyst"),
                DebuffHealItem.define("antidotum_pills")
                        .defineSound(() -> ModSounds.USE_ANTIDOTUM_PILLS)
                        .canUse(data -> data.hasDebuff(Debuffs.POISON))
                        .onUse(data -> data.heal(Debuffs.POISON, 40))
                        .describe("Heals 40% of poison progress")
                        .animate(32, AnimationPaths.PILLS)
                        .build(),
                DebuffHealItem.define("vaccine")
                        .defineSound(() -> ModSounds.USE_VACCINE)
                        .canUse(data -> data.hasDebuff(Debuffs.INFECTION))
                        .onUse(data -> data.heal(Debuffs.INFECTION, 50))
                        .describe("Heals 50% of infection progress")
                        .animate(32, AnimationPaths.INJECTION)
                        .build(),
                DebuffHealItem.define("plaster_cast")
                        .defineSound(() -> ModSounds.USE_PLASTER_CAST)
                        .canUse(data -> data.hasDebuff(Debuffs.FRACTURE))
                        .onUse(data -> data.heal(Debuffs.FRACTURE, 35))
                        .describe("Heals 35% of fracture progress")
                        .animate(32, AnimationPaths.SPLINT)
                        .build(),
                DebuffHealItem.define("bandage")
                        .defineSound(() -> ModSounds.USE_BANDAGE)
                        .canUse(data -> data.hasDebuff(Debuffs.BLEED))
                        .onUse(data -> data.heal(Debuffs.BLEED, 25))
                        .describe("Heals 25% of bleeding progress")
                        .animate(50, AnimationPaths.BANDAGE)
                        .build(),
                PlayerHealItem.define("analgetics")
                        .defineSound(() -> ModSounds.USE_ANTIDOTUM_PILLS)
                        .canUse(player -> player.getHealth() < player.getMaxHealth())
                        .onUse(player -> player.heal(5))
                        .describe("Recovers 2.5 hearts")
                        .animate(32, AnimationPaths.PILLS)
                        .build(),
                PlayerHealItem.define("stereoids")
                        .defineSound(() -> ModSounds.USE_VACCINE)
                        .onUse(PlayerHealItem::onStereoidsUsed)
                        .describe("Effects:", "Strength I for 60 seconds", "Jump Boost II for 60 seconds")
                        .animate(32, AnimationPaths.INJECTION)
                        .build(),
                PlayerHealItem.define("adrenaline")
                        .defineSound(() -> ModSounds.USE_VACCINE)
                        .onUse(PlayerHealItem::onAdrenalineUsed)
                        .describe("Effects:", "Regeneration I for 35 seconds", "Speed I for 60 seconds")
                        .animate(32, AnimationPaths.INJECTION)
                        .build(),
                PlayerHealItem.define("painkillers")
                        .defineSound(() -> ModSounds.USE_ANTIDOTUM_PILLS)
                        .canUse(player -> player.getHealth() < player.getMaxHealth())
                        .onUse(player -> player.heal(12.0F))
                        .describe("Recovers 6 hearts")
                        .animate(32, AnimationPaths.PILLS)
                        .build(),
                PlayerHealItem.define("morphine")
                        .defineSound(() -> ModSounds.USE_VACCINE)
                        .onUse(PlayerHealItem::onMorphineUsed)
                        .describe("Recovers 7 hearts", "Effects:", "Regeneration II for 15 seconds", "Strength II for 30 seconds",
                                "Resistance I for 45 seconds", "Additional 20% to projectile damage")
                        .animate(32, AnimationPaths.PILLS)
                        .build(),
                new AmmoItem("wooden_ammo_9mm", AmmoType._9MM, AmmoMaterials.WOOD),
                new AmmoItem("wooden_ammo_45acp", AmmoType._45ACP, AmmoMaterials.WOOD),
                new AmmoItem("wooden_ammo_556mm", AmmoType._556MM, AmmoMaterials.WOOD),
                new AmmoItem("wooden_ammo_762mm", AmmoType._762MM, AmmoMaterials.WOOD),
                new AmmoItem("wooden_ammo_12g", AmmoType._12G, AmmoMaterials.WOOD),
                new AmmoItem("wooden_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterials.WOOD),
                new AmmoItem("stone_ammo_9mm", AmmoType._9MM, AmmoMaterials.STONE),
                new AmmoItem("stone_ammo_45acp", AmmoType._45ACP, AmmoMaterials.STONE),
                new AmmoItem("stone_ammo_556mm", AmmoType._556MM, AmmoMaterials.STONE),
                new AmmoItem("stone_ammo_762mm", AmmoType._762MM, AmmoMaterials.STONE),
                new AmmoItem("stone_ammo_12g", AmmoType._12G, AmmoMaterials.STONE),
                new AmmoItem("stone_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterials.STONE),
                new AmmoItem("iron_ammo_9mm", AmmoType._9MM, AmmoMaterials.IRON),
                new AmmoItem("iron_ammo_45acp", AmmoType._45ACP, AmmoMaterials.IRON),
                new AmmoItem("iron_ammo_556mm", AmmoType._556MM, AmmoMaterials.IRON),
                new AmmoItem("iron_ammo_762mm", AmmoType._762MM, AmmoMaterials.IRON),
                new AmmoItem("iron_ammo_12g", AmmoType._12G, AmmoMaterials.IRON),
                new AmmoItem("iron_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterials.IRON),
                new AmmoItem("gold_ammo_9mm", AmmoType._9MM, AmmoMaterials.GOLD),
                new AmmoItem("gold_ammo_45acp", AmmoType._45ACP, AmmoMaterials.GOLD),
                new AmmoItem("gold_ammo_556mm", AmmoType._556MM, AmmoMaterials.GOLD),
                new AmmoItem("gold_ammo_762mm", AmmoType._762MM, AmmoMaterials.GOLD),
                new AmmoItem("gold_ammo_12g", AmmoType._12G, AmmoMaterials.GOLD),
                new AmmoItem("gold_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterials.GOLD),
                new AmmoItem("diamond_ammo_9mm", AmmoType._9MM, AmmoMaterials.DIAMOND),
                new AmmoItem("diamond_ammo_45acp", AmmoType._45ACP, AmmoMaterials.DIAMOND),
                new AmmoItem("diamond_ammo_556mm", AmmoType._556MM, AmmoMaterials.DIAMOND),
                new AmmoItem("diamond_ammo_762mm", AmmoType._762MM, AmmoMaterials.DIAMOND),
                new AmmoItem("diamond_ammo_12g", AmmoType._12G, AmmoMaterials.DIAMOND),
                new AmmoItem("diamond_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterials.DIAMOND),
                new AmmoItem("emerald_ammo_9mm", AmmoType._9MM, AmmoMaterials.EMERALD),
                new AmmoItem("emerald_ammo_45acp", AmmoType._45ACP, AmmoMaterials.EMERALD),
                new AmmoItem("emerald_ammo_556mm", AmmoType._556MM, AmmoMaterials.EMERALD),
                new AmmoItem("emerald_ammo_762mm", AmmoType._762MM, AmmoMaterials.EMERALD),
                new AmmoItem("emerald_ammo_12g", AmmoType._12G, AmmoMaterials.EMERALD),
                new AmmoItem("emerald_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterials.EMERALD),
                new AmmoItem("amethyst_ammo_9mm", AmmoType._9MM, AmmoMaterials.AMETHYST),
                new AmmoItem("amethyst_ammo_45acp", AmmoType._45ACP, AmmoMaterials.AMETHYST),
                new AmmoItem("amethyst_ammo_556mm", AmmoType._556MM, AmmoMaterials.AMETHYST),
                new AmmoItem("amethyst_ammo_762mm", AmmoType._762MM, AmmoMaterials.AMETHYST),
                new AmmoItem("amethyst_ammo_12g", AmmoType._12G, AmmoMaterials.AMETHYST),
                new AmmoItem("amethyst_ammo_crossbow_bolt", AmmoType.CROSSBOW, AmmoMaterials.AMETHYST),
                new M1911Item("m1911"),
                new Ump45Item("ump45"),
                new SksItem("sks"),
                new Kar98kItem("kar98k"),
                new S1897Item("s1897"),
                new WoodenCrossbowItem("wooden_crossbow"),
                BaseItem.basic("small_bullet_casing"),
                BaseItem.basic("large_bullet_casing"),
                BaseItem.basic("shotgun_shell"),
                BaseItem.basic("barrel"),
                BaseItem.basic("long_barrel"),
                BaseItem.basic("iron_stock"),
                BaseItem.basic("small_iron_stock"),
                BaseItem.basic("wooden_stock"),
                BaseItem.basic("magazine"),
                BaseItem.basic("gun_parts"),
                BaseItem.basic("bolt_fletching"),
                new GrenadeItem("grenade", 4, false),
                new GrenadeItem("massive_grenade", 6, false),
                new GrenadeItem("impact_grenade", 4, true),
                BaseItem.basic("iron_ore_chunk"),
                BaseItem.basic("gold_ore_chunk"),
                new SkillBookItem("skillpoint_book"),
                new HammerItem("wooden_hammer", HammerItem.WOOD_HAMMER_MATERIAL),
                new HammerItem("stone_hammer", HammerItem.STONE_HAMMER_MATERIAL),
                new HammerItem("iron_hammer", HammerItem.IRON_HAMMER_MATERIAL),
                BaseItem.basic("gold_egg_shard"),
                BaseItem.basic("blaze_lump"),
                new ModFoodItem("bacon_burger", ModFoods.BACON_BURGER),
                new ModFoodItem("fish_and_chips", ModFoods.FISH_AND_CHIPS),
                new ModFoodItem("garden_soup", ModFoods.GARDEN_SOUP),
                new ModFoodItem("chicken_dinner", ModFoods.CHICKED_DINNER),
                new ModFoodItem("deluxe_meal", ModFoods.DELUXE_MEAL).buff(player -> player.heal(3)),
                new ModFoodItem("meaty_stew_xxl", ModFoods.MEATY_STEW_XXL).buff(player -> player.heal(5)),
                new ModFoodItem("rabbit_creamy_soup", ModFoods.RABBIT_CREAMY_SOUP).buff(player -> player.heal(3)),
                new ModFoodItem("shepherds_pie", ModFoods.SHEPHERDS_PIE),
                new ModFoodItem("fruit_salad", ModFoods.FRUIT_SALAD).buff(player -> player.heal(2.0F)),
                new ModFoodItem("egg_salad", ModFoods.EGG_SALAD),
                new ModFoodItem("chocolate_glazed_apple_pie", ModFoods.CHOCOLATE_GLAZED_APPLE_PIE).buff(player -> player.heal(3.0F))
        );
        queue.forEach(registry::register);
        queue = null;
    }

    @SubscribeEvent
    public static void onSoundRegister(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                sound("use_antidotum_pills"),
                sound("use_vaccine"),
                sound("use_plaster_cast"),
                sound("use_bandage"),
                sound("plane_fly_by"),
                sound("m1911"),
                sound("m1911_silent"),
                sound("ump45"),
                sound("ump45_silent"),
                sound("sks"),
                sound("sks_silent"),
                sound("kar98k"),
                sound("kar98k_silent"),
                sound("s1897"),
                sound("m1911_reload"),
                sound("m1911_reload_short"),
                sound("ump45_reload"),
                sound("ump45_reload_short"),
                sound("sks_reload"),
                sound("kar98k_reload"),
                sound("kar98k_reload_short"),
                sound("kar98k_bolt"),
                sound("s1897_reload"),
                sound("s1897_reload_short"),
                sound("m9"),
                sound("mp5"),
                sound("slr"),
                sound("m24"),
                sound("s686"),
                sound("kar98k_reload_clip"),
                sound("kar98k_reload_clip_fast"),
                sound("crossbow_shoot"),
                sound("crossbow_reload"),
                sound("crossbow_reload_fast"),
                sound("flare_shoot"),
                sound("second_chance_use"),
                sound("relaxed_2"),
                sound("use_avenge_me_friends"),
                sound("use_well_fed"),
                sound("bullet_whizz")
        );
    }

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.ZOMBIE_GUNNER.get(), ZombieGunnerEntity.createAttributes().build());
        event.put(ModEntities.EXPLOSIVE_SKELETON.get(), ExplosiveSkeletonEntity.createAttributes().build());
        event.put(ModEntities.ROCKET_ANGEL.get(), RocketAngelEntity.createAttributes().build());
        event.put(ModEntities.BLOODMOON_GOLEM.get(), BloodmoonGolemEntity.createAttributes().build());
        event.put(ModEntities.GOLD_DRAGON.get(), GoldDragonEntity.createAttributes().build());
    }

    protected static SoundEvent sound(String key) {
        ResourceLocation name = GunsRPG.makeResource(key);
        return new SoundEvent(name).setRegistryName(name);
    }

    protected static <V extends IForgeRegistryEntry<V>> RegistryBuilder<V> createRegistry(ResourceLocation location, Class<V> type) {
        return new RegistryBuilder<V>().setName(location).setType(type).setMaxID(Integer.MAX_VALUE - 1);
    }

    @SuppressWarnings("unchecked")
    private static <V extends ForgeRegistryEntry<V>> IForgeRegistry<V> createGenericRegistry(String name, Class<?> vClass) {
        ResourceLocation location = GunsRPG.makeResource(name);
        createRegistry(location, (Class<V>) vClass).create();
        return RegistryManager.ACTIVE.getRegistry(location);
    }
}
