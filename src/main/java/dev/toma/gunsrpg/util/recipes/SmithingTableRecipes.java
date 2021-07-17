package dev.toma.gunsrpg.util.recipes;

import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.init.GRPGItems;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.skills.core.SkillType;
import dev.toma.gunsrpg.common.tileentity.SmithingTableTileEntity;
import dev.toma.gunsrpg.util.SkillUtil;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @deprecated Use {@link net.minecraft.item.crafting.IRecipe} API
 */
@Deprecated
public class SmithingTableRecipes {

    private static final List<SmithingRecipe> RECIPES = new ArrayList<>();

    /*
    0 1 2
    3 4 5
    6 7 8
     */
    public static void register() {
        register(new RecipeBuilder().out(GRPGItems.BACON_BURGER).add(0, Items.COOKED_BEEF).add(new int[]{1, 3}, Items.BREAD).add(4, Items.COOKED_PORKCHOP).req(Skills.LOCAL_CHEF).recipe());
        register(new RecipeBuilder().out(GRPGItems.FISH_AND_CHIPS).add(new int[]{3, 5}, Items.BAKED_POTATO).add(4, Items.COOKED_COD, Items.COOKED_SALMON).add(7, Items.CARROT).req(Skills.LOCAL_CHEF).recipe());
        register(new RecipeBuilder().out(GRPGItems.GARDEN_SOUP).add(1, Blocks.BROWN_MUSHROOM).add(new int[]{3, 5}, Items.CARROT).add(4, Items.BEETROOT).add(7, Items.WATER_BUCKET).add(new int[]{6, 8}, Items.POTATO).req(Skills.LOCAL_CHEF).bucket().recipe());
        register(new RecipeBuilder().out(GRPGItems.CHICKEN_DINNER).add(new int[]{3, 5}, Items.COOKED_CHICKEN).add(new int[]{4, 7}, Items.BAKED_POTATO).add(new int[]{6, 8}, Items.CARROT).req(Skills.LOCAL_CHEF).recipe());
        register(new RecipeBuilder().out(GRPGItems.FRUIT_SALAD).add(new int[]{0, 2}, Items.APPLE).add(1, Items.SUGAR).add(new int[]{3, 5}, Items.MELON).add(4, Blocks.PUMPKIN).req(Skills.LOCAL_CHEF).recipe());
        register(new RecipeBuilder().out(GRPGItems.EGG_SALAD).add(new int[]{1, 7}, Items.EGG).add(new int[]{3, 5}, Items.BAKED_POTATO).add(4, Items.WHEAT_SEEDS).req(Skills.LOCAL_CHEF).recipe());
        register(new RecipeBuilder().out(GRPGItems.DELUXE_MEAL).add(new int[]{0, 2}, Items.BAKED_POTATO).add(1, Items.CARROT).add(3, Items.COOKED_PORKCHOP).add(4, Items.COOKED_BEEF).add(5, Items.COOKED_CHICKEN).add(new int[]{6, 7, 8}, Items.BREAD).req(Skills.MASTER_CHEF).recipe());
        register(new RecipeBuilder().out(GRPGItems.MEATY_STEW_XXL).add(new int[]{0, 3}, Items.COOKED_MUTTON).add(new int[]{1, 4}, Items.COOKED_PORKCHOP).add(new int[]{2, 5}, Items.COOKED_BEEF).add(new int[]{6, 8}, Items.POTATO).add(7, Items.WATER_BUCKET).req(Skills.MASTER_CHEF).bucket().recipe());
        register(new RecipeBuilder().out(GRPGItems.RABBIT_CREAMY_SOUP).add(new int[]{0, 2}, Items.POTATO).add(1, Items.MILK_BUCKET).add(new int[]{3, 5}, Items.CARROT).add(4, Blocks.BROWN_MUSHROOM).add(new int[]{6, 8}, Items.BREAD).add(7, Items.COOKED_RABBIT).req(Skills.MASTER_CHEF).bucket().recipe());
        register(new RecipeBuilder().out(GRPGItems.SHEPHERDS_PIE).add(new int[]{0, 1, 2}, Blocks.BROWN_MUSHROOM).add(new int[]{3, 5}, Items.COOKED_MUTTON).add(4, Items.BAKED_POTATO).add(new int[]{6, 7, 8}, Items.BREAD).req(Skills.MASTER_CHEF).recipe());
        register(new RecipeBuilder().out(GRPGItems.CHOCOLATE_GLAZED_APPLE_PIE).add(new int[]{0, 1, 2}, Items.APPLE).add(new int[]{3, 5}, Items.COOKIE).add(4, Items.SUGAR).add(new int[]{6, 8}, Items.EGG).add(7, Items.WHEAT).req(Skills.MASTER_CHEF).recipe());
        register(new RecipeBuilder().out(Items.GUNPOWDER, SkillUtil::getGunpowderCraftAmount).add(new int[]{0, 4}, Items.CHARCOAL).add(1, Items.INK_SAC).add(3, Items.SUGAR).req(Skills.GUNPOWDER_NOVICE).recipe());
        register(new RecipeBuilder().out(new ItemStack(Items.BONE_MEAL), SkillUtil::getBonemealCraftAmount).add(0, Items.BONE).req(Skills.BONE_GRINDER_I).recipe());
        register(new RecipeBuilder().out(GRPGItems.BLAZE_LUMP, SkillUtil::getBlazepowderCraftAmount).add(new int[]{0, 2}, Items.COAL).add(new int[]{1, 6, 7, 8}, Items.REDSTONE).add(new int[]{3, 5}, Items.GUNPOWDER).add(4, Items.SUGAR).req(Skills.BLACKSMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.LARGE_BULLET_CASING).add(new int[]{0, 1}, GRPGItems.SMALL_BULLET_CASING).req(Skills.WOODEN_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.SHOTGUN_SHELL).add(0, Items.RED_DYE).add(1, GRPGItems.LARGE_BULLET_CASING).add(2, Items.PAPER).req(Skills.WOODEN_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.BOLT_FLETCHING).add(0, Items.IRON_NUGGET).add(3, Items.FEATHER).add(6, Items.GOLD_NUGGET).req(Skills.WOODEN_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.SMALL_BULLET_CASING, 16).add(0, Items.COAL).add(1, Items.IRON_INGOT).add(2, Items.GOLD_INGOT).req(Skills.WOODEN_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.WOODEN_AMMO_9MM, SkillUtil::getAmmoAmount).add(0, Blocks.OAK_PLANKS).add(3, Items.GUNPOWDER).add(6, GRPGItems.SMALL_BULLET_CASING).req(Skills.WOODEN_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.STONE_AMMO_9MM, SkillUtil::getAmmoAmount).add(0, Blocks.STONE).add(3, Items.GUNPOWDER).add(6, GRPGItems.SMALL_BULLET_CASING).req(Skills.STONE_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.IRON_AMMO_9MM, SkillUtil::getAmmoAmount).add(0, Items.IRON_INGOT).add(3, Items.GUNPOWDER).add(6, GRPGItems.SMALL_BULLET_CASING).req(Skills.IRON_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.GOLD_AMMO_9MM, SkillUtil::getAmmoAmount).add(0, Items.GOLD_INGOT).add(3, Items.GUNPOWDER).add(6, GRPGItems.SMALL_BULLET_CASING).req(Skills.GOLD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.DIAMOND_AMMO_9MM, SkillUtil::getAmmoAmount).add(0, Items.DIAMOND).add(3, Items.GUNPOWDER).add(6, GRPGItems.SMALL_BULLET_CASING).req(Skills.DIAMOND_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.EMERALD_AMMO_9MM, SkillUtil::getAmmoAmount).add(0, Items.EMERALD).add(3, Items.GUNPOWDER).add(6, GRPGItems.SMALL_BULLET_CASING).req(Skills.EMERALD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.AMETHYST_AMMO_9MM, SkillUtil::getAmmoAmount).add(0, GRPGItems.AMETHYST).add(3, Items.GUNPOWDER).add(6, GRPGItems.SMALL_BULLET_CASING).req(Skills.AMETHYST_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.WOODEN_AMMO_45ACP, SkillUtil::getAmmoAmount).add(0, Blocks.OAK_PLANKS).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, GRPGItems.SMALL_BULLET_CASING).req(Skills.WOODEN_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.STONE_AMMO_45ACP, SkillUtil::getAmmoAmount).add(0, Blocks.STONE).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, GRPGItems.SMALL_BULLET_CASING).req(Skills.STONE_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.IRON_AMMO_45ACP, SkillUtil::getAmmoAmount).add(0, Items.IRON_INGOT).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, GRPGItems.SMALL_BULLET_CASING).req(Skills.IRON_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.GOLD_AMMO_45ACP, SkillUtil::getAmmoAmount).add(0, Items.GOLD_INGOT).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, GRPGItems.SMALL_BULLET_CASING).req(Skills.GOLD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.DIAMOND_AMMO_45ACP, SkillUtil::getAmmoAmount).add(0, Items.DIAMOND).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, GRPGItems.SMALL_BULLET_CASING).req(Skills.DIAMOND_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.EMERALD_AMMO_45ACP, SkillUtil::getAmmoAmount).add(0, Items.EMERALD).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, GRPGItems.SMALL_BULLET_CASING).req(Skills.EMERALD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.AMETHYST_AMMO_45ACP, SkillUtil::getAmmoAmount).add(0, GRPGItems.AMETHYST).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, GRPGItems.SMALL_BULLET_CASING).req(Skills.AMETHYST_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.WOODEN_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).add(0, Blocks.OAK_PLANKS).add(3, Items.STICK).add(6, GRPGItems.BOLT_FLETCHING).req(Skills.WOODEN_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.STONE_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).add(0, Blocks.STONE).add(3, Items.STICK).add(6, GRPGItems.BOLT_FLETCHING).req(Skills.STONE_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.IRON_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).add(0, Items.IRON_INGOT).add(3, Items.STICK).add(6, GRPGItems.BOLT_FLETCHING).req(Skills.IRON_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.GOLD_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).add(0, Items.GOLD_INGOT).add(3, Items.STICK).add(6, GRPGItems.BOLT_FLETCHING).req(Skills.GOLD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.DIAMOND_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).add(0, Items.DIAMOND).add(3, Items.STICK).add(6, GRPGItems.BOLT_FLETCHING).req(Skills.DIAMOND_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.EMERALD_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).add(0, Items.EMERALD).add(3, Items.STICK).add(6, GRPGItems.BOLT_FLETCHING).req(Skills.EMERALD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.AMETHYST_AMMO_CROSSBOW_BOLT, SkillUtil::getCrossbowBoltAmount).add(0, GRPGItems.AMETHYST).add(3, Items.STICK).add(6, GRPGItems.BOLT_FLETCHING).req(Skills.AMETHYST_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.WOODEN_AMMO_12G, SkillUtil::getAmmoAmount).add(0, Blocks.OAK_PLANKS).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, GRPGItems.SHOTGUN_SHELL).req(Skills.WOODEN_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.STONE_AMMO_12G, SkillUtil::getAmmoAmount).add(0, Blocks.STONE).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, GRPGItems.SHOTGUN_SHELL).req(Skills.STONE_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.IRON_AMMO_12G, SkillUtil::getAmmoAmount).add(0, Items.IRON_INGOT).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, GRPGItems.SHOTGUN_SHELL).req(Skills.IRON_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.GOLD_AMMO_12G, SkillUtil::getAmmoAmount).add(0, Items.GOLD_INGOT).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, GRPGItems.SHOTGUN_SHELL).req(Skills.GOLD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.DIAMOND_AMMO_12G, SkillUtil::getAmmoAmount).add(0, Items.DIAMOND).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, GRPGItems.SHOTGUN_SHELL).req(Skills.DIAMOND_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.EMERALD_AMMO_12G, SkillUtil::getAmmoAmount).add(0, Items.EMERALD).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, GRPGItems.SHOTGUN_SHELL).req(Skills.EMERALD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.AMETHYST_AMMO_12G, SkillUtil::getAmmoAmount).add(0, GRPGItems.AMETHYST).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, GRPGItems.SHOTGUN_SHELL).req(Skills.AMETHYST_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.WOODEN_AMMO_556MM, SkillUtil::getAmmoAmount).add(0, Blocks.OAK_PLANKS).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, GRPGItems.LARGE_BULLET_CASING).req(Skills.WOODEN_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.STONE_AMMO_556MM, SkillUtil::getAmmoAmount).add(0, Blocks.STONE).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, GRPGItems.LARGE_BULLET_CASING).req(Skills.STONE_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.IRON_AMMO_556MM, SkillUtil::getAmmoAmount).add(0, Items.IRON_INGOT).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, GRPGItems.LARGE_BULLET_CASING).req(Skills.IRON_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.GOLD_AMMO_556MM, SkillUtil::getAmmoAmount).add(0, Items.GOLD_INGOT).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, GRPGItems.LARGE_BULLET_CASING).req(Skills.GOLD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.DIAMOND_AMMO_556MM, SkillUtil::getAmmoAmount).add(0, Items.DIAMOND).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, GRPGItems.LARGE_BULLET_CASING).req(Skills.DIAMOND_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.EMERALD_AMMO_556MM, SkillUtil::getAmmoAmount).add(0, Items.EMERALD).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, GRPGItems.LARGE_BULLET_CASING).req(Skills.EMERALD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.AMETHYST_AMMO_556MM, SkillUtil::getAmmoAmount).add(0, GRPGItems.AMETHYST).add(new int[]{3, 4}, Items.GUNPOWDER).add(6, GRPGItems.LARGE_BULLET_CASING).req(Skills.AMETHYST_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.WOODEN_AMMO_762MM, SkillUtil::getAmmoAmount).add(0, Blocks.OAK_PLANKS).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, GRPGItems.LARGE_BULLET_CASING).req(Skills.WOODEN_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.STONE_AMMO_762MM, SkillUtil::getAmmoAmount).add(0, Blocks.STONE).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, GRPGItems.LARGE_BULLET_CASING).req(Skills.STONE_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.IRON_AMMO_762MM, SkillUtil::getAmmoAmount).add(0, Items.IRON_INGOT).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, GRPGItems.LARGE_BULLET_CASING).req(Skills.IRON_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.GOLD_AMMO_762MM, SkillUtil::getAmmoAmount).add(0, Items.GOLD_INGOT).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, GRPGItems.LARGE_BULLET_CASING).req(Skills.GOLD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.DIAMOND_AMMO_762MM, SkillUtil::getAmmoAmount).add(0, Items.DIAMOND).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, GRPGItems.LARGE_BULLET_CASING).req(Skills.DIAMOND_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.EMERALD_AMMO_762MM, SkillUtil::getAmmoAmount).add(0, Items.EMERALD).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, GRPGItems.LARGE_BULLET_CASING).req(Skills.EMERALD_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.AMETHYST_AMMO_762MM, SkillUtil::getAmmoAmount).add(0, GRPGItems.AMETHYST).add(new int[]{3, 4, 5}, Items.GUNPOWDER).add(6, GRPGItems.LARGE_BULLET_CASING).req(Skills.AMETHYST_AMMO_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.GRENADE).add(new int[]{1, 3, 5, 7}, Items.IRON_INGOT).add(4, Blocks.TNT).add(2, Items.FLINT_AND_STEEL).req(Skills.GRENADES).recipe());
        register(new RecipeBuilder().out(GRPGItems.MASSIVE_GRENADE).add(new int[]{0, 2, 3, 5, 6, 7, 8}, Items.GOLD_NUGGET).add(1, Blocks.TNT).add(4, GRPGItems.GRENADE).req(Skills.MASSIVE_GRENADES).recipe());
        register(new RecipeBuilder().out(GRPGItems.IMPACT_GRENADE).add(new int[]{0, 2, 6, 8}, Items.REDSTONE).add(new int[]{3, 7, 5}, Items.GLOWSTONE_DUST).add(1, Items.BLAZE_POWDER).add(4, GRPGItems.GRENADE).req(Skills.IMPACT_GRENADES).recipe());
        register(new RecipeBuilder().out(GRPGItems.WOODEN_HAMMER).add(new int[]{3, 0, 1, 2, 5}, Blocks.OAK_LOG).add(new int[]{4, 7}, Items.STICK).req(Skills.HAMMER_I, Skills.HEAVY_PICKAXE_II, Skills.MOTHER_LODE_II).recipe());
        register(new RecipeBuilder().out(GRPGItems.STONE_HAMMER).add(new int[]{3, 0, 1, 2, 5}, Blocks.STONE).add(new int[]{4, 7}, Items.STICK).req(Skills.HAMMER_II, Skills.HEAVY_PICKAXE_III, Skills.MOTHER_LODE_III).recipe());
        register(new RecipeBuilder().out(GRPGItems.IRON_HAMMER).add(new int[]{3, 0, 1, 2, 5}, Items.IRON_INGOT).add(new int[]{4, 7}, Items.STICK).req(Skills.HAMMER_III, Skills.HEAVY_PICKAXE_IV, Skills.MOTHER_LODE_IV).recipe());
        register(new RecipeBuilder().out(GRPGItems.AMETHYST).add(1, Items.DIAMOND).add(new int[]{3, 5}, Items.QUARTZ).add(4, Items.BLAZE_POWDER).add(7, Items.EMERALD).req(Skills.MINERALOGIST).recipe());
        register(new RecipeBuilder().out(GRPGItems.PLASTER_CAST).add(new int[]{0, 2, 6, 8}, Items.BONE_MEAL).add(new int[]{1, 3, 5, 7}, Blocks.WHITE_CARPET).add(4, Items.DIAMOND).req(Skills.MEDIC).recipe());
        register(new RecipeBuilder().out(GRPGItems.BANDAGE, 2).add(new int[]{0, 8}, Items.YELLOW_DYE).add(new int[]{1, 3, 5, 7}, Items.PAPER).add(new int[]{2, 6}, Items.SLIME_BALL).add(4, Blocks.WHITE_WOOL).req(Skills.MEDIC).recipe());
        register(new RecipeBuilder().out(GRPGItems.VACCINE).add(8, Items.GOLDEN_APPLE).add(6, Items.GLISTERING_MELON_SLICE).add(4, GRPGItems.AMETHYST).add(2, Items.GOLDEN_CARROT).add(0, Items.IRON_INGOT).add(new int[]{1, 3, 5, 7}, Blocks.GLASS_PANE).req(Skills.DOCTOR).recipe());
        register(new RecipeBuilder().out(GRPGItems.ANTIDOTUM_PILLS, 2).add(new int[]{0, 2}, Items.PAPER).add(1, Items.SUGAR).add(3, Blocks.POPPY).add(4, Items.EMERALD).add(5, Items.GREEN_DYE).add(6, Blocks.DANDELION).add(7, Blocks.POPPY).add(8, Blocks.POPPY).req(Skills.DOCTOR).recipe());
        register(new RecipeBuilder().out(GRPGItems.ANALGETICS, 2).add(new int[]{0, 2}, Items.PAPER).add(1, Items.SUGAR).add(3, Blocks.DANDELION).add(4, Items.GREEN_DYE).add(5, Blocks.POPPY).add(new int[]{6, 7, 8}, Items.YELLOW_DYE).req(Skills.PHARMACIST_I).recipe());
        register(new RecipeBuilder().out(GRPGItems.STEREOIDS).add(0, Items.IRON_INGOT).add(new int[]{1, 3, 5, 7}, Blocks.GLASS_PANE).add(new int[]{2, 6}, Items.GOLDEN_CARROT).add(4, Blocks.REDSTONE_BLOCK).add(8, Items.SUGAR).req(Skills.PHARMACIST_II).recipe());
        register(new RecipeBuilder().out(GRPGItems.ADRENALINE).add(0, Items.IRON_INGOT).add(new int[]{1, 3, 5, 7}, Blocks.GLASS_PANE).add(new int[]{2, 6}, Items.GLISTERING_MELON_SLICE).add(4, Items.FERMENTED_SPIDER_EYE).add(8, Blocks.POPPY).req(Skills.PHARMACIST_III).recipe());
        register(new RecipeBuilder().out(GRPGItems.PAINKILLERS, 2).add(new int[]{0, 2}, Items.PAPER).add(1, Items.SUGAR).add(new int[]{3, 4, 5}, Items.FERMENTED_SPIDER_EYE).add(new int[]{6, 8}, Items.NETHER_WART).add(7, Blocks.REDSTONE_BLOCK).req(Skills.PHARMACIST_IV).recipe());
        register(new RecipeBuilder().out(GRPGItems.MORPHINE).add(0, Items.IRON_INGOT).add(new int[]{1, 3, 5, 7}, Blocks.GLASS_PANE).add(2, GRPGItems.STEREOIDS).add(4, GRPGItems.PAINKILLERS).add(6, GRPGItems.ADRENALINE).add(8, Items.ENDER_EYE).req(Skills.PHARMACIST_V).recipe());
        register(new RecipeBuilder().out(GRPGItems.BARREL).add(new int[]{0, 1, 2, 6, 7, 8}, Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE).req(Skills.GUN_PARTS_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.GUN_PARTS).add(new int[]{0, 2, 6, 8}, Blocks.IRON_BARS).add(new int[]{1, 7}, Blocks.IRON_TRAPDOOR).add(new int[]{3, 5}, Items.IRON_INGOT).add(4, Items.IRON_PICKAXE).req(Skills.GUN_PARTS_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.IRON_STOCK).add(new int[]{0, 1}, Blocks.IRON_BLOCK).add(new int[]{2, 4}, Items.IRON_INGOT).add(5, Items.FLINT_AND_STEEL).req(Skills.GUN_PARTS_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.LONG_BARREL).add(new int[]{1, 2}, GRPGItems.BARREL).req(Skills.GUN_PARTS_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.MAGAZINE).add(new int[]{1, 2, 4, 5, 6, 7}, Items.IRON_INGOT).req(Skills.GUN_PARTS_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.SMALL_IRON_STOCK).add(0, Blocks.IRON_BLOCK).add(new int[]{1, 3}, Items.IRON_INGOT).add(4, Items.FLINT_AND_STEEL).req(Skills.GUN_PARTS_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.WOODEN_STOCK).add(new int[]{0, 1}, Blocks.OAK_LOG).add(2, Items.IRON_INGOT).add(4, Blocks.OAK_SLAB).add(5, Items.FLINT_AND_STEEL).req(Skills.GUN_PARTS_SMITH).recipe());
        register(new RecipeBuilder().out(GRPGItems.PISTOL).add(0, GRPGItems.BARREL).add(1, GRPGItems.GUN_PARTS).add(2, GRPGItems.SMALL_IRON_STOCK).req(Skills.PISTOL_ASSEMBLY).recipe());
        register(new RecipeBuilder().out(GRPGItems.SMG).add(0, GRPGItems.BARREL).add(1, GRPGItems.GUN_PARTS).add(2, GRPGItems.IRON_STOCK).add(4, GRPGItems.MAGAZINE).req(Skills.SMG_ASSEMBLY).recipe());
        register(new RecipeBuilder().out(GRPGItems.CROSSBOW).add(3, Blocks.TRIPWIRE_HOOK).add(4, GRPGItems.GUN_PARTS).add(5, GRPGItems.WOODEN_STOCK).add(new int[]{1, 7}, Items.BOW).req(Skills.CROSSBOW_ASSEMBLY).recipe());
        register(new RecipeBuilder().out(GRPGItems.SHOTGUN).add(0, GRPGItems.LONG_BARREL).add(1, GRPGItems.GUN_PARTS).add(2, GRPGItems.WOODEN_STOCK).add(4, GRPGItems.MAGAZINE).req(Skills.SHOTGUN_ASSEMBLY).recipe());
        register(new RecipeBuilder().out(GRPGItems.ASSAULT_RIFLE).add(0, GRPGItems.LONG_BARREL).add(1, GRPGItems.GUN_PARTS).add(2, GRPGItems.IRON_STOCK).add(4, GRPGItems.MAGAZINE).req(Skills.ASSAULT_RIFLE_ASSEMBLY).recipe());
        register(new RecipeBuilder().out(GRPGItems.SNIPER_RIFLE).add(0, GRPGItems.LONG_BARREL).add(1, GRPGItems.GUN_PARTS).add(2, GRPGItems.WOODEN_STOCK).req(Skills.SNIPER_RIFLE_ASSEMBLY).recipe());
    }

    public static List<SmithingRecipe> getRecipes() {
        return RECIPES;
    }

    public static SmithingRecipe findRecipe(SmithingTableTileEntity table, int selected) {
        IItemHandlerModifiable handler = table.getInventory().orElseThrow(NullPointerException::new);
        if (selected >= 0 && selected < RECIPES.size()) {
            SmithingRecipe recipe = getRecipeById(selected);
            boolean valid = true;
            for (SmithingIngredient ingredient : recipe.getIngredients()) {
                ItemStack stack = handler.getStackInSlot(ingredient.getIndex());
                if (!ingredient.test(stack)) {
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
                ItemStack item = handler.getStackInSlot(ingredient.id);
                if (!ingredient.test(item)) {
                    valid = false;
                    break;
                }
            }
            if (valid) {
                validRecipes.add(recipe);
            }
        }
        if (!validRecipes.isEmpty()) {
            if (validRecipes.size() == 1) {
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
        for (SmithingRecipe recipe : RECIPES) {
            if (recipe.output.item.getItem() == output) {
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

    public static List<SmithingRecipe> getAvailableRecipes(PlayerEntity player) {
        PlayerSkills skills = PlayerDataFactory.get(player).orElseThrow(NullPointerException::new).getSkills();
        return RECIPES.stream().filter(r -> hasRequiredSkills(r, skills)).collect(Collectors.toList());
    }

    public static boolean hasRequiredSkills(SmithingRecipe recipe, PlayerSkills skills) {
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
        private final boolean returnsBucket;

        public SmithingRecipe(RecipeBuilder builder) {
            this.ingredients = builder.ingredientList.toArray(new SmithingIngredient[0]);
            this.output = builder.output;
            this.requiredTypes = builder.requiredTypes;
            this.returnsBucket = builder.returnsBucket;
        }

        public ItemStack getOutputForDisplay() {
            return output.item.copy();
        }

        public ItemStack getOutput(PlayerEntity player) {
            if (!player.level.isClientSide && returnsBucket) {
                player.addItem(new ItemStack(Items.BUCKET));
            }
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
        private boolean returnsBucket;

        private RecipeBuilder add(int integer, Function<Integer, SmithingIngredient> ingredientFunction) {
            return add(new int[]{integer}, ingredientFunction);
        }

        private RecipeBuilder add(int[] ints, Function<Integer, SmithingIngredient> ingredientFunction) {
            for (int i : ints) {
                if (ingredientList.size() > 8) throw new IllegalArgumentException("Added way too many ingredients!");
                ingredientList.add(ingredientFunction.apply(i));
            }
            return this;
        }

        private Item[] convert(Block[] array) {
            Item[] items = new Item[array.length];
            for (int i = 0; i < array.length; i++) {
                items[i] = array[i].asItem();
            }
            return items;
        }

        public RecipeBuilder add(int[] ints, Item... item) {
            return add(ints, id -> new SmithingIngredient(id, item));
        }

        public RecipeBuilder add(int integer, Item... item) {
            return add(integer, id -> new SmithingIngredient(id, item));
        }

        public RecipeBuilder add(int[] ints, Block block, int subtypes) {
            return add(ints, id -> new SmithingIngredient(id, block.asItem()));
        }

        public RecipeBuilder add(int integer, Block... block) {
            return add(integer, convert(block));
        }

        public RecipeBuilder add(int[] ints, Item item) {
            return add(ints, id -> new SmithingIngredient(id, item));
        }

        public RecipeBuilder add(int integer, Item item) {
            return add(new int[]{integer}, item);
        }

        public RecipeBuilder add(int[] ints, Block block) {
            return add(ints, id -> new SmithingIngredient(id, block.asItem()));
        }

        public RecipeBuilder add(int integer, Block block) {
            return add(new int[]{integer}, block);
        }

        public RecipeBuilder out(Item item) {
            this.output = new SmithingRecipeOutput(item, p -> 1);
            return this;
        }

        public RecipeBuilder out(ItemStack stack, Function<PlayerEntity, Integer> toIntFunction) {
            this.output = new SmithingRecipeOutput(stack, toIntFunction);
            return this;
        }

        public RecipeBuilder out(Item item, int amount) {
            this.output = new SmithingRecipeOutput(item, p -> amount);
            return this;
        }

        public RecipeBuilder out(Item item, Function<PlayerEntity, Integer> amountFunction) {
            this.output = new SmithingRecipeOutput(item, amountFunction);
            return this;
        }

        public RecipeBuilder req(SkillType<?>... types) {
            this.requiredTypes = types;
            return this;
        }

        public RecipeBuilder bucket() {
            this.returnsBucket = true;
            return this;
        }

        public SmithingRecipe recipe() {
            return new SmithingRecipe(this);
        }
    }

    public static class SmithingIngredient implements Predicate<ItemStack> {

        private final int id;
        private final Item[] item;

        public SmithingIngredient(int index, Item item) {
            this(index, new Item[]{item});
        }

        public SmithingIngredient(int index, Item[] item) {
            this.id = index;
            this.item = item;
        }

        @Override
        public boolean test(ItemStack stack) {
            Item testItem = stack.getItem();
            if (stack.isEmpty()) {
                return false;
            }
            for (Item variant : item) {
                if (testItem == variant) {
                    return true;
                }
            }
            return false;
        }

        public int getIndex() {
            return id;
        }

        public Item[] getItems() {
            return item;
        }
    }

    public static class SmithingRecipeOutput {

        private final Function<PlayerEntity, Integer> amount;
        private final ItemStack item;

        private SmithingRecipeOutput(Item item, Function<PlayerEntity, Integer> amount) {
            this.item = new ItemStack(item);
            this.amount = amount;
        }

        private SmithingRecipeOutput(ItemStack stack, Function<PlayerEntity, Integer> toIntFunction) {
            this.item = stack;
            this.amount = toIntFunction;
        }

        public ItemStack getResult(PlayerEntity player) {
            ItemStack stack = item.copy();
            stack.setCount(amount.apply(player));
            return stack;
        }
    }
}
