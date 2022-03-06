package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.api.common.data.IPlayerData;
import dev.toma.gunsrpg.common.AnimationPaths;
import dev.toma.gunsrpg.common.attribute.Attribs;
import dev.toma.gunsrpg.common.attribute.AttributeTarget;
import dev.toma.gunsrpg.common.attribute.Modifiers;
import dev.toma.gunsrpg.common.block.*;
import dev.toma.gunsrpg.common.container.*;
import dev.toma.gunsrpg.common.debuffs.DebuffRegistration;
import dev.toma.gunsrpg.common.debuffs.DebuffType;
import dev.toma.gunsrpg.common.entity.*;
import dev.toma.gunsrpg.common.item.*;
import dev.toma.gunsrpg.common.item.guns.*;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoItem;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoMaterials;
import dev.toma.gunsrpg.common.item.guns.ammo.AmmoType;
import dev.toma.gunsrpg.common.item.heal.AttributeAccessHealItem;
import dev.toma.gunsrpg.common.item.heal.ContinuousHealingItem;
import dev.toma.gunsrpg.common.item.heal.DebuffHealItem;
import dev.toma.gunsrpg.common.item.heal.PlayerHealItem;
import dev.toma.gunsrpg.common.item.perk.CrystalItem;
import dev.toma.gunsrpg.common.item.perk.PerkItem;
import dev.toma.gunsrpg.common.item.perk.PerkVariant;
import dev.toma.gunsrpg.common.skills.*;
import dev.toma.gunsrpg.common.skills.core.DisplayData;
import dev.toma.gunsrpg.common.skills.core.DisplayType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.util.Interval;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.util.helper.StorageUtil;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;

