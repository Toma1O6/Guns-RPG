package dev.toma.gunsrpg.util.recipes;

import dev.toma.gunsrpg.common.ModRegistry;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.tileentity.TileEntitySmithingTable;
import dev.toma.gunsrpg.util.ModUtils;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SmithingTableRecipes {

    private static final List<SmithingRecipe> RECIPES = new ArrayList<>();

    /*
    0 1 2
    3 4 5
    6 7 8
     */
    public static void register() {
        register(new RecipeBuilder().outputs(Items.GUNPOWDER, SkillUtil::getGunpowderCraftAmount).withIngredient(new int[]{0, 4}, 1, Items.COAL).withIngredient(1, Items.DYE, 15).withIngredient(3, Items.SUGAR).requires(ModRegistry.Skills.GUNPOWDER_NOVICE).asRecipe());
        register(new RecipeBuilder().outputs(new ItemStack(Items.DYE, 1, 15), SkillUtil::getBonemealCraftAmount).withIngredient(0, Items.BONE).requires(ModRegistry.Skills.BONE_GRINDER_I).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.LARGE_BULLET_CASING).withIngredient(new int[]{0, 1}, ModRegistry.GRPGItems.SMALL_BULLET_CASING).requires(ModRegistry.Skills.WOODEN_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.SHOTGUN_SHELL).withIngredient(0, Items.DYE, 1).withIngredient(1, ModRegistry.GRPGItems.LARGE_BULLET_CASING).withIngredient(2, Items.PAPER).requires(ModRegistry.Skills.WOODEN_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.BOLT_FLETCHING).withIngredient(0, Items.IRON_NUGGET).withIngredient(3, Items.FEATHER).withIngredient(6, Items.GOLD_NUGGET).requires(ModRegistry.Skills.WOODEN_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.SMALL_BULLET_CASING, 16).withIngredient(0, Items.COAL, 0).withIngredient(1, Items.IRON_INGOT).withIngredient(2, Items.GOLD_INGOT).requires(ModRegistry.Skills.WOODEN_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.WOODEN_AMMO_9MM, SkillUtil::getAmmoAmount).withIngredient(0, Blocks.PLANKS).withIngredient(3, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).requires(ModRegistry.Skills.WOODEN_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.STONE_AMMO_9MM, SkillUtil::getAmmoAmount).withIngredient(0, Blocks.STONE).withIngredient(3, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).requires(ModRegistry.Skills.STONE_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.IRON_AMMO_9MM, SkillUtil::getAmmoAmount).withIngredient(0, Items.IRON_INGOT).withIngredient(3, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).requires(ModRegistry.Skills.IRON_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.GOLD_AMMO_9MM, SkillUtil::getAmmoAmount).withIngredient(0, Items.GOLD_INGOT).withIngredient(3, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).requires(ModRegistry.Skills.GOLD_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.DIAMOND_AMMO_9MM, SkillUtil::getAmmoAmount).withIngredient(0, Items.DIAMOND).withIngredient(3, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).requires(ModRegistry.Skills.DIAMOND_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.EMERALD_AMMO_9MM, SkillUtil::getAmmoAmount).withIngredient(0, Items.EMERALD).withIngredient(3, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).requires(ModRegistry.Skills.EMERALD_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.AMETHYST_AMMO_9MM, SkillUtil::getAmmoAmount).withIngredient(0, ModRegistry.GRPGItems.AMETHYST).withIngredient(3, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).requires(ModRegistry.Skills.AMETHYST_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.WOODEN_AMMO_45ACP, SkillUtil::getAmmoAmount).withIngredient(0, Blocks.PLANKS).withIngredient(new int[]{3, 4}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).requires(ModRegistry.Skills.WOODEN_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.STONE_AMMO_45ACP, SkillUtil::getAmmoAmount).withIngredient(0, Blocks.STONE).withIngredient(new int[]{3, 4}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).requires(ModRegistry.Skills.STONE_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.IRON_AMMO_45ACP, SkillUtil::getAmmoAmount).withIngredient(0, Items.IRON_INGOT).withIngredient(new int[]{3, 4}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).requires(ModRegistry.Skills.IRON_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.GOLD_AMMO_45ACP, SkillUtil::getAmmoAmount).withIngredient(0, Items.GOLD_INGOT).withIngredient(new int[]{3, 4}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).requires(ModRegistry.Skills.GOLD_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.DIAMOND_AMMO_45ACP, SkillUtil::getAmmoAmount).withIngredient(0, Items.DIAMOND).withIngredient(new int[]{3, 4}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).requires(ModRegistry.Skills.DIAMOND_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.EMERALD_AMMO_45ACP, SkillUtil::getAmmoAmount).withIngredient(0, Items.EMERALD).withIngredient(new int[]{3, 4}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).requires(ModRegistry.Skills.EMERALD_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.AMETHYST_AMMO_45ACP, SkillUtil::getAmmoAmount).withIngredient(0, ModRegistry.GRPGItems.AMETHYST).withIngredient(new int[]{3, 4}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SMALL_BULLET_CASING).requires(ModRegistry.Skills.AMETHYST_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.WOODEN_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).withIngredient(0, Blocks.PLANKS).withIngredient(3, Items.STICK).withIngredient(6, ModRegistry.GRPGItems.BOLT_FLETCHING).requires(ModRegistry.Skills.WOODEN_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.STONE_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).withIngredient(0, Blocks.STONE).withIngredient(3, Items.STICK).withIngredient(6, ModRegistry.GRPGItems.BOLT_FLETCHING).requires(ModRegistry.Skills.STONE_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.IRON_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).withIngredient(0, Items.IRON_INGOT).withIngredient(3, Items.STICK).withIngredient(6, ModRegistry.GRPGItems.BOLT_FLETCHING).requires(ModRegistry.Skills.IRON_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.GOLD_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).withIngredient(0, Items.GOLD_INGOT).withIngredient(3, Items.STICK).withIngredient(6, ModRegistry.GRPGItems.BOLT_FLETCHING).requires(ModRegistry.Skills.GOLD_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.DIAMOND_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).withIngredient(0, Items.DIAMOND).withIngredient(3, Items.STICK).withIngredient(6, ModRegistry.GRPGItems.BOLT_FLETCHING).requires(ModRegistry.Skills.DIAMOND_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.EMERALD_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).withIngredient(0, Items.EMERALD).withIngredient(3, Items.STICK).withIngredient(6, ModRegistry.GRPGItems.BOLT_FLETCHING).requires(ModRegistry.Skills.EMERALD_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.AMETHYST_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).withIngredient(0, ModRegistry.GRPGItems.AMETHYST).withIngredient(3, Items.STICK).withIngredient(6, ModRegistry.GRPGItems.BOLT_FLETCHING).requires(ModRegistry.Skills.AMETHYST_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.WOODEN_AMMO_12G, SkillUtil::getAmmoAmount).withIngredient(0, Blocks.PLANKS).withIngredient(new int[]{3, 4, 5}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SHOTGUN_SHELL).requires(ModRegistry.Skills.WOODEN_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.STONE_AMMO_12G, SkillUtil::getAmmoAmount).withIngredient(0, Blocks.STONE).withIngredient(new int[]{3, 4, 5}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SHOTGUN_SHELL).requires(ModRegistry.Skills.STONE_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.IRON_AMMO_12G, SkillUtil::getAmmoAmount).withIngredient(0, Items.IRON_INGOT).withIngredient(new int[]{3, 4, 5}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SHOTGUN_SHELL).requires(ModRegistry.Skills.IRON_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.GOLD_AMMO_12G, SkillUtil::getAmmoAmount).withIngredient(0, Items.GOLD_INGOT).withIngredient(new int[]{3, 4, 5}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SHOTGUN_SHELL).requires(ModRegistry.Skills.GOLD_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.DIAMOND_AMMO_12G, SkillUtil::getAmmoAmount).withIngredient(0, Items.DIAMOND).withIngredient(new int[]{3, 4, 5}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SHOTGUN_SHELL).requires(ModRegistry.Skills.DIAMOND_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.EMERALD_AMMO_12G, SkillUtil::getAmmoAmount).withIngredient(0, Items.EMERALD).withIngredient(new int[]{3, 4, 5}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SHOTGUN_SHELL).requires(ModRegistry.Skills.EMERALD_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.AMETHYST_AMMO_12G, SkillUtil::getAmmoAmount).withIngredient(0, ModRegistry.GRPGItems.AMETHYST).withIngredient(new int[]{3, 4, 5}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.SHOTGUN_SHELL).requires(ModRegistry.Skills.AMETHYST_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.WOODEN_AMMO_556MM, SkillUtil::getAmmoAmount).withIngredient(0, Blocks.PLANKS).withIngredient(new int[]{3, 4}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).requires(ModRegistry.Skills.WOODEN_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.STONE_AMMO_556MM, SkillUtil::getAmmoAmount).withIngredient(0, Blocks.STONE).withIngredient(new int[]{3, 4}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).requires(ModRegistry.Skills.STONE_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.IRON_AMMO_556MM, SkillUtil::getAmmoAmount).withIngredient(0, Items.IRON_INGOT).withIngredient(new int[]{3, 4}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).requires(ModRegistry.Skills.IRON_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.GOLD_AMMO_556MM, SkillUtil::getAmmoAmount).withIngredient(0, Items.GOLD_INGOT).withIngredient(new int[]{3, 4}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).requires(ModRegistry.Skills.GOLD_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.DIAMOND_AMMO_556MM, SkillUtil::getAmmoAmount).withIngredient(0, Items.DIAMOND).withIngredient(new int[]{3, 4}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).requires(ModRegistry.Skills.DIAMOND_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.EMERALD_AMMO_556MM, SkillUtil::getAmmoAmount).withIngredient(0, Items.EMERALD).withIngredient(new int[]{3, 4}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).requires(ModRegistry.Skills.EMERALD_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.AMETHYST_AMMO_556MM, SkillUtil::getAmmoAmount).withIngredient(0, ModRegistry.GRPGItems.AMETHYST).withIngredient(new int[]{3, 4}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).requires(ModRegistry.Skills.AMETHYST_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.WOODEN_AMMO_762MM, SkillUtil::getAmmoAmount).withIngredient(0, Blocks.PLANKS).withIngredient(new int[]{3, 4, 5}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).requires(ModRegistry.Skills.WOODEN_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.STONE_AMMO_762MM, SkillUtil::getAmmoAmount).withIngredient(0, Blocks.STONE).withIngredient(new int[]{3, 4, 5}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).requires(ModRegistry.Skills.STONE_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.IRON_AMMO_762MM, SkillUtil::getAmmoAmount).withIngredient(0, Items.IRON_INGOT).withIngredient(new int[]{3, 4, 5}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).requires(ModRegistry.Skills.IRON_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.GOLD_AMMO_762MM, SkillUtil::getAmmoAmount).withIngredient(0, Items.GOLD_INGOT).withIngredient(new int[]{3, 4, 5}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).requires(ModRegistry.Skills.GOLD_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.DIAMOND_AMMO_762MM, SkillUtil::getAmmoAmount).withIngredient(0, Items.DIAMOND).withIngredient(new int[]{3, 4, 5}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).requires(ModRegistry.Skills.DIAMOND_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.EMERALD_AMMO_762MM, SkillUtil::getAmmoAmount).withIngredient(0, Items.EMERALD).withIngredient(new int[]{3, 4, 5}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).requires(ModRegistry.Skills.EMERALD_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.AMETHYST_AMMO_762MM, SkillUtil::getAmmoAmount).withIngredient(0, ModRegistry.GRPGItems.AMETHYST).withIngredient(new int[]{3, 4, 5}, Items.GUNPOWDER).withIngredient(6, ModRegistry.GRPGItems.LARGE_BULLET_CASING).requires(ModRegistry.Skills.AMETHYST_AMMO_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.GRENADE).withIngredient(new int[]{1, 3, 5, 7}, Items.IRON_INGOT).withIngredient(4, Blocks.TNT).withIngredient(2, Items.FLINT_AND_STEEL).requires(ModRegistry.Skills.GRENADES).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.MASSIVE_GRENADE).withIngredient(new int[]{0, 2, 3, 5, 6, 7, 8}, Items.GOLD_NUGGET).withIngredient(1, Blocks.TNT).withIngredient(4, ModRegistry.GRPGItems.GRENADE).requires(ModRegistry.Skills.MASSIVE_GRENADES).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.IMPACT_GRENADE).withIngredient(new int[]{0, 2, 6, 8}, Items.REDSTONE).withIngredient(new int[]{3, 7, 5}, Items.GLOWSTONE_DUST).withIngredient(1, Items.BLAZE_POWDER).withIngredient(4, ModRegistry.GRPGItems.GRENADE).requires(ModRegistry.Skills.IMPACT_GRENADES).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.WOODEN_HAMMER).withIngredient(new int[]{3, 0, 1, 2, 5}, Blocks.LOG).withIngredient(new int[]{4, 7}, Items.STICK).requires(ModRegistry.Skills.HAMMER_I, ModRegistry.Skills.HEAVY_PICKAXE_II, ModRegistry.Skills.MOTHER_LODE_II).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.STONE_HAMMER).withIngredient(new int[]{3, 0, 1, 2, 5}, Blocks.STONE).withIngredient(new int[]{4, 7}, Items.STICK).requires(ModRegistry.Skills.HAMMER_II, ModRegistry.Skills.HEAVY_PICKAXE_III, ModRegistry.Skills.MOTHER_LODE_III).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.IRON_HAMMER).withIngredient(new int[]{3, 0, 1, 2, 5}, Items.IRON_INGOT).withIngredient(new int[]{4, 7}, Items.STICK).requires(ModRegistry.Skills.HAMMER_III, ModRegistry.Skills.HEAVY_PICKAXE_IV, ModRegistry.Skills.MOTHER_LODE_IV).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.AMETHYST).withIngredient(1, Items.DIAMOND).withIngredient(new int[]{3, 5}, Items.QUARTZ).withIngredient(4, Items.BLAZE_POWDER).withIngredient(7, Items.EMERALD).requires(ModRegistry.Skills.MINERALOGIST).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.PLASTER_CAST, 2).withIngredient(new int[]{0, 2, 6, 8}, Items.DYE, 15).withIngredient(new int[]{1, 3, 5, 7}, Blocks.CARPET).withIngredient(4, Items.DIAMOND).requires(ModRegistry.Skills.MEDIC).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.BANDAGE, 2).withIngredient(new int[]{0, 8}, Items.DYE, 4).withIngredient(new int[]{1, 3, 5, 7}, Items.PAPER).withIngredient(new int[]{2, 6}, Items.SLIME_BALL).withIngredient(4, new ItemStack(Blocks.WOOL, 1, 0)).requires(ModRegistry.Skills.MEDIC).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.VACCINE).withIngredient(8, Items.GOLDEN_APPLE).withIngredient(6, Items.SPECKLED_MELON).withIngredient(4, ModRegistry.GRPGItems.AMETHYST).withIngredient(2, Items.GOLDEN_CARROT).withIngredient(0, Items.IRON_INGOT).withIngredient(new int[]{1, 3, 5, 7}, Blocks.GLASS_PANE).requires(ModRegistry.Skills.DOCTOR).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.ANTIDOTUM_PILLS, 2).withIngredient(new int[]{0, 2}, Items.PAPER).withIngredient(1, Items.SUGAR).withIngredient(3, new ItemStack(Blocks.RED_FLOWER, 1, 2)).withIngredient(4, Items.EMERALD).withIngredient(5, Items.DYE, 2).withIngredient(6, Blocks.YELLOW_FLOWER).withIngredient(7, new ItemStack(Blocks.RED_FLOWER, 1, 1)).withIngredient(8, Blocks.RED_FLOWER).requires(ModRegistry.Skills.DOCTOR).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.BARREL).withIngredient(new int[]{0, 1, 2, 6, 7, 8}, Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE).requires(ModRegistry.Skills.GUN_PARTS_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.GUN_PARTS).withIngredient(new int[]{0, 2, 6, 8}, Blocks.IRON_BARS).withIngredient(new int[]{1, 7}, Blocks.IRON_TRAPDOOR).withIngredient(new int[]{3, 5}, Items.IRON_INGOT).withIngredient(4, Items.IRON_PICKAXE).requires(ModRegistry.Skills.GUN_PARTS_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.IRON_STOCK).withIngredient(new int[]{0, 1}, Blocks.IRON_BLOCK).withIngredient(new int[]{2, 4}, Items.IRON_INGOT).withIngredient(5, Items.FLINT_AND_STEEL).requires(ModRegistry.Skills.GUN_PARTS_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.LONG_BARREL).withIngredient(new int[]{1, 2}, ModRegistry.GRPGItems.BARREL).requires(ModRegistry.Skills.GUN_PARTS_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.MAGAZINE).withIngredient(new int[]{1, 2, 4, 5, 6, 7}, Items.IRON_INGOT).requires(ModRegistry.Skills.GUN_PARTS_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.SMALL_IRON_STOCK).withIngredient(0, Blocks.IRON_BLOCK).withIngredient(new int[]{1, 3}, Items.IRON_INGOT).withIngredient(4, Items.FLINT_AND_STEEL).requires(ModRegistry.Skills.GUN_PARTS_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.WOODEN_STOCK).withIngredient(new int[]{0, 1}, Blocks.LOG, Blocks.LOG2).withIngredient(2, Items.IRON_INGOT).withIngredient(4, Blocks.WOODEN_SLAB).withIngredient(5, Items.FLINT_AND_STEEL).requires(ModRegistry.Skills.GUN_PARTS_SMITH).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.PISTOL).withIngredient(0, ModRegistry.GRPGItems.BARREL).withIngredient(1, ModRegistry.GRPGItems.GUN_PARTS).withIngredient(2, ModRegistry.GRPGItems.SMALL_IRON_STOCK).requires(ModRegistry.Skills.PISTOL_ASSEMBLY).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.SMG).withIngredient(0, ModRegistry.GRPGItems.BARREL).withIngredient(1, ModRegistry.GRPGItems.GUN_PARTS).withIngredient(2, ModRegistry.GRPGItems.IRON_STOCK).withIngredient(4, ModRegistry.GRPGItems.MAGAZINE).requires(ModRegistry.Skills.SMG_ASSEMBLY).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.CROSSBOW).withIngredient(3, Blocks.TRIPWIRE_HOOK).withIngredient(4, ModRegistry.GRPGItems.GUN_PARTS).withIngredient(5, ModRegistry.GRPGItems.WOODEN_STOCK).withIngredient(new int[]{1, 7}, Items.BOW).requires(ModRegistry.Skills.CROSSBOW_ASSEMBLY).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.SHOTGUN).withIngredient(0, ModRegistry.GRPGItems.LONG_BARREL).withIngredient(1, ModRegistry.GRPGItems.GUN_PARTS).withIngredient(2, ModRegistry.GRPGItems.WOODEN_STOCK).withIngredient(4, ModRegistry.GRPGItems.MAGAZINE).requires(ModRegistry.Skills.SHOTGUN_ASSEMBLY).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.ASSAULT_RIFLE).withIngredient(0, ModRegistry.GRPGItems.LONG_BARREL).withIngredient(1, ModRegistry.GRPGItems.GUN_PARTS).withIngredient(2, ModRegistry.GRPGItems.IRON_STOCK).withIngredient(4, ModRegistry.GRPGItems.MAGAZINE).requires(ModRegistry.Skills.ASSAULT_RIFLE_ASSEMBLY).asRecipe());
        register(new RecipeBuilder().outputs(ModRegistry.GRPGItems.SNIPER_RIFLE).withIngredient(0, ModRegistry.GRPGItems.LONG_BARREL).withIngredient(1, ModRegistry.GRPGItems.GUN_PARTS).withIngredient(2, ModRegistry.GRPGItems.WOODEN_STOCK).requires(ModRegistry.Skills.SNIPER_RIFLE_ASSEMBLY).asRecipe());
    }

    public static SmithingRecipe findRecipe(TileEntitySmithingTable table, int selected) {
        if (selected >= 0 && selected < RECIPES.size()) {
            SmithingRecipe recipe = getRecipeById(selected);
            boolean valid = true;
            for (SmithingIngredient ingredient : recipe.getIngredients()) {
                ItemStack stack = table.getStackInSlot(ingredient.getIndex());
                if (!ModUtils.contains(stack, ingredient.items, ItemStack::isItemEqual)) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                return recipe;
            }
        }
        List<SmithingRecipe> validRecipes = new ArrayList<>();
        for (SmithingRecipe recipe : RECIPES) {
            boolean valid = true;
            for (SmithingIngredient ingredient : recipe.ingredients) {
                ItemStack item = table.getStackInSlot(ingredient.id);
                if (!ModUtils.contains(item, ingredient.items, ItemStack::isItemEqual)) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                validRecipes.add(recipe);
            }
        }
        if(!validRecipes.isEmpty()) {
            if(validRecipes.size() == 1) {
                return validRecipes.get(0);
            } else {
                SmithingRecipe matched = null;
                for (SmithingRecipe recipe : validRecipes) {
                    if (matched == null || recipe.ingredients.length > matched.ingredients.length) {
                        matched = recipe;
                    }
                }
                return matched;
            }
        }
        return null;
    }

    public static SmithingRecipe getRecipeByOutput(Item output) {
        for(SmithingRecipe recipe : RECIPES) {
            if(recipe.output.item.getItem() == output) {
                return recipe;
            }
        }
        return RECIPES.get(0);
    }

    public static int getRecipeId(SmithingRecipe recipe) {
        return RECIPES.indexOf(recipe);
    }

    public static SmithingRecipe getRecipeById(int id) {
        if (id >= 0 && id < RECIPES.size()) {
            return RECIPES.get(id);
        }
        return RECIPES.get(0);
    }

    private static void register(SmithingRecipe recipe) {
        RECIPES.add(recipe);
    }

    public static List<SmithingRecipe> getAvailableRecipes(EntityPlayer player) {
        PlayerSkills skills = PlayerDataFactory.get(player).getSkills();
        return RECIPES.stream().filter(r -> canCraftRecipe(r, skills)).collect(Collectors.toList());
    }

    public static boolean canCraftRecipe(SmithingRecipe recipe, PlayerSkills skills) {
        if (recipe.requiredTypes != null && recipe.requiredTypes.length > 0) {
            for (SkillType<?> type : recipe.requiredTypes) {
                if (!skills.hasSkill(type)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static class SmithingRecipe {
        private final SmithingIngredient[] ingredients;
        private final SmithingRecipeOutput output;
        private final SkillType<?>[] requiredTypes;

        public SmithingRecipe(RecipeBuilder builder) {
            this.ingredients = builder.ingredientList.toArray(new SmithingIngredient[0]);
            this.output = builder.output;
            this.requiredTypes = builder.requiredTypes;
        }

        public ItemStack getOutputForDisplay() {
            return output.item.copy();
        }

        public ItemStack getOutput(EntityPlayer player) {
            return output.getResult(player);
        }

        public SkillType<?>[] getRequiredTypes() {
            return requiredTypes;
        }

        public SmithingIngredient[] getIngredients() {
            return ingredients;
        }
    }

    public static class RecipeBuilder {
        private final List<SmithingIngredient> ingredientList = new ArrayList<>();
        private SmithingRecipeOutput output;
        private SkillType<?>[] requiredTypes;

        public RecipeBuilder withIngredient(int id, ItemStack stack) {
            if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
            ingredientList.add(new SmithingIngredient(id, stack));
            return this;
        }

        public RecipeBuilder withIngredient(int id, Block... blocks) {
            if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
            ingredientList.add(new SmithingIngredient(id, Arrays.stream(blocks).map(ItemStack::new).toArray(ItemStack[]::new)));
            return this;
        }

        public RecipeBuilder withIngredient(int id, Block block) {
            if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
            ingredientList.add(new SmithingIngredient(id, Item.getItemFromBlock(block)));
            return this;
        }

        public RecipeBuilder withIngredient(int id, Item item) {
            if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
            ingredientList.add(new SmithingIngredient(id, item));
            return this;
        }

        public RecipeBuilder withIngredient(int id, Item item, int meta) {
            if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
            ingredientList.add(new SmithingIngredient(id, item, meta));
            return this;
        }

        public RecipeBuilder withIngredient(int id, Item... items) {
            if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
            ingredientList.add(new SmithingIngredient(id, 0, items));
            return this;
        }

        public RecipeBuilder withIngredient(int[] ints, Item item, int meta) {
            for (int id : ints) {
                if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
                ingredientList.add(new SmithingIngredient(id, item, meta));
            }
            return this;
        }

        public RecipeBuilder withIngredient(int[] ints, Item... items) {
            for (int id : ints) {
                if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
                ingredientList.add(new SmithingIngredient(id, 0, items));
            }
            return this;
        }

        public RecipeBuilder withIngredient(int[] ints, int meta, Item... items) {
            for (int id : ints) {
                if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
                ingredientList.add(new SmithingIngredient(id, meta, items));
            }
            return this;
        }

        public RecipeBuilder withIngredient(int[] ints, Block block) {
            return this.withIngredient(ints, Item.getItemFromBlock(block));
        }

        public RecipeBuilder withIngredient(int[] ints, Block... blocks) {
            return this.withIngredient(ints, Arrays.stream(blocks).map(Item::getItemFromBlock).toArray(Item[]::new));
        }

        public RecipeBuilder outputs(Item item) {
            this.output = new SmithingRecipeOutput(item, p -> 1);
            return this;
        }

        public RecipeBuilder outputs(ItemStack stack, Function<EntityPlayer, Integer> toIntFunction) {
            this.output = new SmithingRecipeOutput(stack, toIntFunction);
            return this;
        }

        public RecipeBuilder outputs(Item item, int amount) {
            this.output = new SmithingRecipeOutput(item, p -> amount);
            return this;
        }

        public RecipeBuilder outputs(Item item, Function<EntityPlayer, Integer> amountFunction) {
            this.output = new SmithingRecipeOutput(item, amountFunction);
            return this;
        }

        public RecipeBuilder requires(SkillType<?>... types) {
            this.requiredTypes = types;
            return this;
        }

        public SmithingRecipe asRecipe() {
            return new SmithingRecipe(this);
        }
    }

    public static class SmithingIngredient {

        private final int id;
        private final ItemStack[] items;

        private SmithingIngredient(int id, Item item, int meta) {
            this(id, new ItemStack(item, 1, meta));
        }

        private SmithingIngredient(int id, Item item) {
            this(id, new ItemStack(item));
        }

        private SmithingIngredient(int id, int meta, Item... items) {
            this(id, Arrays.stream(items).map(item -> new ItemStack(item, 1, meta)).toArray(ItemStack[]::new));
        }

        private SmithingIngredient(int id, ItemStack item) {
            this(id, new ItemStack[]{item});
        }

        private SmithingIngredient(int id, ItemStack[] item) {
            this.id = id;
            this.items = item;
        }

        public int getIndex() {
            return id;
        }

        public ItemStack getFirstItem() {
            return items[0].copy();
        }

        public Item[] getEntries() {
            Item[] arr = new Item[items.length];
            int j = 0;
            for(ItemStack stack : items) {
                arr[j] = stack.getItem();
                ++j;
            }
            return arr;
        }
    }

    public static class SmithingRecipeOutput {

        private final Function<EntityPlayer, Integer> amount;
        private final ItemStack item;

        private SmithingRecipeOutput(Item item, Function<EntityPlayer, Integer> amount) {
            this.item = new ItemStack(item);
            this.amount = amount;
        }

        private SmithingRecipeOutput(ItemStack stack, Function<EntityPlayer, Integer> toIntFunction) {
            this.item = stack;
            this.amount = toIntFunction;
        }

        public ItemStack getResult(EntityPlayer player) {
            ItemStack stack = item.copy();
            stack.setCount(amount.apply(player));
            return stack;
        }
    }
}
