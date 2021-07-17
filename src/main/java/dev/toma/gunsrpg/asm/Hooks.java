package dev.toma.gunsrpg.asm;

import dev.toma.gunsrpg.common.capability.PlayerData;
import dev.toma.gunsrpg.common.capability.PlayerDataFactory;
import dev.toma.gunsrpg.common.capability.object.PlayerSkills;
import dev.toma.gunsrpg.common.init.GRPGItems;
import dev.toma.gunsrpg.common.init.Skills;
import dev.toma.gunsrpg.common.skills.AdrenalineRushSkill;
import dev.toma.gunsrpg.common.skills.MotherlodeSkill;
import dev.toma.gunsrpg.util.SkillUtil;
import dev.toma.gunsrpg.util.object.Pair;
import dev.toma.gunsrpg.world.cap.WorldDataFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.LazyOptional;

import java.util.*;

public class Hooks {

    private static final Map<Item, Item> ORE2CHUNK_MAP = new IdentityHashMap<>(2);

    public static double modifyAttackDelay(PlayerEntity player) {
        double value = player.getAttributeValue(Attributes.ATTACK_SPEED);
        LazyOptional<PlayerData> optional = PlayerDataFactory.get(player);
        if (optional.isPresent()) {
            PlayerData data = optional.orElse(null);
            PlayerSkills skills = data.getSkills();
            AdrenalineRushSkill adrenaline = SkillUtil.getBestSkillFromOverrides(skills.getSkill(Skills.ADRENALINE_RUSH_I), player);
            if (adrenaline != null && adrenaline.apply(player)) {
                value *= adrenaline.getAttackSpeedBoost();
            }
        }
        return value;
    }

    public static double modifyFollowDistance(MobEntity mob) {
        double attributeValue = mob.getAttributeValue(Attributes.FOLLOW_RANGE);
        World world = mob.level;
        if (WorldDataFactory.isBloodMoon(world)) {
            return Math.max(attributeValue, 90/*GRPGConfig.worldConfig.bloodMoonMobAgroRange.get()*/);
        }
        return attributeValue;
    }

    public static List<ItemStack> modifyBlockDrops(LootTable table, LootContext context) {
        List<ItemStack> drops = table.getRandomItems(context);
        BlockState state = context.getParamOrNull(LootParameters.BLOCK_STATE);
        Entity entity = context.getParamOrNull(LootParameters.THIS_ENTITY);
        if (state == null || !(entity instanceof PlayerEntity)) {
            return drops;
        }
        Block block = state.getBlock();
        PlayerEntity player = (PlayerEntity) entity;
        PlayerData data = PlayerDataFactory.get(player).orElse(null);
        if (data == null)
            return drops;
        PlayerSkills skills = data.getSkills();
        if (block.getTags().contains(BlockTags.LOGS.getName())) { // is log
            MinecraftServer server = entity.getServer();
            if (server == null || !skills.hasSkill(Skills.LUMBERJACK_I))
                return drops;
            RecipeManager manager = server.getRecipeManager();
            List<ICraftingRecipe> craftingRecipes = manager.getAllRecipesFor(IRecipeType.CRAFTING);
            for (ICraftingRecipe recipe : craftingRecipes) {
                NonNullList<Ingredient> ingredients = recipe.getIngredients();
                if (ingredients.size() == 1) {
                    Ingredient ingredient = ingredients.get(0);
                    if (ingredient.test(new ItemStack(block))) {
                        ItemStack result = recipe.getResultItem();
                        Pair<Float, Float> chances = SkillUtil.getBestSkillFromOverrides(skills.getSkill(Skills.LUMBERJACK_I), player).getDropChances();
                        if (player.getRandom().nextFloat() < chances.getLeft()) {
                            drops.add(new ItemStack(result.getItem(), 1));
                        }
                        if (player.getRandom().nextFloat() < chances.getRight()) {
                            drops.add(new ItemStack(Items.STICK, 2));
                        }
                        break;
                    }
                }
            }
        } else if (block.getTags().contains(Tags.Blocks.ORES.getName())) {
            MotherlodeSkill skill = skills.getSkill(Skills.MOTHER_LODE_I);
            if (skill != null) {
                skill = SkillUtil.getBestSkillFromOverrides(skill, player);
                Pair<Float, Float> multiplierChances = skill.getDropChances();
                Random random = player.getRandom();
                int multiplier = random.nextFloat() < multiplierChances.getRight() ? 3 : random.nextFloat() < multiplierChances.getLeft() ? 2 : 1;
                Iterator<ItemStack> iterator = drops.iterator();
                List<ItemStack> pending = new ArrayList<>();
                while (iterator.hasNext()) {
                    ItemStack stack = iterator.next();
                    Item replacement = ORE2CHUNK_MAP.get(stack.getItem());
                    if (replacement != null) {
                        pending.add(new ItemStack(replacement, Math.min(64, stack.getCount() * multiplier)));
                        iterator.remove();
                    } else if (stack.getItem() != block.asItem()) {
                        stack.setCount(Math.min(64, stack.getCount() * multiplier));
                    }
                }
                drops.addAll(pending);
            }
        }
        return drops;
    }

    public static void initOre2ChunkMap() {
        ORE2CHUNK_MAP.put(Blocks.IRON_ORE.asItem(), GRPGItems.IRON_ORE_CHUNK);
        ORE2CHUNK_MAP.put(Blocks.GOLD_ORE.asItem(), GRPGItems.GOLD_ORE_CHUNK);
    }
}