import java.util.ArrayList;
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
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.GUNPOWDER_I, Attribs.GUNPOWDER_OUTPUT))).description(0).build().setRegistryName("gunpowder_novice"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.GUNPOWDER_II, Attribs.GUNPOWDER_OUTPUT))).description(0).build().setRegistryName("gunpowder_expert"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.GUNPOWDER_III, Attribs.GUNPOWDER_OUTPUT))).description(0).build().setRegistryName("gunpowder_master"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.BONEMEAL_I, Attribs.BONEMEAL_OUTPUT))).description(0).build().setRegistryName("bone_grinder_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.BONEMEAL_II, Attribs.BONEMEAL_OUTPUT))).description(0).build().setRegistryName("bone_grinder_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.BONEMEAL_III, Attribs.BONEMEAL_OUTPUT))).description(0).build().setRegistryName("bone_grinder_iii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.BLAZEPOWDER_I, Attribs.BLAZEPOWDER_OUTPUT))).description(0).build().setRegistryName("blaze_powder_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.BLAZEPOWDER_II, Attribs.BLAZEPOWDER_OUTPUT))).description(0).build().setRegistryName("blaze_powder_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.BLAZEPOWDER_III, Attribs.BLAZEPOWDER_OUTPUT))).description(0).build().setRegistryName("blaze_powder_iii"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("wooden_ammo_smith"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("stone_ammo_smith"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("iron_ammo_smith"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("lapis_ammo_smith"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("gold_ammo_smith"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("redstone_ammo_smith"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("diamond_ammo_smith"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("quartz_ammo_smith"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("emerald_ammo_smith"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("amethyst_ammo_smith"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("netherite_ammo_smith"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.MASTER_AMMO, Attribs.AMMO_OUTPUT))).description(0).build().setRegistryName("ammo_smithing_mastery"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("gun_parts_smith"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModBlocks.REPAIR_STATION))).build().setRegistryName("repair_station"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.M1911))).build().setRegistryName("m1911_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.R45))).build().setRegistryName("r45_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.DESERT_EAGLE))).build().setRegistryName("desert_eagle_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.UMP45))).build().setRegistryName("ump45_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.THOMPSON))).build().setRegistryName("thompson_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.VECTOR))).build().setRegistryName("vector_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.AKM))).build().setRegistryName("akm_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.HK416))).build().setRegistryName("hk416_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.AUG))).build().setRegistryName("aug_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.WOODEN_CROSSBOW))).build().setRegistryName("crossbow_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.CHUKONU))).build().setRegistryName("chukonu_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.S1897))).build().setRegistryName("s1897_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.S686))).build().setRegistryName("s686_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.S12K))).build().setRegistryName("s12k_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.SKS))).build().setRegistryName("sks_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.VSS))).build().setRegistryName("vss_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.MK14EBR))).build().setRegistryName("mk14ebr_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.KAR98K))).build().setRegistryName("kar98k_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.WINCHESTER))).build().setRegistryName("winchester_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.AWM))).build().setRegistryName("awm_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.GRENADE_LAUNCHER))).build().setRegistryName("grenade_launcher_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.ROCKET_LAUNCHER))).build().setRegistryName("rocket_launcher_assembly"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("grenades"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("massive_grenades"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("impact_grenades"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("demolisher"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("heavy_demolisher"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("repair_man_i"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("repair_man_ii"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("repair_man_iii"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("careful_gunner_i"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("careful_gunner_ii"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("careful_gunner_iii"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("lucky_shooter_i"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("lucky_shooter_ii"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("lucky_shooter_iii"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("medic"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("doctor"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("efficient_meds"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.DEBUFF_DELAY_I, Attribs.POISON_DELAY), AttributeTarget.create(Modifiers.DEBUFF_RESIST_I, Attribs.POISON_RESISTANCE))).description(0).build().setRegistryName("poison_resistance_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.DEBUFF_DELAY_II, Attribs.POISON_DELAY), AttributeTarget.create(Modifiers.DEBUFF_RESIST_II, Attribs.POISON_RESISTANCE))).description(0).build().setRegistryName("poison_resistance_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.DEBUFF_DELAY_III, Attribs.POISON_DELAY), AttributeTarget.create(Modifiers.DEBUFF_RESIST_III, Attribs.POISON_RESISTANCE))).description(0).build().setRegistryName("poison_resistance_iii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.DEBUFF_DELAY_I, Attribs.INFECTION_DELAY), AttributeTarget.create(Modifiers.DEBUFF_RESIST_I, Attribs.INFECTION_RESISTANCE))).description(0).build().setRegistryName("infection_resistance_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.DEBUFF_DELAY_II, Attribs.INFECTION_DELAY), AttributeTarget.create(Modifiers.DEBUFF_RESIST_II, Attribs.INFECTION_RESISTANCE))).description(0).build().setRegistryName("infection_resistance_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.DEBUFF_DELAY_III, Attribs.INFECTION_DELAY), AttributeTarget.create(Modifiers.DEBUFF_RESIST_III, Attribs.INFECTION_RESISTANCE))).description(0).build().setRegistryName("infection_resistance_iii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.DEBUFF_DELAY_I, Attribs.FRACTURE_DELAY), AttributeTarget.create(Modifiers.DEBUFF_RESIST_I, Attribs.FRACTURE_RESISTANCE))).description(0).build().setRegistryName("fracture_resistance_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.DEBUFF_DELAY_II, Attribs.FRACTURE_DELAY), AttributeTarget.create(Modifiers.DEBUFF_RESIST_II, Attribs.FRACTURE_RESISTANCE))).description(0).build().setRegistryName("fracture_resistance_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.DEBUFF_DELAY_III, Attribs.FRACTURE_DELAY), AttributeTarget.create(Modifiers.DEBUFF_RESIST_III, Attribs.FRACTURE_RESISTANCE))).description(0).build().setRegistryName("fracture_resistance_iii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.DEBUFF_DELAY_I, Attribs.BLEED_DELAY), AttributeTarget.create(Modifiers.DEBUFF_RESIST_I, Attribs.FRACTURE_RESISTANCE))).description(0).build().setRegistryName("bleeding_resistance_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.DEBUFF_DELAY_II, Attribs.BLEED_DELAY), AttributeTarget.create(Modifiers.DEBUFF_RESIST_II, Attribs.FRACTURE_RESISTANCE))).description(0).build().setRegistryName("bleeding_resistance_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.DEBUFF_DELAY_III, Attribs.BLEED_DELAY), AttributeTarget.create(Modifiers.DEBUFF_RESIST_III, Attribs.FRACTURE_RESISTANCE))).description(0).build().setRegistryName("bleeding_resistance_iii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.ACROBATICS_FALL_I, Attribs.FALL_RESISTANCE), AttributeTarget.create(Modifiers.ACROBATICS_EXPLOSION_I, Attribs.EXPLOSION_RESISTANCE))).build().setRegistryName("acrobatics_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.ACROBATICS_FALL_II, Attribs.FALL_RESISTANCE), AttributeTarget.create(Modifiers.ACROBATICS_EXPLOSION_II, Attribs.EXPLOSION_RESISTANCE))).build().setRegistryName("acrobatics_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.ACROBATICS_FALL_III, Attribs.FALL_RESISTANCE), AttributeTarget.create(Modifiers.ACROBATICS_EXPLOSION_III, Attribs.EXPLOSION_RESISTANCE))).build().setRegistryName("acrobatics_iii"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("pharmacist_i"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("pharmacist_ii"),
                SkillType.Builder.create(BasicSkill::new).description(2).build().setRegistryName("pharmacist_iii"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("pharmacist_iv"),
                SkillType.Builder.create(BasicSkill::new).description(2).build().setRegistryName("pharmacist_v"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.CHOPPING_I, Attribs.WOODCUTTING_SPEED))).description(0).build().setRegistryName("sharp_axe_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.CHOPPING_II, Attribs.WOODCUTTING_SPEED))).description(0).build().setRegistryName("sharp_axe_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.CHOPPING_III, Attribs.WOODCUTTING_SPEED))).description(0).build().setRegistryName("sharp_axe_iii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.CHOPPING_IV, Attribs.WOODCUTTING_SPEED))).description(0).build().setRegistryName("sharp_axe_iv"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.CHOPPING_V, Attribs.WOODCUTTING_SPEED))).description(0).build().setRegistryName("sharp_axe_v"),
                SkillType.Builder.<LumberjackSkill>create(type -> new LumberjackSkill(type, 1)).build().setRegistryName("lumberjack_i"),
                SkillType.Builder.<LumberjackSkill>create(type -> new LumberjackSkill(type, 2)).build().setRegistryName("lumberjack_ii"),
                SkillType.Builder.<LumberjackSkill>create(type -> new LumberjackSkill(type, 3)).build().setRegistryName("lumberjack_iii"),
                SkillType.Builder.<LumberjackSkill>create(type -> new LumberjackSkill(type, 4)).build().setRegistryName("lumberjack_iv"),
                SkillType.Builder.<LumberjackSkill>create(type -> new LumberjackSkill(type, 5)).build().setRegistryName("lumberjack_v"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.DIGGING_I, Attribs.DIGGING_SPEED))).description(0).build().setRegistryName("grave_digger_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.DIGGING_II, Attribs.DIGGING_SPEED))).description(0).build().setRegistryName("grave_digger_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.DIGGING_III, Attribs.DIGGING_SPEED))).description(0).build().setRegistryName("grave_digger_iii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.DIGGING_IV, Attribs.DIGGING_SPEED))).description(0).build().setRegistryName("grave_digger_iv"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.DIGGING_V, Attribs.DIGGING_SPEED))).description(0).build().setRegistryName("grave_digger_v"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.MINING_I, Attribs.MINING_SPEED))).description(0).build().setRegistryName("heavy_pickaxe_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.MINING_II, Attribs.MINING_SPEED))).description(0).build().setRegistryName("heavy_pickaxe_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.MINING_III, Attribs.MINING_SPEED))).description(0).build().setRegistryName("heavy_pickaxe_iii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.MINING_IV, Attribs.MINING_SPEED))).description(0).build().setRegistryName("heavy_pickaxe_iv"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.MINING_V, Attribs.MINING_SPEED))).description(0).build().setRegistryName("heavy_pickaxe_v"),
                SkillType.Builder.<MotherlodeSkill>create(type -> new MotherlodeSkill(type, 1)).build().setRegistryName("mother_lode_i"),
                SkillType.Builder.<MotherlodeSkill>create(type -> new MotherlodeSkill(type, 2)).build().setRegistryName("mother_lode_ii"),
                SkillType.Builder.<MotherlodeSkill>create(type -> new MotherlodeSkill(type, 3)).build().setRegistryName("mother_lode_iii"),
                SkillType.Builder.<MotherlodeSkill>create(type -> new MotherlodeSkill(type, 4)).build().setRegistryName("mother_lode_iv"),
                SkillType.Builder.<MotherlodeSkill>create(type -> new MotherlodeSkill(type, 5)).build().setRegistryName("mother_lode_v"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("hammer_i"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("hammer_ii"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("hammer_iii"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("blacksmith"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("mineralogist"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModBlocks.CRYSTAL_STATION))).build().setRegistryName("crystal_station"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModBlocks.CRYSTAL_FUSE))).build().setRegistryName("crystal_forge"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModBlocks.CRYSTAL_PURIFICATION))).build().setRegistryName("crystal_purification_station"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.DAMAGE_I, Attribs.MELEE_DAMAGE))).description(0).build().setRegistryName("strong_muscles_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.DAMAGE_II, Attribs.MELEE_DAMAGE))).description(0).build().setRegistryName("strong_muscles_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.DAMAGE_III, Attribs.MELEE_DAMAGE))).description(0).build().setRegistryName("strong_muscles_iii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.AGILITY_I, Attribs.MOVEMENT_SPEED))).description(0).build().setRegistryName("agility_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.AGILITY_II, Attribs.MOVEMENT_SPEED))).description(0).build().setRegistryName("agility_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.AGILITY_III, Attribs.MOVEMENT_SPEED))).description(0).build().setRegistryName("agility_iii"),
                SkillType.Builder.<AdrenalineRushSkill>create(type -> new AdrenalineRushSkill(type, 0, 0.05f)).build().setRegistryName("adrenaline_rush_i"),
                SkillType.Builder.<AdrenalineRushSkill>create(type -> new AdrenalineRushSkill(type, 1, 0.1f)).build().setRegistryName("adrenaline_rush_ii"),
                SkillType.Builder.<AdrenalineRushSkill>create(type -> new AdrenalineRushSkill(type, 2, 0.2f)).build().setRegistryName("adrenaline_rush_iii"),
                SkillType.Builder.<WellFedSkill>create(type -> new WellFedSkill(type, 1, 0.40F)).build().setRegistryName("well_fed_i"),
                SkillType.Builder.<WellFedSkill>create(type -> new WellFedSkill(type, 2, 0.55F)).build().setRegistryName("well_fed_ii"),
                SkillType.Builder.<WellFedSkill>create(type -> new WellFedSkill(type, 3, 0.70F)).build().setRegistryName("well_fed_iii"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("local_chef"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("master_chef"),
                SkillType.Builder.<SecondChanceSkill>create(type -> new SecondChanceSkill(type, Interval.minutes(15), 10, 0)).build().setRegistryName("second_chance_i"),
                SkillType.Builder.<SecondChanceSkill>create(type -> new SecondChanceSkill(type, Interval.minutes(12), 15, 1)).build().setRegistryName("second_chance_ii"),
                SkillType.Builder.<SecondChanceSkill>create(type -> new SecondChanceSkill(type, Interval.minutes( 9), 20, 2)).build().setRegistryName("second_chance_iii"),
                SkillType.Builder.create(type -> new IronBuddySkill(type, Interval.minutes(25))).build().setRegistryName("iron_buddy_i"),
                SkillType.Builder.create(type -> new IronBuddySkill(type, Interval.minutes(20), 50.0F)).build().setRegistryName("iron_buddy_ii"),
                SkillType.Builder.create(type -> new IronBuddySkill(type, Interval.minutes(15), 100.0F)).build().setRegistryName("iron_buddy_iii"),
                SkillType.Builder.create(GodHelpUsSkill::new).description(2).build().setRegistryName("god_help_us"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.INSTANT_KILL_I, Attribs.INSTANT_KILL))).description(0).build().setRegistryName("skull_crusher_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.INSTANT_KILL_II, Attribs.INSTANT_KILL))).description(0).build().setRegistryName("skull_crusher_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.INSTANT_KILL_III, Attribs.INSTANT_KILL))).description(0).build().setRegistryName("skull_crusher_iii"),
                SkillType.Builder.create(LightHunterSkill::new).build().setRegistryName("light_hunter"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("traps_i"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("traps_ii"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("traps_iii"),
                SkillType.Builder.<BartenderSkill>create(type -> new BartenderSkill(type, 1)).description(0).build().setRegistryName("bartender_i"),
                SkillType.Builder.<BartenderSkill>create(type -> new BartenderSkill(type, 2)).description(0).build().setRegistryName("bartender_ii"),
                SkillType.Builder.<BartenderSkill>create(type -> new BartenderSkill(type, 3)).description(0).build().setRegistryName("bartender_iii"),
                SkillType.Builder.<BartenderSkill>create(type -> new BartenderSkill(type, 4)).description(0).build().setRegistryName("bartender_iv"),
                SkillType.Builder.<BartenderSkill>create(type -> new BartenderSkill(type, 5)).build().setRegistryName("bartender_v"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("crystalized"),
                SkillType.Builder.<LikeACatSkill>create(type -> new LikeACatSkill(type, Interval.minutes(5), Interval.minutes(2))).build().setRegistryName("like_a_cat_i"),
                SkillType.Builder.<LikeACatSkill>create(type -> new LikeACatSkill(type, Interval.minutes(5), Interval.minutes(4))).build().setRegistryName("like_a_cat_ii"),
                SkillType.Builder.<LikeACatSkill>create(type -> new LikeACatSkill(type, Interval.minutes(5), Interval.minutes(7))).build().setRegistryName("like_a_cat_iii"),
                SkillType.Builder.create(AvengeMeFriendsSkill::new).build().setRegistryName("avenge_me_friends"),
                SkillType.Builder.create(WarMachineSkill::new).build().setRegistryName("war_machine"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.QUICKDRAW_MAG, Attribs.M1911_RELOAD_SPEED))).renderModIcon("quickdraw").description(0).build().setRegistryName("m1911_quickdraw"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.M1911_EXTENDED_MAG, Attribs.M1911_MAG_CAPACITY))).renderModIcon("extended").description(0).build().setRegistryName("m1911_extended"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.TOUGH_SPRING, Attribs.M1911_FIRERATE))).renderModIcon("tough_spring").description(0).build().setRegistryName("m1911_tough_spring"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.M1911_CARBON_BARREL, Attribs.M1911_HORIZONTAL_RECOIL), AttributeTarget.create(Modifiers.M1911_CARBON_BARREL, Attribs.M1911_VERTICAL_RECOIL))).renderModIcon("carbon_barrel").build().setRegistryName("m1911_carbon_barrel"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.NOISE, Attribs.M1911_NOISE))).renderModIcon("suppressor").build().setRegistryName("m1911_suppressor"),
                SkillType.Builder.create(BasicSkill::new).description(3).build().setRegistryName("m1911_heavy_bullets"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.createMany(Modifiers.DUAL_WIELD, Attribs.M1911_MAG_CAPACITY, Attribs.M1911_RELOAD_SPEED))).renderModIcon("dual_wield").description(SkillUtil.Localizations::generateSimpleDescription).build().setRegistryName("m1911_dual_wield"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("fast_hands").description(0).build().setRegistryName("r45_fast_hands"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("carbon_barrel").build().setRegistryName("r45_carbon_barrel"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("extended").description(0).build().setRegistryName("r45_extended"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("suppressor").build().setRegistryName("r45_suppressor"),
                SkillType.Builder.create(BasicSkill::new).description(0).build().setRegistryName("r45_light_trigger"),
                SkillType.Builder.create(BasicSkill::new).description(0).build().setRegistryName("r45_ace_of_hearts"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("dual_wield").description(SkillUtil.Localizations::generateSimpleDescription).build().setRegistryName("r45_dual_wield"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("quickdraw").description(0).build().setRegistryName("deagle_quickdraw"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("tough_spring").description(0).build().setRegistryName("deagle_tough_spring"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("compensator").build().setRegistryName("deagle_compensator"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("extended").description(0).build().setRegistryName("deagle_extended"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("red_dot").build().setRegistryName("deagle_red_dot"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("penetrator").build().setRegistryName("deagle_penetrator"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("deagle_finisher"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.QUICKDRAW_MAG, Attribs.UMP45_RELOAD_SPEED))).renderModIcon("quickdraw").description(0).build().setRegistryName("ump45_quickdraw"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.UMP45_EXTENDED, Attribs.UMP45_MAG_CAPACITY))).renderModIcon("extended").description(0).build().setRegistryName("ump45_extended"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.VERTICAL_GRIP, Attribs.UMP45_VERTICAL_RECOIL))).renderModIcon("vertical_grip").build().setRegistryName("ump45_vertical_grip"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.TOUGH_SPRING, Attribs.UMP45_FIRERATE))).renderModIcon("tough_spring").description(0).build().setRegistryName("ump45_tough_spring"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("red_dot").build().setRegistryName("ump45_red_dot"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.NOISE, Attribs.UMP45_NOISE))).renderModIcon("suppressor").build().setRegistryName("ump45_suppressor"),
                SkillType.Builder.create(BasicSkill::new).description(3).build().setRegistryName("ump45_commando"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("quickdraw").description(0).build().setRegistryName("thompson_quickdraw"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("vertical_grip").build().setRegistryName("thompson_vertical"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("red_dot").build().setRegistryName("thompson_red_dot"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("extended").description(0).build().setRegistryName("thompson_extended"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("tough_spring").description(0).build().setRegistryName("thompson_tough_spring"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("suppressor").build().setRegistryName("thompson_suppressor"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("soul_taker").build().setRegistryName("thompson_soul_taker"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("quickdraw").description(0).build().setRegistryName("vector_quickdraw"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("vertical_grip").build().setRegistryName("vector_vertical"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("red_dot").build().setRegistryName("vector_red_dot"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("tough_spring").description(0).build().setRegistryName("vector_tough_spring"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("extended").description(0).build().setRegistryName("vector_extended"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("suppressor").build().setRegistryName("vector_suppressor"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("overloaded").build().setRegistryName("vector_overloaded"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.CROSSBOW_QUIVER, Attribs.CROSSBOW_RELOAD_SPEED))).description(0).renderModIcon("quiver").build().setRegistryName("crossbow_quiver"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("poisoned_bolts").build().setRegistryName("crossbow_poisoned_bolts"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("hunter").description(2).build().setRegistryName("crossbow_hunter"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("tough_bowstring").description(1).build().setRegistryName("crossbow_tough_bowstring"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("penetrator").build().setRegistryName("crossbow_penetrator"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("scope").build().setRegistryName("crossbow_scope"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.CROSSBOW_EXTENDED, Attribs.CROSSBOW_MAG_CAPACITY), AttributeTarget.create(Modifiers.CROSSBOW_REPEATER, Attribs.CROSSBOW_RELOAD_SPEED))).description(2, SkillUtil.Localizations::generateSimpleDescription).build().setRegistryName("crossbow_repeater"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("quiver").description(0).build().setRegistryName("chukonu_quiver"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("poisoned_bolts").build().setRegistryName("chukonu_poisoned_bolts"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("scope").build().setRegistryName("chukonu_scope"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("extended").description(0).build().setRegistryName("chukonu_extended"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("tough_bowstring").build().setRegistryName("chukonu_tough_bowstring"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("gear_grinder").description(0).build().setRegistryName("chukonu_gear_grinder"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("chukonu_heavy_bolts"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.BULLET_LOOPS, Attribs.S1897_RELOAD_SPEED))).description(0).renderModIcon("bullet_loops").build().setRegistryName("s1897_bullet_loops"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.S1897_EXTENDED, Attribs.S1897_MAG_CAPACITY))).description(0).renderModIcon("extended").build().setRegistryName("s1897_extended"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.S1897_FAST_PUMP, Attribs.S1897_FIRERATE))).description(0).build().setRegistryName("s1897_pump_in_action"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.S1897_CHOKE, Attribs.S1897_PELLET_SPREAD))).renderModIcon("choke").build().setRegistryName("s1897_choke"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("never_give_up").build().setRegistryName("s1897_never_give_up"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("extended_barrel").build().setRegistryName("s1897_extended_barrel"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("bullet_loops").description(0).build().setRegistryName("s686_bullet_loops"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("hunter").description(2).build().setRegistryName("s686_hunter"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("choke").build().setRegistryName("s686_choke"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("s686_blazing_pellets"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("s686_cannon_blast"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("extended_barrel").build().setRegistryName("s686_extended_barrel"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("quickdraw").build().setRegistryName("s12k_quickdraw"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("cheekpad").build().setRegistryName("s12k_cheekpad"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("choke").build().setRegistryName("s12k_choke"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("never_give_up").build().setRegistryName("s12k_never_give_up"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("tough_spring").build().setRegistryName("s12k_tough_spring"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("red_dot").build().setRegistryName("s12k_red_dot"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("suppressor").build().setRegistryName("s12k_suppressor"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("extended").build().setRegistryName("s12k_extended_drum"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("quickdraw").description(0).build().setRegistryName("akm_quickdraw"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("extended").description(0).build().setRegistryName("akm_extended"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("suppressor").build().setRegistryName("akm_suppressor"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("red_dot").build().setRegistryName("akm_red_dot"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("tough_spring").description(0).build().setRegistryName("akm_tough_spring"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("every_bullet_counts").build().setRegistryName("akm_every_bullet_counts"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("quickdraw").description(0).build().setRegistryName("hk416_quickdraw"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("vertical_grip").build().setRegistryName("hk416_vertical"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("extended").description(0).build().setRegistryName("hk416_extended"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("red_dot").build().setRegistryName("hk416_red_dot"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("suppressor").build().setRegistryName("hk416_suppressor"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("tough_spring").description(0).build().setRegistryName("hk416_tough_spring"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("cheekpad").build().setRegistryName("hk416_cheekpad"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("overloaded").build().setRegistryName("hk416_overloaded"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("quickdraw").description(0).build().setRegistryName("aug_quickdraw"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("extended").description(0).build().setRegistryName("aug_extended"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("vertical_grip").build().setRegistryName("aug_vertical"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("compensator").build().setRegistryName("aug_compensator"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("red_dot").build().setRegistryName("aug_red_dot"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("suppressor").build().setRegistryName("aug_suppressor"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("tough_spring").description(0).build().setRegistryName("aug_tough_spring"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("soul_taker").build().setRegistryName("aug_soul_taker"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.TOUGH_SPRING, Attribs.SKS_FIRERATE))).description(0).renderModIcon("tough_spring").build().setRegistryName("sks_tough_spring"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.VERTICAL_GRIP, Attribs.SKS_VERTICAL_RECOIL))).renderModIcon("vertical_grip").build().setRegistryName("sks_vertical_grip"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.SKS_EXTENDED, Attribs.SKS_MAG_CAPACITY))).description(0).renderModIcon("extended").build().setRegistryName("sks_extended"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("red_dot").build().setRegistryName("sks_red_dot"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.NOISE, Attribs.SKS_NOISE))).renderModIcon("suppressor").build().setRegistryName("sks_suppressor"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.createMany(Modifiers.CHEEKPAD, Attribs.SKS_VERTICAL_RECOIL, Attribs.SKS_HORIZONTAL_RECOIL))).renderModIcon("cheekpad").build().setRegistryName("sks_cheekpad"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.SKS_ADAPTIVE, Attribs.SKS_FIRERATE))).renderModIcon("adaptive_chambering").build().setRegistryName("sks_adaptive_chambering"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("quickdraw").description(0).build().setRegistryName("vss_quickdraw"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("scope").build().setRegistryName("vss_scope"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("cheekpad").build().setRegistryName("vss_cheekpad"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("extended").description(0).build().setRegistryName("vss_extended"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("adaptive_chambering").build().setRegistryName("vss_adaptive_chambering"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("every_bullet_counts").build().setRegistryName("vss_every_bullet_counts"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("quickdraw").description(0).build().setRegistryName("mk14ebr_quickdraw"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("vertical_grip").build().setRegistryName("mk14ebr_vertical"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("extended").description(0).build().setRegistryName("mk14ebr_extended"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("tough_spring").description(0).build().setRegistryName("mk14ebr_tough_spring"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("cheekpad").build().setRegistryName("mk14ebr_cheekpad"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("scope").build().setRegistryName("mk14ebr_scope"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("suppressor").build().setRegistryName("mk14ebr_suppressor"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("dead_eye").build().setRegistryName("mk14ebr_dead_eye"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("scope").build().setRegistryName("kar98k_scope"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.createMany(Modifiers.CHEEKPAD, Attribs.KAR98K_HORIZONTAL_RECOIL, Attribs.KAR98K_VERTICAL_RECOIL))).renderModIcon("cheekpad").build().setRegistryName("kar98k_cheekpad"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.KAR98K_EXTENDED, Attribs.KAR98K_MAG_CAPACITY))).description(0).renderModIcon("extended").build().setRegistryName("kar98k_extended"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.NOISE, Attribs.KAR98K_NOISE))).renderModIcon("suppressor").build().setRegistryName("kar98k_suppressor"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.KAR98K_FAST_HANDS_RELOAD, Attribs.KAR98K_RELOAD_SPEED), AttributeTarget.create(Modifiers.KAR98K_FAST_HANDS_ROF, Attribs.KAR98K_FIRERATE))).description(0).renderModIcon("fast_hands").build().setRegistryName("kar98k_fast_hands"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("penetrator").build().setRegistryName("kar98k_penetrator"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeTarget.create(Modifiers.KAR98K_DEAD_EYE, Attribs.KAR98K_HS_DAMAGE))).renderModIcon("dead_eye").description(0).build().setRegistryName("kar98k_dead_eye"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("bullet_loops").description(0).build().setRegistryName("winchester_bullet_loops"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("hunter").build().setRegistryName("winchester_hunter"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("scope").build().setRegistryName("winchester_scope"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("extended").description(0).build().setRegistryName("winchester_extended"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("fast_hands").description(0).build().setRegistryName("winchester_fast_hands"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("penetrator").build().setRegistryName("winchester_penetrator"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("winchester_cold_blooded"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("quickdraw").description(0).build().setRegistryName("awm_quickdraw"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("scope").build().setRegistryName("awm_scope"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("extended").description(0).build().setRegistryName("awm_extended"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("cheekpad").build().setRegistryName("awm_cheekpad"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("suppressor").build().setRegistryName("awm_suppressor"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("fast_hands").description(0).build().setRegistryName("awm_fast_hands"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("penetrator").build().setRegistryName("awm_penetrator"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("dead_eye").build().setRegistryName("awm_dead_eye"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("fast_hands").description(0).build().setRegistryName("grenade_launcher_fast_hands"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("gear_grinder").description(0).build().setRegistryName("grenade_launcher_gear_grinder"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("extended").description(0).build().setRegistryName("grenade_launcher_extended"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("grenade_launcher_better_cartridge"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("demolition_expert").build().setRegistryName("grenade_launcher_demolition_expert"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("quickdraw").description(0).build().setRegistryName("rocket_launcher_quickdraw"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("rocket_launcher_homing_missile"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("rocket_launcher_rocket_fuel"),
                SkillType.Builder.create(BasicSkill::new).renderModIcon("demolition_expert").build().setRegistryName("rocket_launcher_demolition_expert"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("rocket_launcher_rocket_barrage")
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
        IForgeRegistry<Block> registry = event.getRegistry();
        registry.registerAll(
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
                new TrapBlock("hidden_landmine", AbstractBlock.Properties.of(Material.METAL).noOcclusion(), new TrapBlock.MineReaction(3.0F)),
                new BaseBlock("crystal_station", AbstractBlock.Properties.of(Material.STONE).noOcclusion()),
                new BaseBlock("crystal_fuse", AbstractBlock.Properties.of(Material.STONE).noOcclusion()),
                new BaseBlock("crystal_purification", AbstractBlock.Properties.of(Material.STONE).noOcclusion()),
                new CookerBlock("cooker"),
                new RepairStationBlock("repair_station"),
                new MilitaryCrateBlock("artic_military_crate"),
                new MilitaryCrateBlock("desert_military_crate"),
                new MilitaryCrateBlock("woodland_military_crate"),
                new CulinaryTableBlock("culinary_table")
        );
        for (PerkVariant variant : PerkVariant.values()) {
            registry.register(new CrystalOre(variant.getRegistryName("crystal_ore")));
            registry.register(new BaseBlock(variant.getRegistryName("orb_ore"), AbstractBlock.Properties.of(Material.STONE).strength(3.3F).harvestTool(ToolType.PICKAXE).harvestLevel(2)));
        }
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.registerAll(
                BaseItem.simpleItem("amethyst"),
                DebuffHealItem.define("antidotum_pills")
                        .canUse(data -> data.hasDebuff(Debuffs.POISON))
                        .onUse(data -> data.heal(Debuffs.POISON, 40))
                        .describe("Heals 40% of poison progress")
                        .animate(60, AnimationPaths.PILLS)
                        .build(),
                DebuffHealItem.define("vaccine")
                        .canUse(data -> data.hasDebuff(Debuffs.INFECTION))
                        .onUse(data -> data.heal(Debuffs.INFECTION, 50))
                        .describe("Heals 50% of infection progress")
                        .animate(75, AnimationPaths.INJECTION)
                        .build(),
                DebuffHealItem.define("plaster_cast")
                        .canUse(data -> data.hasDebuff(Debuffs.FRACTURE))
                        .onUse(data -> data.heal(Debuffs.FRACTURE, 35))
                        .describe("Heals 35% of fracture progress")
                        .animate(65, AnimationPaths.SPLINT)
                        .build(),
                DebuffHealItem.define("bandage")
                        .canUse(data -> data.hasDebuff(Debuffs.BLEED))
                        .onUse(data -> data.heal(Debuffs.BLEED, 25))
                        .describe("Heals 25% of bleeding progress")
                        .animate(70, AnimationPaths.BANDAGE)
                        .build(),
                AttributeAccessHealItem.define("hemostat")
                        .defineModifiers(
                                AttributeTarget.create(Modifiers.BLEED_BLOCKING, Attribs.BLEED_BLOCK),
                                AttributeTarget.create(Modifiers.BLEED_DELAYING, Attribs.BLEED_DELAY)
                        )
                        .describe("Bleeding:", "Disabled for 60 seconds", "Spread speed: -50%")
                        .animate(50, AnimationPaths.HEMOSTAT)
                        .build(),
                AttributeAccessHealItem.define("vitamins")
                        .defineModifiers(
                                AttributeTarget.create(Modifiers.INFECTION_BLOCKING, Attribs.INFECTION_BLOCK),
                                AttributeTarget.create(Modifiers.INFECTION_DELAYING, Attribs.INFECTION_DELAY)
                        )
                        .describe("Infection:", "Disabled for 60 seconds", "Spread speed: -50%")
                        .animate(40, AnimationPaths.VITAMINS)
                        .build(),
                AttributeAccessHealItem.define("propital")
                        .defineModifiers(
                                AttributeTarget.create(Modifiers.FRACTURE_BLOCKING, Attribs.FRACTURE_BLOCK),
                                AttributeTarget.create(Modifiers.FRACTURE_DELAYING, Attribs.FRACTURE_DELAY)
                        )
                        .describe("Fracture:", "Disabled for 60 seconds", "Spread speed: -50%")
                        .animate(30, AnimationPaths.STIM)
                        .build(),
                AttributeAccessHealItem.define("calcium_shot")
                        .defineModifiers(
                                AttributeTarget.create(Modifiers.POISON_BLOCKING, Attribs.POISON_BLOCK),
                                AttributeTarget.create(Modifiers.POISON_DELAYING, Attribs.POISON_DELAY)
                        )
                        .describe("Poison:", "Disabled for 60 seconds", "Spread speed: -50%")
                        .animate(30, AnimationPaths.STIM)
                        .build(),
                ContinuousHealingItem.define("ufak")
                        .uses(65)
                        .prepareIn(45)
                        .canUse(player -> player.getHealth() < player.getMaxHealth())
                        .onUse(player -> player.heal(1))
                        .describe("Recovers 0.5 hearts")
                        .animate(20, AnimationPaths.UFAK)
                        .build(),
                ContinuousHealingItem.define("kodiak")
                        .uses(150)
                        .prepareIn(60)
                        .canUse(player -> player.getHealth() < player.getMaxHealth())
                        .onUse(player -> player.heal(1))
                        .describe("Recovers 0.5 hearts")
                        .animate(30, AnimationPaths.KODIAK)
                        .build(),
                PlayerHealItem.define("analgetics")
                        .canUse(player -> player.getHealth() < player.getMaxHealth())
                        .onUse(player -> player.heal(5))
                        .describe("Recovers 2.5 hearts")
                        .animate(60, AnimationPaths.PILLS)
                        .build(),
                PlayerHealItem.define("stereoids")
                        .onUse(PlayerHealItem::onStereoidsUsed)
                        .describe("Effects:", "Strength I for 60 seconds", "Jump Boost II for 60 seconds")
                        .animate(75, AnimationPaths.INJECTION)
                        .build(),
                PlayerHealItem.define("adrenaline")
                        .onUse(PlayerHealItem::onAdrenalineUsed)
                        .describe("Effects:", "Regeneration I for 35 seconds", "Speed I for 60 seconds")
                        .animate(75, AnimationPaths.INJECTION)
                        .build(),
                PlayerHealItem.define("painkillers")
                        .canUse(player -> player.getHealth() < player.getMaxHealth())
                        .onUse(player -> player.heal(12.0F))
                        .describe("Recovers 6 hearts")
                        .animate(60, AnimationPaths.PILLS)
                        .build(),
                PlayerHealItem.define("morphine")
                        .onUse(PlayerHealItem::onMorphineUsed)
                        .describe("Recovers 7 hearts", "Effects:", "Regeneration II for 15 seconds", "Strength II for 45 seconds",
                                "Resistance I for 60 seconds")
                        .animate(75, AnimationPaths.INJECTION)
                        .build(),
                new AmmoItem("wooden_9mm", AmmoType.AMMO_9MM, AmmoMaterials.WOOD),
                new AmmoItem("wooden_45acp", AmmoType.AMMO_45ACP, AmmoMaterials.WOOD),
                new AmmoItem("wooden_556mm", AmmoType.AMMO_556MM, AmmoMaterials.WOOD),
                new AmmoItem("wooden_762mm", AmmoType.AMMO_762MM, AmmoMaterials.WOOD),
                new AmmoItem("wooden_12g", AmmoType.AMMO_12G, AmmoMaterials.WOOD),
                new AmmoItem("wooden_bolt", AmmoType.BOLT, AmmoMaterials.WOOD),
                new AmmoItem("stone_9mm", AmmoType.AMMO_9MM, AmmoMaterials.STONE),
                new AmmoItem("stone_45acp", AmmoType.AMMO_45ACP, AmmoMaterials.STONE),
                new AmmoItem("stone_556mm", AmmoType.AMMO_556MM, AmmoMaterials.STONE),
                new AmmoItem("stone_762mm", AmmoType.AMMO_762MM, AmmoMaterials.STONE),
                new AmmoItem("stone_12g", AmmoType.AMMO_12G, AmmoMaterials.STONE),
                new AmmoItem("stone_bolt", AmmoType.BOLT, AmmoMaterials.STONE),
                new AmmoItem("iron_9mm", AmmoType.AMMO_9MM, AmmoMaterials.IRON),
                new AmmoItem("iron_45acp", AmmoType.AMMO_45ACP, AmmoMaterials.IRON),
                new AmmoItem("iron_556mm", AmmoType.AMMO_556MM, AmmoMaterials.IRON),
                new AmmoItem("iron_762mm", AmmoType.AMMO_762MM, AmmoMaterials.IRON),
                new AmmoItem("iron_12g", AmmoType.AMMO_12G, AmmoMaterials.IRON),
                new AmmoItem("iron_bolt", AmmoType.BOLT, AmmoMaterials.IRON),
                new AmmoItem("lapis_9mm", AmmoType.AMMO_9MM, AmmoMaterials.LAPIS),
                new AmmoItem("lapis_45acp", AmmoType.AMMO_45ACP, AmmoMaterials.LAPIS),
                new AmmoItem("lapis_556mm", AmmoType.AMMO_556MM, AmmoMaterials.LAPIS),
                new AmmoItem("lapis_762mm", AmmoType.AMMO_762MM, AmmoMaterials.LAPIS),
                new AmmoItem("lapis_12g", AmmoType.AMMO_12G, AmmoMaterials.LAPIS),
                new AmmoItem("lapis_bolt", AmmoType.BOLT, AmmoMaterials.LAPIS),
                new AmmoItem("gold_9mm", AmmoType.AMMO_9MM, AmmoMaterials.GOLD),
                new AmmoItem("gold_45acp", AmmoType.AMMO_45ACP, AmmoMaterials.GOLD),
                new AmmoItem("gold_556mm", AmmoType.AMMO_556MM, AmmoMaterials.GOLD),
                new AmmoItem("gold_762mm", AmmoType.AMMO_762MM, AmmoMaterials.GOLD),
                new AmmoItem("gold_12g", AmmoType.AMMO_12G, AmmoMaterials.GOLD),
                new AmmoItem("gold_bolt", AmmoType.BOLT, AmmoMaterials.GOLD),
                new AmmoItem("redstone_9mm", AmmoType.AMMO_9MM, AmmoMaterials.REDSTONE),
                new AmmoItem("redstone_45acp", AmmoType.AMMO_45ACP, AmmoMaterials.REDSTONE),
                new AmmoItem("redstone_556mm", AmmoType.AMMO_556MM, AmmoMaterials.REDSTONE),
                new AmmoItem("redstone_762mm", AmmoType.AMMO_762MM, AmmoMaterials.REDSTONE),
                new AmmoItem("redstone_12g", AmmoType.AMMO_12G, AmmoMaterials.REDSTONE),
                new AmmoItem("redstone_bolt", AmmoType.BOLT, AmmoMaterials.REDSTONE),
                new AmmoItem("emerald_9mm", AmmoType.AMMO_9MM, AmmoMaterials.EMERALD),
                new AmmoItem("emerald_45acp", AmmoType.AMMO_45ACP, AmmoMaterials.EMERALD),
                new AmmoItem("emerald_556mm", AmmoType.AMMO_556MM, AmmoMaterials.EMERALD),
                new AmmoItem("emerald_762mm", AmmoType.AMMO_762MM, AmmoMaterials.EMERALD),
                new AmmoItem("emerald_12g", AmmoType.AMMO_12G, AmmoMaterials.EMERALD),
                new AmmoItem("emerald_bolt", AmmoType.BOLT, AmmoMaterials.EMERALD),
                new AmmoItem("quartz_9mm", AmmoType.AMMO_9MM, AmmoMaterials.QUARTZ),
                new AmmoItem("quartz_45acp", AmmoType.AMMO_45ACP, AmmoMaterials.QUARTZ),
                new AmmoItem("quartz_556mm", AmmoType.AMMO_556MM, AmmoMaterials.QUARTZ),
                new AmmoItem("quartz_762mm", AmmoType.AMMO_762MM, AmmoMaterials.QUARTZ),
                new AmmoItem("quartz_12g", AmmoType.AMMO_12G, AmmoMaterials.QUARTZ),
                new AmmoItem("quartz_bolt", AmmoType.BOLT, AmmoMaterials.QUARTZ),
                new AmmoItem("diamond_9mm", AmmoType.AMMO_9MM, AmmoMaterials.DIAMOND),
                new AmmoItem("diamond_45acp", AmmoType.AMMO_45ACP, AmmoMaterials.DIAMOND),
                new AmmoItem("diamond_556mm", AmmoType.AMMO_556MM, AmmoMaterials.DIAMOND),
                new AmmoItem("diamond_762mm", AmmoType.AMMO_762MM, AmmoMaterials.DIAMOND),
                new AmmoItem("diamond_12g", AmmoType.AMMO_12G, AmmoMaterials.DIAMOND),
                new AmmoItem("diamond_bolt", AmmoType.BOLT, AmmoMaterials.DIAMOND),
                new AmmoItem("amethyst_9mm", AmmoType.AMMO_9MM, AmmoMaterials.AMETHYST),
                new AmmoItem("amethyst_45acp", AmmoType.AMMO_45ACP, AmmoMaterials.AMETHYST),
                new AmmoItem("amethyst_556mm", AmmoType.AMMO_556MM, AmmoMaterials.AMETHYST),
                new AmmoItem("amethyst_762mm", AmmoType.AMMO_762MM, AmmoMaterials.AMETHYST),
                new AmmoItem("amethyst_12g", AmmoType.AMMO_12G, AmmoMaterials.AMETHYST),
                new AmmoItem("amethyst_bolt", AmmoType.BOLT, AmmoMaterials.AMETHYST),
                new AmmoItem("netherite_9mm", AmmoType.AMMO_9MM, AmmoMaterials.NETHERITE),
                new AmmoItem("netherite_45acp", AmmoType.AMMO_45ACP, AmmoMaterials.NETHERITE),
                new AmmoItem("netherite_556mm", AmmoType.AMMO_556MM, AmmoMaterials.NETHERITE),
                new AmmoItem("netherite_762mm", AmmoType.AMMO_762MM, AmmoMaterials.NETHERITE),
                new AmmoItem("netherite_12g", AmmoType.AMMO_12G, AmmoMaterials.NETHERITE),
                new AmmoItem("netherite_bolt", AmmoType.BOLT, AmmoMaterials.NETHERITE),
                new AmmoItem("explosive_grenade_launcher_shell", AmmoType.GRENADE, AmmoMaterials.GRENADE),
                new AmmoItem("high_explosive_grenade_launcher_shell", AmmoType.GRENADE, AmmoMaterials.HE_GRENADE),
                new AmmoItem("impact_grenade_launcher_shell", AmmoType.GRENADE, AmmoMaterials.IMPACT),
                new AmmoItem("sticky_grenade_launcher_shell", AmmoType.GRENADE, AmmoMaterials.STICKY),
                new AmmoItem("tear_gas_grenade_launcher_shell", AmmoType.GRENADE, AmmoMaterials.TEAR_GAS),
                new AmmoItem("rocket", AmmoType.ROCKET, AmmoMaterials.ROCKET),
                new AmmoItem("high_explosive_rocket", AmmoType.ROCKET, AmmoMaterials.HE_ROCKET),
                new AmmoItem("demolition_rocket", AmmoType.ROCKET, AmmoMaterials.DEMOLITION),
                new AmmoItem("napalm_rocket", AmmoType.ROCKET, AmmoMaterials.NAPALM),
                new AmmoItem("toxin_rocket", AmmoType.ROCKET, AmmoMaterials.TOXIN),
                new M1911Item("m1911"),
                new R45Item("r45"),
                new DesertEagleItem("desert_eagle"),
                new Ump45Item("ump45"),
                new ThompsonItem("thompson"),
                new VectorItem("vector"),
                new AkmItem("akm"),
                new Hk416Item("hk416"),
                new AugItem("aug"),
                new SksItem("sks"),
                new VssItem("vss"),
                new Mk14EbrItem("mk14ebr"),
                new Kar98kItem("kar98k"),
                new WinchesterItem("winchester"),
                new AwmItem("awm"),
                new S1897Item("s1897"),
                new S686Item("s686"),
                new S12KItem("s12k"),
                new WoodenCrossbowItem("wooden_crossbow"),
                new ChuKoNuItem("chukonu"),
                new GrenadeLauncherItem("grenade_launcher"),
                new RocketLauncherItem("rocket_launcher"),
                new SlingItem("sling"),
                BaseItem.simpleItem("small_bullet_casing"),
                BaseItem.simpleItem("large_bullet_casing"),
                BaseItem.simpleItem("shotgun_shell"),
                BaseItem.simpleItem("grenade_launcher_shell"),
                BaseItem.simpleItem("rocket_shell"),
                BaseItem.simpleItem("barrel"),
                BaseItem.simpleItem("long_barrel"),
                BaseItem.simpleItem("iron_stock"),
                BaseItem.simpleItem("small_iron_stock"),
                BaseItem.simpleItem("wooden_stock"),
                BaseItem.simpleItem("magazine"),
                BaseItem.simpleItem("gun_parts"),
                BaseItem.simpleItem("bolt_fletching"),
                new GrenadeItem("grenade", 4, false),
                new GrenadeItem("massive_grenade", 6, false),
                new GrenadeItem("impact_grenade", 4, true),
                BaseItem.simpleItem("iron_ore_chunk"),
                BaseItem.simpleItem("gold_ore_chunk"),
                new PointAwardItem("skillpoint_book", IPlayerData::getProgressData),
                new PointAwardItem("perkpoint_book", IPlayerData::getPerkProvider),
                new HammerItem("wooden_hammer", HammerItem.WOOD_HAMMER_MATERIAL),
                new HammerItem("stone_hammer", HammerItem.STONE_HAMMER_MATERIAL),
                new HammerItem("iron_hammer", HammerItem.IRON_HAMMER_MATERIAL),
                BaseItem.simpleItem("gold_egg_shard"),
                BaseItem.simpleItem("blaze_lump"),
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
                new ModFoodItem("chocolate_glazed_apple_pie", ModFoods.CHOCOLATE_GLAZED_APPLE_PIE).buff(player -> player.heal(3.0F)),
                new ModFoodItem("fried_egg", ModFoods.FRIED_EGG),
                new ModFoodItem("fries", ModFoods.FRIES),
                new ModFoodItem("chicken_nuggets", ModFoods.CHICKEN_NUGGETS).buff(player -> player.heal(1f)),
                new ModFoodItem("schnitzel", ModFoods.SCHNITZEL),
                new ModFoodItem("raw_doughnut", ModFoods.RAW_DOUGHNUT),
                new ModFoodItem("doughnut", ModFoods.DOUGHNUT),
                new ModFoodItem("sushi_maki", ModFoods.SUSHI_MAKI).buff(player -> player.heal(1.0F)),
                new CookingOilItem("cooking_oil"),
                new StorageItem("lunch_box", new Item.Properties(), 3, 2, StorageUtil::isFood, LunchBoxContainer::new),
                new StorageItem("ammo_case", new Item.Properties(), 4, 4, StorageUtil::isAmmo, AmmoCaseContainer::new),
                new StorageItem("grenade_case", new Item.Properties(), 4, 3, StorageUtil::isExplosive, GrenadeCaseContainer::new),
                new StorageItem("meds_case", new Item.Properties(), 4, 4, StorageUtil::isMed, MedsCaseContainer::new),
                new StorageItem("item_case", new Item.Properties(), 6, 4, StorageUtil::notAnInventory, ItemCaseContainer::new),
                new WeaponRepairKitItem("weapon_repair_kit"),
                BaseItem.simpleItem("small_stone"),
                new StarterGearItem("starter_gear")
        );
        for (PerkVariant variant : PerkVariant.values()) {
            registry.register(new CrystalItem(variant.getRegistryName("crystal"), variant));
            registry.register(new PerkItem(variant.getRegistryName("orb_of_purity"), variant));
            registry.register(new PerkItem(variant.getRegistryName("orb_of_transmutation"), variant));
        }
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
                sound("m16"),
                sound("kar98k_reload_clip"),
                sound("kar98k_reload_clip_fast"),
                sound("crossbow_reload"),
                sound("crossbow_reload_fast"),
                sound("flare_shoot"),
                sound("second_chance_use"),
                sound("relaxed_2"),
                sound("use_avenge_me_friends"),
                sound("use_well_fed"),
                sound("bullet_whizz"),

                sound("p1911_mag1"),
                sound("p1911_mag2"),
                sound("p1911_slide1"),
                sound("gun_p1911"),
                sound("gun_p1911_silenced"),
                sound("akm_ch1"),
                sound("akm_ch2"),
                sound("akm_mag1"),
                sound("akm_mag2"),
                sound("gun_akm"),
                sound("gun_akm_silenced"),
                sound("aug_ch1"),
                sound("aug_ch2"),
                sound("aug_mag1"),
                sound("aug_mag2"),
                sound("gun_aug"),
                sound("gun_aug_silenced"),
                sound("awm_bolt1"),
                sound("awm_bolt2"),
                sound("awm_bolt3"),
                sound("awm_bolt4"),
                sound("awm_mag1"),
                sound("awm_mag2"),
                sound("gun_awm"),
                sound("gun_awm_silenced"),
                sound("crossbow_arrow1"),
                sound("crossbow_arrow2"),
                sound("crossbow_arrow3"),
                sound("crossbow_shoot"),
                sound("crossbow_string1"),
                sound("deagle_mag1"),
                sound("deagle_mag2"),
                sound("deagle_slide1"),
                sound("gun_deagle"),
                sound("gl_bullet1"),
                sound("gl_bullet2"),
                sound("gl_mag1"),
                sound("gl_mag2"),
                sound("gl_shot1"),
                sound("grenade_cock"),
                sound("grenade_pin"),
                sound("gun_m416"),
                sound("gun_m416_silenced"),
                sound("hk416_ch1"),
                sound("hk416_mag1"),
                sound("hk416_mag2"),
                sound("hk416_mag3"),
                sound("gun_kar98k"),
                sound("gun_kar98k_silenced"),
                sound("kar98k_bolt1"),
                sound("kar98k_bolt2"),
                sound("kar98k_bolt3"),
                sound("kar98k_bolt4"),
                sound("kar98k_bolt5"),
                sound("kar98k_bolt6"),
                sound("kar98k_bolt7"),
                sound("kar98k_bolt8"),
                sound("kar98k_bullet1"),
                sound("kar98k_bullet2"),
                sound("reload_kar98k_single"),
                sound("gun_mk14"),
                sound("gun_mk14_silenced"),
                sound("mk14_ch1"),
                sound("mk14_ch2"),
                sound("mk14_mag1"),
                sound("mk14_mag2"),
                sound("gun_r1895_silenced"),
                sound("gun_r45"),
                sound("r45_mag1"),
                sound("r45_mag2"),
                sound("rl_beep1"),
                sound("rl_bullet"),
                sound("rl_locked1"),
                sound("rl_shot"),
                sound("gun_s12k"),
                sound("gun_s12k_silenced"),
                sound("s12k_ch1"),
                sound("s12k_ch2"),
                sound("s12k_mag1"),
                sound("s12k_mag2"),
                sound("s12k_mag3"),
                sound("bolt_s1897"),
                sound("gun_s1897"),
                sound("reload_s1897"),
                sound("s1897_pump1"),
                sound("s1897_pump2"),
                sound("gun_s686"),
                sound("s686_bar1"),
                sound("s686_bar2"),
                sound("s686_bullet1"),
                sound("gun_sks"),
                sound("gun_sks_silenced"),
                sound("sks_ch1"),
                sound("sks_ch2"),
                sound("sks_mag1"),
                sound("sks_mag2"),
                sound("gun_tommy_gun"),
                sound("gun_tommy_gun_silenced"),
                sound("thompson_ch1"),
                sound("thompson_ch2"),
                sound("thompson_hit1"),
                sound("thompson_mag1"),
                sound("thompson_mag2"),
                sound("gun_ump9"),
                sound("gun_ump9_silenced"),
                sound("ump45_ch1"),
                sound("ump45_ch2"),
                sound("ump45_mag1"),
                sound("ump45_mag2"),
                sound("unjam1"),
                sound("unjam10"),
                sound("unjam11"),
                sound("unjam12"),
                sound("unjam13"),
                sound("unjam14"),
                sound("unjam15"),
                sound("unjam16"),
                sound("unjam17"),
                sound("unjam18"),
                sound("unjam19"),
                sound("unjam2"),
                sound("unjam20"),
                sound("unjam21"),
                sound("unjam22"),
                sound("unjam23"),
                sound("unjam24"),
                sound("unjam25"),
                sound("unjam26"),
                sound("unjam27"),
                sound("unjam28"),
                sound("unjam29"),
                sound("unjam3"),
                sound("unjam30"),
                sound("unjam31"),
                sound("unjam32"),
                sound("unjam33"),
                sound("unjam34"),
                sound("unjam35"),
                sound("unjam36"),
                sound("unjam37"),
                sound("unjam38"),
                sound("unjam39"),
                sound("unjam4"),
                sound("unjam40"),
                sound("unjam41"),
                sound("unjam42"),
                sound("unjam43"),
                sound("unjam44"),
                sound("unjam5"),
                sound("unjam6"),
                sound("unjam7"),
                sound("unjam8"),
                sound("unjam9"),
                sound("gun_vector"),
                sound("gun_vector_silenced"),
                sound("vector_ch1"),
                sound("vector_mag1"),
                sound("vector_mag2"),
                sound("gun_vss"),
                sound("vss_ch1"),
                sound("vss_ch2"),
                sound("vss_mag1"),
                sound("vss_mag2"),
                sound("gun_win94"),
                sound("win_bolt1"),
                sound("win_bolt2"),
                sound("band1"),
                sound("band2"),
                sound("band3"),
                sound("band4"),
                sound("hemo1"),
                sound("med1"),
                sound("med2"),
                sound("pen1"),
                sound("pen2"),
                sound("pen3"),
                sound("pills1"),
                sound("pills2"),
                sound("pills3")
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
