package dev.toma.gunsrpg.common.init;

import dev.toma.gunsrpg.GunsRPG;
import dev.toma.gunsrpg.ModTabs;
import dev.toma.gunsrpg.common.AnimationPaths;
import dev.toma.gunsrpg.common.attribute.Attribs;
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
import dev.toma.gunsrpg.common.skills.*;
import dev.toma.gunsrpg.common.skills.core.DisplayData;
import dev.toma.gunsrpg.common.skills.core.DisplayType;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.skills.core.TransactionValidatorRegistry;
import dev.toma.gunsrpg.util.Constants;
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
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.GUNPOWDER_OUTPUT, Modifiers.GUNPOWDER_I))).description(0).build().setRegistryName("gunpowder_novice"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.GUNPOWDER_OUTPUT, Modifiers.GUNPOWDER_II))).description(0).build().setRegistryName("gunpowder_expert"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.GUNPOWDER_OUTPUT, Modifiers.GUNPOWDER_III))).description(0).build().setRegistryName("gunpowder_master"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.BONEMEAL_OUTPUT, Modifiers.BONEMEAL_I))).description(0).build().setRegistryName("bone_grinder_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.BONEMEAL_OUTPUT, Modifiers.BONEMEAL_II))).description(0).build().setRegistryName("bone_grinder_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.BONEMEAL_OUTPUT, Modifiers.BONEMEAL_III))).description(0).build().setRegistryName("bone_grinder_iii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.BLAZEPOWDER_OUTPUT, Modifiers.BLAZEPOWDER_I))).description(0).build().setRegistryName("blaze_powder_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.BLAZEPOWDER_OUTPUT, Modifiers.BLAZEPOWDER_II))).description(0).build().setRegistryName("blaze_powder_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.BLAZEPOWDER_OUTPUT, Modifiers.BLAZEPOWDER_III))).description(0).build().setRegistryName("blaze_powder_iii"),
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
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("ammo_smithing_mastery"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("gun_parts_smith"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.M1911))).build().setRegistryName("m1911_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.UMP45))).build().setRegistryName("ump45_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.WOODEN_CROSSBOW))).build().setRegistryName("crossbow_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.S1897))).build().setRegistryName("s1897_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.SKS))).build().setRegistryName("sks_assembly"),
                SkillType.Builder.create(BasicSkill::new).render(type -> DisplayData.create(DisplayType.ITEM, new ItemStack(ModItems.KAR98K))).build().setRegistryName("kar98k_assembly"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("grenades"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("massive_grenades"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("impact_grenades"),
                // TODO grenade launcher lvl 40, rocket launcher 60
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("medic"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("doctor"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("efficient_meds"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.POISON_DELAY, Modifiers.DEBUFF_DELAY_I), AttributeSkill.IAttributeTarget.of(Attribs.POISON_RESISTANCE, Modifiers.DEBUFF_RESIST_I))).description(0).build().setRegistryName("poison_resistance_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.POISON_DELAY, Modifiers.DEBUFF_DELAY_II), AttributeSkill.IAttributeTarget.of(Attribs.POISON_RESISTANCE, Modifiers.DEBUFF_RESIST_II))).description(0).build().setRegistryName("poison_resistance_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.POISON_DELAY, Modifiers.DEBUFF_DELAY_III), AttributeSkill.IAttributeTarget.of(Attribs.POISON_RESISTANCE, Modifiers.DEBUFF_RESIST_III))).description(0).build().setRegistryName("poison_resistance_iii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.INFECTION_DELAY, Modifiers.DEBUFF_DELAY_I), AttributeSkill.IAttributeTarget.of(Attribs.INFECTION_RESISTANCE, Modifiers.DEBUFF_RESIST_I))).description(0).build().setRegistryName("infection_resistance_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.INFECTION_DELAY, Modifiers.DEBUFF_DELAY_II), AttributeSkill.IAttributeTarget.of(Attribs.INFECTION_RESISTANCE, Modifiers.DEBUFF_RESIST_II))).description(0).build().setRegistryName("infection_resistance_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.INFECTION_DELAY, Modifiers.DEBUFF_DELAY_III), AttributeSkill.IAttributeTarget.of(Attribs.INFECTION_RESISTANCE, Modifiers.DEBUFF_RESIST_III))).description(0).build().setRegistryName("infection_resistance_iii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.FRACTURE_DELAY, Modifiers.DEBUFF_DELAY_I), AttributeSkill.IAttributeTarget.of(Attribs.FRACTURE_RESISTANCE, Modifiers.DEBUFF_RESIST_I))).description(0).build().setRegistryName("fracture_resistance_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.FRACTURE_DELAY, Modifiers.DEBUFF_DELAY_II), AttributeSkill.IAttributeTarget.of(Attribs.FRACTURE_RESISTANCE, Modifiers.DEBUFF_RESIST_II))).description(0).build().setRegistryName("fracture_resistance_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.FRACTURE_DELAY, Modifiers.DEBUFF_DELAY_III), AttributeSkill.IAttributeTarget.of(Attribs.FRACTURE_RESISTANCE, Modifiers.DEBUFF_RESIST_III))).description(0).build().setRegistryName("fracture_resistance_iii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.BLEED_DELAY, Modifiers.DEBUFF_DELAY_I), AttributeSkill.IAttributeTarget.of(Attribs.FRACTURE_RESISTANCE, Modifiers.DEBUFF_RESIST_I))).description(0).build().setRegistryName("bleeding_resistance_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.BLEED_DELAY, Modifiers.DEBUFF_DELAY_II), AttributeSkill.IAttributeTarget.of(Attribs.FRACTURE_RESISTANCE, Modifiers.DEBUFF_RESIST_II))).description(0).build().setRegistryName("bleeding_resistance_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.BLEED_DELAY, Modifiers.DEBUFF_DELAY_III), AttributeSkill.IAttributeTarget.of(Attribs.FRACTURE_RESISTANCE, Modifiers.DEBUFF_RESIST_III))).description(0).build().setRegistryName("bleeding_resistance_iii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.FALL_RESISTANCE, Modifiers.ACROBATICS_FALL_I), AttributeSkill.IAttributeTarget.of(Attribs.EXPLOSION_RESISTANCE, Modifiers.ACROBATICS_EXPLOSION_I))).description(0).build().setRegistryName("acrobatics_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.FALL_RESISTANCE, Modifiers.ACROBATICS_FALL_II), AttributeSkill.IAttributeTarget.of(Attribs.EXPLOSION_RESISTANCE, Modifiers.ACROBATICS_EXPLOSION_II))).description(0).build().setRegistryName("acrobatics_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.FALL_RESISTANCE, Modifiers.ACROBATICS_FALL_III), AttributeSkill.IAttributeTarget.of(Attribs.EXPLOSION_RESISTANCE, Modifiers.ACROBATICS_EXPLOSION_III))).description(0).build().setRegistryName("acrobatics_iii"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("pharmacist_i"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("pharmacist_ii"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("pharmacist_iii"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("pharmacist_iv"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("pharmacist_v"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.WOODCUTTING_SPEED, Modifiers.CHOPPING_I))).description(0).build().setRegistryName("sharp_axe_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.WOODCUTTING_SPEED, Modifiers.CHOPPING_II))).description(0).build().setRegistryName("sharp_axe_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.WOODCUTTING_SPEED, Modifiers.CHOPPING_III))).description(0).build().setRegistryName("sharp_axe_iii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.WOODCUTTING_SPEED, Modifiers.CHOPPING_IV))).description(0).build().setRegistryName("sharp_axe_iv"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.WOODCUTTING_SPEED, Modifiers.CHOPPING_V))).description(0).build().setRegistryName("sharp_axe_v"),
                SkillType.Builder.<LumberjackSkill>create(type -> new LumberjackSkill(type, 1)).description((c, type) -> SkillUtil.Localizations.localizeWithInternalData(c, type, LumberjackSkill::generateDescription)).build().setRegistryName("lumberjack_i"),
                SkillType.Builder.<LumberjackSkill>create(type -> new LumberjackSkill(type, 2)).description((c, type) -> SkillUtil.Localizations.localizeWithInternalData(c, type, LumberjackSkill::generateDescription)).build().setRegistryName("lumberjack_ii"),
                SkillType.Builder.<LumberjackSkill>create(type -> new LumberjackSkill(type, 3)).description((c, type) -> SkillUtil.Localizations.localizeWithInternalData(c, type, LumberjackSkill::generateDescription)).build().setRegistryName("lumberjack_iii"),
                SkillType.Builder.<LumberjackSkill>create(type -> new LumberjackSkill(type, 4)).description((c, type) -> SkillUtil.Localizations.localizeWithInternalData(c, type, LumberjackSkill::generateDescription)).build().setRegistryName("lumberjack_iv"),
                SkillType.Builder.<LumberjackSkill>create(type -> new LumberjackSkill(type, 5)).description((c, type) -> SkillUtil.Localizations.localizeWithInternalData(c, type, LumberjackSkill::generateDescription)).build().setRegistryName("lumberjack_v"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.DIGGING_SPEED, Modifiers.DIGGING_I))).description(0).build().setRegistryName("grave_digger_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.DIGGING_SPEED, Modifiers.DIGGING_II))).description(0).build().setRegistryName("grave_digger_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.DIGGING_SPEED, Modifiers.DIGGING_III))).description(0).build().setRegistryName("grave_digger_iii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.DIGGING_SPEED, Modifiers.DIGGING_IV))).description(0).build().setRegistryName("grave_digger_iv"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.DIGGING_SPEED, Modifiers.DIGGING_V))).description(0).build().setRegistryName("grave_digger_v"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.MINING_SPEED, Modifiers.MINING_I))).description(0).build().setRegistryName("heavy_pickaxe_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.MINING_SPEED, Modifiers.MINING_II))).description(0).build().setRegistryName("heavy_pickaxe_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.MINING_SPEED, Modifiers.MINING_III))).description(0).build().setRegistryName("heavy_pickaxe_iii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.MINING_SPEED, Modifiers.MINING_IV))).description(0).build().setRegistryName("heavy_pickaxe_iv"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.MINING_SPEED, Modifiers.MINING_V))).description(0).build().setRegistryName("heavy_pickaxe_v"),
                SkillType.Builder.<MotherlodeSkill>create(type -> new MotherlodeSkill(type, 1)).description((l, type) -> SkillUtil.Localizations.localizeWithInternalData(l, type, MotherlodeSkill::generateDescription)).build().setRegistryName("mother_lode_i"),
                SkillType.Builder.<MotherlodeSkill>create(type -> new MotherlodeSkill(type, 2)).description((l, type) -> SkillUtil.Localizations.localizeWithInternalData(l, type, MotherlodeSkill::generateDescription)).build().setRegistryName("mother_lode_ii"),
                SkillType.Builder.<MotherlodeSkill>create(type -> new MotherlodeSkill(type, 3)).description((l, type) -> SkillUtil.Localizations.localizeWithInternalData(l, type, MotherlodeSkill::generateDescription)).build().setRegistryName("mother_lode_iii"),
                SkillType.Builder.<MotherlodeSkill>create(type -> new MotherlodeSkill(type, 4)).description((l, type) -> SkillUtil.Localizations.localizeWithInternalData(l, type, MotherlodeSkill::generateDescription)).build().setRegistryName("mother_lode_iv"),
                SkillType.Builder.<MotherlodeSkill>create(type -> new MotherlodeSkill(type, 5)).description((l, type) -> SkillUtil.Localizations.localizeWithInternalData(l, type, MotherlodeSkill::generateDescription)).build().setRegistryName("mother_lode_v"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("hammer_i"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("hammer_ii"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("hammer_iii"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("blacksmith"),
                SkillType.Builder.create(BasicSkill::new).build().setRegistryName("mineralogist"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.MELEE_DAMAGE, Modifiers.DAMAGE_I))).description(0).build().setRegistryName("strong_muscles_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.MELEE_DAMAGE, Modifiers.DAMAGE_II))).description(0).build().setRegistryName("strong_muscles_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.MELEE_DAMAGE, Modifiers.DAMAGE_III))).description(0).build().setRegistryName("strong_muscles_iii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.MOVEMENT_SPEED, Modifiers.AGILITY_I))).description(0).build().setRegistryName("agility_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.MOVEMENT_SPEED, Modifiers.AGILITY_II))).description(0).build().setRegistryName("agility_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.MOVEMENT_SPEED, Modifiers.AGILITY_III))).description(0).build().setRegistryName("agility_iii"),
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
                SkillType.Builder.create(GodHelpUsSkill::new).description(2).build().setRegistryName("god_help_us"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.INSTANT_KILL, Modifiers.INSTANT_KILL_I))).description(0).build().setRegistryName("skull_crusher_i"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.INSTANT_KILL, Modifiers.INSTANT_KILL_II))).description(0).build().setRegistryName("skull_crusher_ii"),
                SkillType.Builder.<AttributeSkill>create(type -> new AttributeSkill(type, AttributeSkill.IAttributeTarget.of(Attribs.INSTANT_KILL, Modifiers.INSTANT_KILL_III))).description(0).build().setRegistryName("skull_crusher_iii"),
                SkillType.Builder.create(LightHunterSkill::new).description(4).build().setRegistryName("light_hunter"),
                SkillType.Builder.<LikeACatSkill>create(type -> new LikeACatSkill(type, Interval.minutes(5), Interval.minutes(2))).build().setRegistryName("like_a_cat_i"),
                SkillType.Builder.<LikeACatSkill>create(type -> new LikeACatSkill(type, Interval.minutes(5), Interval.minutes(4))).build().setRegistryName("like_a_cat_ii"),
                SkillType.Builder.<LikeACatSkill>create(type -> new LikeACatSkill(type, Interval.minutes(5), Interval.minutes(7))).build().setRegistryName("like_a_cat_iii"),
                SkillType.Builder.create(BasicSkill::new).description(3).build().setRegistryName("avenge_me_friends"),
                SkillType.Builder.create(WarMachineSkill::new).build().setRegistryName("war_machine"),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("m1911_quickdraw").iconPathNormal("quickdraw").build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("m1911_extended").iconPathNormal("extended").build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("m1911_tough_spring").iconPathNormal("tough_spring").requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("m1911_carbon_barrel").requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("m1911_suppressor").iconPathNormal("suppressor").requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("m1911_heavy_bullets").descriptionLength(3).requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("m1911_dual_wield").requiredLevel(7).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ump45_quickdraw").iconPathNormal("quickdraw").build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ump45_extended").iconPathNormal("extended").build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ump45_vertical_grip").iconPathNormal("vertical_grip").build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ump45_tough_spring").iconPathNormal("tough_spring").requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ump45_red_dot").iconPathNormal("red_dot").requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ump45_suppressor").iconPathNormal("suppressor").requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("ump45_commando").descriptionLength(3).requiredLevel(7).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_quiver").build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_poisoned_bolts").build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_hunter").build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_tough_bowstring").requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_penetrator").iconPathNormal("penetrator").requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_scope").iconPathNormal("scope").requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("crossbow_repeater").descriptionLength(2).requiredLevel(7).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("s1897_bullet_loops").build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("s1897_extended").iconPathNormal("extended").build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("s1897_pump_in_action").requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("s1897_choke").requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("s1897_never_give_up").requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("s1897_extended_barrel").requiredLevel(7).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sks_tough_spring").iconPathNormal("tough_spring").build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sks_vertical_grip").iconPathNormal("vertical_grip").build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sks_extended").iconPathNormal("extended").requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sks_red_dot").iconPathNormal("red_dot").requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sks_suppressor").iconPathNormal("suppressor").requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sks_cheekpad").iconPathNormal("cheekpad").requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("sks_adaptive_chambering").descriptionLength(2).requiredLevel(7).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("kar98k_scope").iconPathNormal("scope").build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("kar98k_cheekpad").iconPathNormal("cheekpad").build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("kar98k_extended").iconPathNormal("extended").requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("kar98k_suppressor").iconPathNormal("suppressor").requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("kar98k_fast_hands").requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("kar98k_penetrator").iconPathNormal("penetrator").requiredLevel(5).build(),
                SkillType.Builder.create(BasicSkill::new).setAttachmentCategory().setRegistryName("kar98k_dead_eye").requiredLevel(7).build()
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
                new TrapBlock("hidden_landmine", AbstractBlock.Properties.of(Material.METAL).noOcclusion(), new TrapBlock.MineReaction(3.0F)),
                new BaseBlock("crystal_fuse", AbstractBlock.Properties.of(Material.STONE).noOcclusion()),
                new BaseBlock("crystal_purification", AbstractBlock.Properties.of(Material.STONE).noOcclusion())
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
                AttributeAccessHealItem.define("hemostat")
                        .modifyAttributes(Attribs.BLEED_BLOCK, Constants.ModifierIds.MED_BLEEDING_BLOCK, Attribs.BLEED_DELAY, Constants.ModifierIds.MED_BLEEDING_DELAY)
                        .defineSound(() -> ModSounds.USE_VACCINE)
                        .describe("Bleeding:", "Disabled for 60 seconds", "Spread speed: -30%")
                        .animate(40, AnimationPaths.HEMOSTAT)
                        .build(),
                AttributeAccessHealItem.define("vitamins")
                        .modifyAttributes(Attribs.INFECTION_BLOCK, Constants.ModifierIds.MED_INFECTION_BLOCK, Attribs.INFECTION_DELAY, Constants.ModifierIds.MED_INFECTION_DELAY)
                        .defineSound(() -> ModSounds.USE_ANTIDOTUM_PILLS)
                        .describe("Infection:", "Disabled for 60 seconds", "Spread speed: -30%")
                        .animate(30, AnimationPaths.VITAMINS)
                        .build(),
                AttributeAccessHealItem.define("propital")
                        .modifyAttributes(Attribs.FRACTURE_BLOCK, Constants.ModifierIds.MED_FRACTURE_BLOCK, Attribs.FRACTURE_DELAY, Constants.ModifierIds.MED_FRACTURE_DELAY)
                        .defineSound(() -> ModSounds.USE_VACCINE)
                        .describe("Fracture:", "Disabled for 60 seconds", "Spread speed: -30%")
                        .animate(30, AnimationPaths.STIM)
                        .build(),
                AttributeAccessHealItem.define("calcium_shot")
                        .modifyAttributes(Attribs.POISON_BLOCK, Constants.ModifierIds.MED_POISON_BLOCK, Attribs.POISON_DELAY, Constants.ModifierIds.MED_POISON_DELAY)
                        .defineSound(() -> ModSounds.USE_ANTIDOTUM_PILLS)
                        .describe("Poison:", "Disabled for 60 seconds", "Spread speed: -30%")
                        .animate(30, AnimationPaths.STIM)
                        .build(),
                ContinuousHealingItem.define("ufak")
                        .uses(65)
                        .prepareIn(50)
                        .defineSound(() -> ModSounds.USE_BANDAGE)
                        .canUse(player -> player.getHealth() < player.getMaxHealth())
                        .onUse(player -> player.heal(1))
                        .describe("Recovers 0.5 hearts")
                        .animate(20, AnimationPaths.UFAK)
                        .build(),
                ContinuousHealingItem.define("kodiak")
                        .uses(150)
                        .prepareIn(50)
                        .defineSound(() -> ModSounds.USE_BANDAGE)
                        .canUse(player -> player.getHealth() < player.getMaxHealth())
                        .onUse(player -> player.heal(1))
                        .describe("Recovers 0.5 hearts")
                        .animate(30, AnimationPaths.KODIAK)
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
                        .describe("Recovers 7 hearts", "Effects:", "Regeneration II for 15 seconds", "Strength II for 45 seconds",
                                "Resistance I for 60 seconds")
                        .animate(32, AnimationPaths.INJECTION)
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
                new AmmoItem("diamond_9mm", AmmoType.AMMO_9MM, AmmoMaterials.DIAMOND),
                new AmmoItem("diamond_45acp", AmmoType.AMMO_45ACP, AmmoMaterials.DIAMOND),
                new AmmoItem("diamond_556mm", AmmoType.AMMO_556MM, AmmoMaterials.DIAMOND),
                new AmmoItem("diamond_762mm", AmmoType.AMMO_762MM, AmmoMaterials.DIAMOND),
                new AmmoItem("diamond_12g", AmmoType.AMMO_12G, AmmoMaterials.DIAMOND),
                new AmmoItem("diamond_bolt", AmmoType.BOLT, AmmoMaterials.DIAMOND),
                new AmmoItem("quartz_9mm", AmmoType.AMMO_9MM, AmmoMaterials.QUARTZ),
                new AmmoItem("quartz_45acp", AmmoType.AMMO_45ACP, AmmoMaterials.QUARTZ),
                new AmmoItem("quartz_556mm", AmmoType.AMMO_556MM, AmmoMaterials.QUARTZ),
                new AmmoItem("quartz_762mm", AmmoType.AMMO_762MM, AmmoMaterials.QUARTZ),
                new AmmoItem("quartz_12g", AmmoType.AMMO_12G, AmmoMaterials.QUARTZ),
                new AmmoItem("quartz_bolt", AmmoType.BOLT, AmmoMaterials.QUARTZ),
                new AmmoItem("emerald_9mm", AmmoType.AMMO_9MM, AmmoMaterials.EMERALD),
                new AmmoItem("emerald_45acp", AmmoType.AMMO_45ACP, AmmoMaterials.EMERALD),
                new AmmoItem("emerald_556mm", AmmoType.AMMO_556MM, AmmoMaterials.EMERALD),
                new AmmoItem("emerald_762mm", AmmoType.AMMO_762MM, AmmoMaterials.EMERALD),
                new AmmoItem("emerald_12g", AmmoType.AMMO_12G, AmmoMaterials.EMERALD),
                new AmmoItem("emerald_bolt", AmmoType.BOLT, AmmoMaterials.EMERALD),
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
                BaseItem.basic("small_bullet_casing"),
                BaseItem.basic("large_bullet_casing"),
                BaseItem.basic("shotgun_shell"),
                BaseItem.basic("grenade_launcher_shell"),
                BaseItem.basic("rocket_shell"),
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
                new ModFoodItem("chocolate_glazed_apple_pie", ModFoods.CHOCOLATE_GLAZED_APPLE_PIE).buff(player -> player.heal(3.0F)),
                new StorageItem("lunch_box", new Item.Properties(), 3, 2, StorageUtil::isFood, LunchBoxContainer::new),
                new StorageItem("ammo_case", new Item.Properties(), 4, 4, StorageUtil::isAmmo, AmmoCaseContainer::new),
                new StorageItem("grenade_case", new Item.Properties(), 4, 3, StorageUtil::isExplosive, GrenadeCaseContainer::new),
                new StorageItem("meds_case", new Item.Properties(), 4, 4, StorageUtil::isMed, MedsCaseContainer::new),
                new StorageItem("item_case", new Item.Properties(), 6, 4, StorageUtil::notAnInventory, ItemCaseContainer::new),
                new WeaponRepairKitItem("weapon_repair_kit")
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
